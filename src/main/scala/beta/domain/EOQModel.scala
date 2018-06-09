package beta.domain

import java.time.ZonedDateTime

import beta.infrastructure.http.dtos.ProductVariablesDTO

case class EOQModel(
    inputs: EOQInputs,
    q: EOQ,
    n: NumberOfOrders,
    l: DayPerOrder,
    r: ReorderPoint,
    name: String,
    quantity: Int,
    modifiedDate: ZonedDateTime
)

case class EOQInputs(
    ref: String,
    d: Demand,
    s: OrderingCost,
    h: MaintenanceCost,
    workDays: AnnualWorkDays
)

object EOQInputs {
  def validate(ref: String,
               input: ProductVariablesDTO): Either[DomainError, EOQInputs] = {
    for {
      demand <- validateInt(input.annualDemand, "Demand")
      orderingCost <- validateDouble(input.orderingCost, "Ordering Cost")
      maintenanceCost <- validateDouble(input.maintenanceCost,
                                        "Maintenance Cost")
      workDays <- validateInt(input.workDays, "Workdays")
    } yield new EOQInputs(ref, demand, orderingCost, maintenanceCost, workDays)
  }
}
