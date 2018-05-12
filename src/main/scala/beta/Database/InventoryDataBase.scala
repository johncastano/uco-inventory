package beta.Database

import com.outworkers.phantom.connectors.CassandraConnection
import com.outworkers.phantom.database.Database

class InventoryDataBase(override val connector: CassandraConnection)
  extends Database[InventoryDataBase](connector){

}
