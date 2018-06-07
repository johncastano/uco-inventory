package beta.infrastructure.database.tables

import beta.infrastructure.database.entities.ProductEntity
import com.outworkers.phantom.dsl._
import com.outworkers.phantom.jdk8._

import scala.concurrent.Future

abstract class ProductTable extends Table[ProductTable, ProductEntity] {
  override def tableName: String = "product"

  object Ref extends StringColumn with PartitionKey {
    override lazy val name = "ref"
  }

  object Name extends StringColumn {
    override lazy val name = "name"
  }

  object Description extends StringColumn {
    override lazy val name = "description"
  }

  object Quantity extends IntColumn {
    override lazy val name = "quantity"
  }

  object Gender extends StringColumn {
    override lazy val name = "gender"
  }

  object Category extends StringColumn {
    override lazy val name = "category"
  }

  object ModifiedDate extends Col[ZonedDateTime] {
    override lazy val name = "modifiedDate"
  }

  object Brand extends StringColumn {
    override lazy val name = "brand"
  }

  object Price extends DoubleColumn {
    override lazy val name = "price"
  }
}

abstract class ProductDAO extends ProductTable with RootConnector {

  def getProducts: Future[List[ProductEntity]] =
    select.fetch

  def getProductByRef(ref: String): Future[Option[ProductEntity]] =
    select
      .where(_.Ref eqs ref)
      .consistencyLevel_=(ConsistencyLevel.ONE)
      .one()

  def saveOrUpdate(product: ProductEntity): Future[ResultSet] =
    insert
      .value(_.Ref, product.ref)
      .value(_.Name, product.name)
      .value(_.Description, product.description)
      .value(_.Quantity, product.quantity)
      .value(_.Gender, product.gender)
      .value(_.Category, product.category)
      .value(_.ModifiedDate, product.modifiedDate)
      .value(_.Brand, product.brand)
      .value(_.Price, product.price)
      .consistencyLevel_=(ConsistencyLevel.ONE)
      .future()

  def deleteProduct(ref: String): Future[ResultSet] =
    delete.where(_.Ref eqs ref).future()

}
