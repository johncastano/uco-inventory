package beta.domain.services

import beta.domain._
import beta.infrastructure.database.entities.EOQEntity
import beta.infrastructure.database.repository.Repository
import beta.infrastructure.http.dtos.ProductVariablesDTO
import cats.data.{EitherT, OptionT}
import cats.implicits._

import scala.concurrent.{ExecutionContext, Future}

trait EOQService {

  implicit val executionContext: ExecutionContext
  val repository: Repository

  def addEOQ(
      ref: String,
      input: ProductVariablesDTO): Future[Either[DomainError, EOQEntity]] = {
    (for {
      rf <- EitherT.fromEither[Future](validateString(ref, "Ref"))
      _ <- OptionT(repository.getEOQByRef(rf))
        .map(_ => EOQAlreadyExists())
        .toRight(())
        .swap
      p <- EitherT(
        repository.getProductByRef(rf).map(_.toRight(ItemNotFound())))
      v <- EitherT.fromEither[Future](EOQInputs.validate(ref, input))
      model <- OptionT
        .pure[Future](calculateEOQVariables(v, p.name, p.quantity))
        .toRight(Miscalculation())
      sm <- EitherT(
        repository
          .saveOrUpdateEOQ(model)
          .map(Some(_).toRight[DomainError](UnexpectedError())))
    } yield sm).value
  }

  def updateEOQModel(
      ref: String,
      input: ProductVariablesDTO): Future[Either[DomainError, EOQEntity]] =
    (for {
      rf <- EitherT.fromEither[Future](validateString(ref, "Ref"))
      p <- EitherT(
        repository.getProductByRef(rf).map(_.toRight(ItemNotFound())))
      v <- EitherT.fromEither[Future](EOQInputs.validate(ref, input))
      model <- OptionT
        .pure[Future](calculateEOQVariables(v, p.name, p.quantity))
        .toRight(Miscalculation())
      sm <- EitherT(
        repository
          .saveOrUpdateEOQ(model)
          .map(Some(_).toRight[DomainError](UnexpectedError())))
    } yield sm).value

  def getEoQByRef(ref: String): Future[Option[EOQEntity]] =
    repository.getEOQByRef(ref)

  def calculateEOQVariables(input: EOQInputs,
                            name: String,
                            quantity: Int): EOQModel = {
    val q = Q(input.d, input.s, input.h)
    val n = N(input.d)(q)
    val l = L(input.workDays, n)
    val r = R(input.d, input.workDays, l)
    EOQModel(input, q, n, l, r, name, quantity, timeNow)
  }

  private[this] def Q =
    (demand: Demand,
     orderingCost: OrderingCost,
     maintenanceCost: MaintenanceCost) =>
      math.sqrt((2 * demand * orderingCost) / maintenanceCost).round.toInt

  private[this] def N = (demand: Demand) => (q: EOQ) => demand / q

  private[this] def L =
    (workDays: AnnualWorkDays, n: NumberOfOrders) => workDays / n

  private[this] def R =
    (demand: Demand, workDays: AnnualWorkDays, l: DayPerOrder) =>
      demand / workDays * l

}
