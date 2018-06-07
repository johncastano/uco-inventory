package beta.infrastructure.http

import akka.http.scaladsl.server._

trait HttpService extends InventoryRoutes {

  val routes: Route = inventoryRoutes
}
