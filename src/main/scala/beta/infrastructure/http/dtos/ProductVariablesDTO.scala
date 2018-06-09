package beta.infrastructure.http.dtos

case class ProductVariablesDTO(
    annualDemand: Int,
    orderingCost: Double,
    maintenanceCost: Double,
    workDays: Int
)
