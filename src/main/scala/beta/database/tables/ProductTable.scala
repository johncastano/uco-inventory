package beta.database.tables

import beta.database.entities.ProductEntity
import com.outworkers.phantom.dsl._
import com.outworkers.phantom.Table
import com.outworkers.phantom.jdk8._
import com.outworkers.phantom.keys.PartitionKey

abstract class ProductTable extends Table[ProductTable,ProductEntity]{
  override def tableName: String = "product"

  object Ref extends StringColumn with PartitionKey{
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
    override lazy val name = "gender"
  }

  object Brand extends StringColumn {
    override lazy val name = "brand"
  }

  object Price extends DoubleColumn {
    override lazy val name = "price"
  }
}
