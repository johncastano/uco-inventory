package beta.infrastructure.database.repository

import beta.infrastructure.database.DatabaseProvider
import beta.domain.{EOQModel, Product}
import beta.infrastructure.database.entities.{EOQEntity, ProductEntity}
import com.outworkers.phantom.dsl._

import scala.concurrent.Future

class Repository(implicit dbProvider: DatabaseProvider) {

  import dbProvider.database
  import beta.infrastructure.mapper.MapperProductEntity._

  def saveOrUpdateProduct(product: Product): Future[Product] =
    database.productTable
      .saveOrUpdate(product.to[ProductEntity])
      .map(_ => product)

  def getAllProducts: Future[List[ProductEntity]] =
    database.productTable.getProducts

  def getProductByRef(ref: String): Future[Option[ProductEntity]] =
    database.productTable.getProductByRef(ref)

  def deleteProduct(ref: String): Future[Option[ProductEntity]] =
    for {
      product <- database.productTable.getProductByRef(ref)
      _ <- database.productTable.deleteProduct(ref)
    } yield product

  def saveOrUpdateEOQ(eoq: EOQModel): Future[EOQEntity] = {
    val entity = eoq.to[EOQEntity]
    database.eoqTable.saveOrUpdate(entity).map(_ => entity)
  }

  def getEOQByRef(ref: String): Future[Option[EOQEntity]] =
    database.eoqTable.getEOQByRef(ref)
}
