package beta.infrastructure.http
import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.stream.ActorMaterializer
import beta.infrastructure.database.ProductionDatabase
import beta.infrastructure.database.repository.Repository
import com.typesafe.config.ConfigFactory

import scala.concurrent.{Await, ExecutionContextExecutor}
import scala.concurrent.duration._
import scala.util.{Failure, Success}

object Main extends App with HttpService {

  implicit val system: ActorSystem = ActorSystem("beta-http")
  implicit val executionContext: ExecutionContextExecutor = system.dispatcher
  implicit val materializer: ActorMaterializer = ActorMaterializer()

  private val config = ConfigFactory.load()
  private val host = config.getString("http.host")
  private val port = config.getString("http.port").toInt

  //implicit val db = database.create()
  implicit val dbProvider: ProductionDatabase = new ProductionDatabase {
    import com.outworkers.phantom.dsl._
    database.create(2.minutes)(executionContext)
  }
  override lazy val repository: Repository = new Repository()

  // http://localhost:8080/beta/product

  println(s"Starting http service ....")
  Http().bindAndHandle(routes, host, port) onComplete {
    case Success(Http.ServerBinding(address)) =>
      println(s"Http service listening on $address ...")
    case Failure(ex) =>
      println(s"There was an error starting http service", ex)
      Await.ready(system.terminate(), 5.seconds)

  }

}
