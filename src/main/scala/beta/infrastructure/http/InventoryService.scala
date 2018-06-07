package beta.infrastructure.http

import beta.domain.{
  DomainError,
  Product,
  ProductAlreadyExists,
  UnexpectedError,
  _
}
import beta.infrastructure.database.repository.Repository
import beta.infrastructure.http.dtos.ProductDTO
import cats.data.{EitherT, OptionT}
import cats.implicits._

import scala.concurrent.{ExecutionContext, Future}

trait InventoryService {

  implicit val executionContext: ExecutionContext
  val repository: Repository

  def addNewProduct(
      product: ProductDTO): Future[Either[DomainError, Product]] = {
    (for {
      rf <- EitherT.fromEither[Future](validateString(product.ref, "Ref"))
      _ <- OptionT(repository.getProductByRef(rf))
        .map(
          _ => ProductAlreadyExists()
        )
        .toRight(())
        .swap
      pd <- EitherT.fromEither[Future](Product.validate(product))
      spd <- EitherT(
        repository
          .saveOrUpdateProduct(pd)
          .map(Some(_).toRight[DomainError](UnexpectedError())))
    } yield spd).value
  }

}
