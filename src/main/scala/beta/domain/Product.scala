package beta.domain

import java.time.ZonedDateTime

case class Product(
                 ref: String,
                 name: String,
                 description: String,
                 quantity: Int,
                 gender: Gender,
                 category: Category,
                 modifiedDate: ZonedDateTime,
                 brand: String, //TODO: Define entity for brand???
                 price: Double
                 )
