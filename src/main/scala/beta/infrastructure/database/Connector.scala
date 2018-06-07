package beta.infrastructure.database

import com.outworkers.phantom.connectors.{
  CassandraConnection,
  ContactPoint,
  ContactPoints
}
import com.typesafe.config.ConfigFactory

import scala.collection.JavaConverters._

object Connector {
  private val config = ConfigFactory.load()

  private val hosts = config.getString("cassandra.host")
  private val keyspace = config.getString("cassandra.keyspace")

  lazy val connector: CassandraConnection =
    ContactPoints(Seq(hosts)).keySpace(keyspace)

  lazy val testConnector: CassandraConnection =
    ContactPoint.embedded.noHeartbeat().keySpace("users_test")
}
