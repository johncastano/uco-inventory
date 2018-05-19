package beta.database

import com.outworkers.phantom.connectors.CassandraConnection
import com.outworkers.phantom.database.Database
import com.outworkers.phantom.dsl._

class InventoryDataBase(override val connector: CassandraConnection)
  extends Database[InventoryDataBase](connector){
  ???
}
