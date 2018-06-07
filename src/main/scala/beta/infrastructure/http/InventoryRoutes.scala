package beta.infrastructure.http

import akka.http.scaladsl.model.StatusCodes.{
  BadRequest,
  Created,
  InternalServerError
}
import akka.http.scaladsl.server.{Directives, Route, _}
import akka.stream.Materializer
import beta.domain.{GenericError, UnexpectedError}
import beta.infrastructure.http.dtos.ProductDTO
import de.heikoseeberger.akkahttpcirce.FailFastCirceSupport._
import io.circe.generic.auto._

import scala.concurrent.ExecutionContext
import scala.util.{Failure, Success}

trait InventoryRoutes extends Directives with InventoryService {

  //import akka.http.scaladsl.marshalling.ToResponseMarshallable._
  import beta.infrastructure.mapper.MapperProductDTO._

  implicit val executionContext: ExecutionContext
  implicit val materializer: Materializer

  def addProduct = path("beta" / "product") {
    post {
      entity(as[ProductDTO]) { product =>
        extractRequest { implicit req =>
          onComplete(addNewProduct(product)) {
            case Failure(_) =>
              complete(InternalServerError -> UnexpectedError())
            case Success(response) =>
              response.fold(
                err =>
                  complete(BadRequest -> GenericError(err.code, err.message)),
                pr => complete(Created -> pr.to[ProductDTO])
              )
          }
        }
      }
    }
  }

  val inventoryRoutes: Route = addProduct

}
