package beta.domain

import java.time.ZonedDateTime

case class Product(
                 ref: String,
                 name: String,
                 description: String,
                 quantity: Int,
                 category: String, //TODO: Create type for this
                 modifiedDate: ZonedDateTime,
                 price: Double
                 )
