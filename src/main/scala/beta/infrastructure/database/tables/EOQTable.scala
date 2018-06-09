package beta.infrastructure.database.tables

import beta.infrastructure.database.entities.EOQEntity
import com.outworkers.phantom.dsl._
import com.outworkers.phantom.jdk8._

import scala.concurrent.Future

abstract class EOQTable extends Table[EOQTable, EOQEntity] {
  override def tableName: String = "EOQ_table"

  object Ref extends StringColumn with PartitionKey {
    override lazy val name = "ref"
  }

  object Name extends StringColumn {
    override lazy val name = "name"
  }

  object AnnualDemand extends IntColumn {
    override lazy val name = "annual_demand"
  }

  object OrderingCost extends DoubleColumn {
    override lazy val name = "ordering_cost"
  }

  object MaintenanceCost extends DoubleColumn {
    override lazy val name = "maintenance_cost"
  }

  object AnnualWorkDays extends IntColumn {
    override lazy val name = "annual_workdays"
  }

  object EOQ extends IntColumn {
    override lazy val name = "eoq"
  }

  object NumberOfOrders extends IntColumn {
    override lazy val name = "number_of_orders"
  }

  object DayPerOrder extends IntColumn {
    override lazy val name = "day_per_order"
  }

  object ReorderPoint extends IntColumn {
    override lazy val name = "reorder_point"
  }

  object Quantity extends IntColumn {
    override lazy val name = "quantity"
  }

  object ModifiedDate extends Col[ZonedDateTime] {
    override lazy val name = "modifiedDate"
  }

}

abstract class EOQDAO extends EOQTable with RootConnector {

  def getEOQs: Future[List[EOQEntity]] =
    select.fetch

  def getEOQByRef(ref: String): Future[Option[EOQEntity]] =
    select
      .where(_.Ref eqs ref)
      .consistencyLevel_=(ConsistencyLevel.ONE)
      .one()

  def saveOrUpdate(product: EOQEntity): Future[ResultSet] =
    insert
      .value(_.Ref, product.ref)
      .value(_.Name, product.name)
      .value(_.AnnualDemand, product.annualDemand)
      .value(_.OrderingCost, product.orderingCost)
      .value(_.MaintenanceCost, product.maintenanceCost)
      .value(_.AnnualWorkDays, product.annualWorkDays)
      .value(_.EOQ, product.eoq)
      .value(_.NumberOfOrders, product.numberOfOrders)
      .value(_.DayPerOrder, product.dayPerOrder)
      .value(_.ReorderPoint, product.reorderPoint)
      .value(_.Quantity, product.quantity)
      .value(_.ModifiedDate, product.modifiedDate)
      .consistencyLevel_=(ConsistencyLevel.ONE)
      .future()

  def deleteEOQ(ref: String): Future[ResultSet] =
    delete.where(_.Ref eqs ref).future()

}
