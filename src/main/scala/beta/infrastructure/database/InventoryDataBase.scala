package beta.infrastructure.database

import beta.infrastructure.database.Connector._
import beta.infrastructure.database.tables.{EOQDAO, ProductDAO}
import com.outworkers.phantom.connectors.CassandraConnection
import com.outworkers.phantom.database.Database

class InventoryDataBase(override val connector: CassandraConnection)
    extends Database[InventoryDataBase](connector) {

  object productTable extends ProductDAO with connector.Connector
  object eoqTable extends EOQDAO with connector.Connector
}

object InvDatabase extends InventoryDataBase(connector)
object EmbeddedDb extends InventoryDataBase(testConnector)

trait DatabaseProvider {
  def database: InventoryDataBase
}

trait ProductionDatabase extends DatabaseProvider {
  override val database: InvDatabase.type = InvDatabase
}

trait EmbeddedDatabase extends DatabaseProvider {
  override val database: EmbeddedDb.type = EmbeddedDb
}
