package beta.infrastructure.http

import akka.http.scaladsl.model.StatusCodes._
import akka.http.scaladsl.server.{Directives, Route, _}
import akka.stream.Materializer
import beta.domain.{GenericError, ItemNotFound, UnexpectedError}
import beta.infrastructure.http.dtos.{ProductDTO, UpdateProductDTO}
import de.heikoseeberger.akkahttpcirce.FailFastCirceSupport._
import io.circe.generic.auto._
import io.circe.java8.time._

import scala.concurrent.ExecutionContext
import scala.util.{Failure, Success}

trait InventoryRoutes extends Directives with InventoryService {

  import beta.infrastructure.mapper.MapperProductDTO._

  implicit val executionContext: ExecutionContext
  implicit val materializer: Materializer

  def addProduct: Route = path("beta" / "product") {
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

  def updateProduct: Route = path("beta" / "product" / Segment) { ref =>
    put {
      entity(as[UpdateProductDTO]) { product =>
        extractRequest { implicit req =>
          onComplete(updateAProduct(ref, product)) {
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

  def getProductByReference: Route = path("beta" / "product" / Segment) { ref =>
    get {
      extractRequest { implicit req =>
        onComplete(getProductByRef(ref)) {
          case Failure(_) =>
            complete(InternalServerError -> UnexpectedError())
          case Success(response) =>
            response.fold(complete(NotFound -> ItemNotFound())) { r =>
              complete(OK -> r)
            }
        }
      }
    }
  }

  def getProducts: Route = path("beta" / "product") {
    get {
      extractRequest { implicit req =>
        onComplete(getAllProducts) {
          case Failure(_) =>
            complete(InternalServerError -> UnexpectedError())
          case Success(response) =>
            complete(OK -> response)
        }
      }
    }
  }

  def deleteAProduct: Route = path("beta" / "product" / Segment) { ref =>
    delete {
      extractRequest { implicit req =>
        onComplete(deleteProduct(ref)) {
          case Failure(_) =>
            complete(InternalServerError -> UnexpectedError())
          case Success(response) =>
            response.fold(complete(NotFound -> ItemNotFound())) { r =>
              complete(OK -> r)
            }
        }
      }
    }
  }

  val inventoryRoutes
    : Route = addProduct ~ updateProduct ~ getProductByReference ~ getProducts ~ deleteAProduct
}
