package beta.infrastructure.http

import beta.domain.{
  DomainError,
  Product,
  ProductAlreadyExists,
  UnexpectedError,
  _
}
import beta.infrastructure.database.entities.ProductEntity
import beta.infrastructure.database.repository.Repository
import beta.infrastructure.http.dtos.{ProductDTO, UpdateProductDTO}
import cats.data.{EitherT, OptionT}
import cats.implicits._

import scala.concurrent.{ExecutionContext, Future}

trait InventoryService {

  implicit val executionContext: ExecutionContext
  val repository: Repository

  def addNewProduct(product: ProductDTO): Future[Either[DomainError, Product]] =
    (for {
      pd <- EitherT.fromEither[Future](Product.validate(product))
      _ <- OptionT(repository.getProductByRef(pd.ref))
        .map(
          _ => ProductAlreadyExists()
        )
        .toRight(())
        .swap
      spd <- EitherT(
        repository
          .saveOrUpdateProduct(pd)
          .map(Some(_).toRight[DomainError](UnexpectedError())))
    } yield spd).value

  def updateAProduct(
      ref: String,
      product: UpdateProductDTO): Future[Either[DomainError, Product]] = {
    (for {
      rf <- EitherT.fromEither[Future](validateString(ref, "Ref"))
      _ <- EitherT(
        repository.getProductByRef(rf).map(_.toRight(ItemNotFound())))
      vlp <- EitherT.fromEither[Future](Product.validate(rf, product))
      udp <- EitherT(
        repository
          .saveOrUpdateProduct(vlp)
          .map(Some(_).toRight[DomainError](UnexpectedError())))
    } yield udp).value
  }

  def getProductByRef(ref: String): Future[Option[ProductEntity]] =
    repository.getProductByRef(ref)

  def getAllProducts: Future[List[ProductEntity]] =
    repository.getAllProducts

  def deleteProduct(ref: String): Future[Option[ProductEntity]] =
    repository.deleteProduct(ref)
}
