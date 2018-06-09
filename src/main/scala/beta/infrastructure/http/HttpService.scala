package beta.infrastructure.http

import akka.http.scaladsl.server._

trait HttpService extends ProductRoutes {

  val routes: Route = pathPrefix("beta")(inventoryRoutes)
}
