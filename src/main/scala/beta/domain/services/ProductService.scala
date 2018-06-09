package beta.domain.services

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

trait ProductService extends EOQService {

  import ProductService._
  import beta.infrastructure.mapper.MapperProductEntity._

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

  def discountQuantity(ref: String,
                       quantity: Int): Future[Either[DomainError, Product]] =
    (for {
      rf <- EitherT.fromEither[Future](validateString(ref, "Ref"))
      q <- EitherT.fromEither[Future](validateInt(quantity, "Quantity"))
      product <- EitherT(
        repository.getProductByRef(rf).map(_.toRight(ItemNotFound())))
      dq <- EitherT.fromEither[Future](makeDiscount(product.to[Product], q))
      eoqm <- EitherT(
        repository.getEOQByRef(rf).map(_.toRight(EOQIsNecessary())))
      fp <- OptionT
        .pure[Future](checkReorderPoint(dq, eoqm.to[EOQModel]))
        .toRight(ErrorCalculatingReorder())
      sv <- EitherT(
        repository
          .saveOrUpdateProduct(fp)
          .map(Some(_).toRight[DomainError](UnexpectedError())))
    } yield sv).value

  def getProductByRef(ref: String): Future[Option[ProductEntity]] =
    repository.getProductByRef(ref)

  def getAllProducts: Future[List[ProductEntity]] =
    repository.getAllProducts

  def deleteProduct(ref: String): Future[Option[ProductEntity]] =
    repository.deleteProduct(ref)
}

object ProductService {

  def makeDiscount(product: Product,
                   quantity: Int): Either[DomainError, Product] = {
    val newQuantity = product.quantity - quantity
    if (newQuantity < 0)
      Left(InsufficientProduct())
    else Right(product.copy(quantity = newQuantity))
  }

  def checkReorderPoint(product: Product, eoq: EOQModel): Product =
    if (product.quantity <= eoq.r)
      orderProducts(product, eoq.q)
    else
      product

  def orderProducts(product: Product, quantity: Int): Product = {
    val newQuantity = product.quantity + quantity
    product.copy(quantity = newQuantity)
  }

}
