package beta.infrastructure.http

import akka.http.scaladsl.model.StatusCodes._
import akka.http.scaladsl.server.{Directives, Route, _}
import akka.stream.Materializer
import beta.domain.services.{EOQService, ProductService}
import beta.domain.{GenericError, ItemNotFound, UnexpectedError}
import beta.infrastructure.http.dtos.{
  DiscountQuantityDTO,
  ProductDTO,
  ProductVariablesDTO,
  UpdateProductDTO
}
import de.heikoseeberger.akkahttpcirce.FailFastCirceSupport._
import io.circe.generic.auto._
import io.circe.java8.time._

import scala.concurrent.ExecutionContext
import scala.util.{Failure, Success}

trait ProductRoutes extends Directives with ProductService with EOQService {

  import beta.infrastructure.mapper.MapperProductDTO._

  implicit val executionContext: ExecutionContext
  implicit val materializer: Materializer

  def addProduct: Route = path("product") {
    post {
      entity(as[ProductDTO]) { product =>
        extractRequest { implicit req =>
          onComplete(addNewProduct(product)) {
            case Failure(ex) => {
              println(s"Exception: $ex")
              complete(InternalServerError -> UnexpectedError())
            }
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

  def updateProduct: Route = path("product" / Segment) { ref =>
    put {
      entity(as[UpdateProductDTO]) { product =>
        extractRequest { implicit req =>
          onComplete(updateAProduct(ref, product)) {
            case Failure(ex) => {
              println(s"Exception: $ex")
              complete(InternalServerError -> UnexpectedError())
            }
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

  def getProductByReference: Route = path("product" / Segment) { ref =>
    get {
      extractRequest { implicit req =>
        onComplete(getProductByRef(ref)) {
          case Failure(ex) => {
            println(s"Exception: $ex")
            complete(InternalServerError -> UnexpectedError())
          }
          case Success(response) =>
            response.fold(complete(NotFound -> ItemNotFound())) { r =>
              complete(OK -> r)
            }
        }
      }
    }
  }

  def getProducts: Route = path("product") {
    get {
      extractRequest { implicit req =>
        onComplete(getAllProducts) {
          case Failure(ex) => {
            println(s"Exception: $ex")
            complete(InternalServerError -> UnexpectedError())
          }
          case Success(response) =>
            complete(OK -> response)
        }
      }
    }
  }

  def deleteAProduct: Route = path("product" / Segment) { ref =>
    delete {
      extractRequest { implicit req =>
        onComplete(deleteProduct(ref)) {
          case Failure(ex) => {
            println(s"Exception: $ex")
            complete(InternalServerError -> UnexpectedError())
          }
          case Success(response) =>
            response.fold(complete(NotFound -> ItemNotFound())) { r =>
              complete(OK -> r)
            }
        }
      }
    }
  }

  def calculateEOQ: Route = path("product" / "eoq" / Segment) { ref =>
    post {
      entity(as[ProductVariablesDTO]) { product =>
        extractRequest { implicit req =>
          onComplete(addEOQ(ref, product)) {
            case Failure(ex) => {
              println(s"Exception: $ex")
              complete(InternalServerError -> UnexpectedError())
            }
            case Success(response) =>
              response.fold(
                err =>
                  complete(BadRequest -> GenericError(err.code, err.message)),
                pr => complete(Created -> pr)
              )
          }
        }
      }
    }
  }

  def updateEOQ: Route = path("product" / "eoq" / Segment) { ref =>
    put {
      entity(as[ProductVariablesDTO]) { product =>
        extractRequest { implicit req =>
          onComplete(updateEOQModel(ref, product)) {
            case Failure(ex) => {
              println(s"Exception: $ex")
              complete(InternalServerError -> UnexpectedError())
            }
            case Success(response) =>
              response.fold(
                err =>
                  complete(BadRequest -> GenericError(err.code, err.message)),
                pr => complete(Created -> pr)
              )
          }
        }
      }
    }
  }

  def getEOQByRef: Route = path("product" / "eoq" / Segment) { ref =>
    get {
      extractRequest { implicit req =>
        onComplete(getEoQByRef(ref)) {
          case Failure(ex) => {
            println(s"Exception: $ex")
            complete(InternalServerError -> UnexpectedError())
          }
          case Success(response) =>
            response.fold(complete(NotFound -> ItemNotFound())) { r =>
              complete(OK -> r)
            }
        }
      }
    }
  }

  def discountProduct: Route = path("product" / "discount" / Segment) { ref =>
    post {
      entity(as[DiscountQuantityDTO]) { q =>
        extractRequest { implicit req =>
          onComplete(discountQuantity(ref, q.quantity)) {
            case Failure(ex) => {
              println(s"Exception: $ex")
              complete(InternalServerError -> UnexpectedError())
            }
            case Success(response) =>
              response.fold(
                err =>
                  complete(BadRequest -> GenericError(err.code, err.message)),
                r => complete(OK -> r.to[ProductDTO])
              )
          }
        }
      }
    }
  }

  val inventoryRoutes
    : Route = addProduct ~ updateProduct ~ getProductByReference ~ getProducts ~ deleteAProduct ~ calculateEOQ ~ updateEOQ ~ discountProduct ~ getEOQByRef
}
