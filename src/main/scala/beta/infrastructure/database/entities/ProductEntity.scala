package beta.infrastructure.database.entities

import java.time.ZonedDateTime

case class ProductEntity(
    ref: String,
    name: String,
    description: String,
    quantity: Int,
    gender: String,
    category: String,
    modifiedDate: ZonedDateTime,
    brand: String,
    price: Double
)
