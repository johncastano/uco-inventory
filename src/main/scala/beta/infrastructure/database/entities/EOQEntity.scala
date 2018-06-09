package beta.infrastructure.database.entities

import com.outworkers.phantom.jdk8.ZonedDateTime

case class EOQEntity(
    ref: String,
    name: String,
    annualDemand: Int,
    orderingCost: Double,
    maintenanceCost: Double,
    annualWorkDays: Int,
    eoq: Int,
    numberOfOrders: Int,
    dayPerOrder: Int,
    reorderPoint: Int,
    quantity: Int,
    modifiedDate: ZonedDateTime
)
