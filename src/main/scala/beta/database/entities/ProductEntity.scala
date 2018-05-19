package beta.database.entities

import java.time.ZonedDateTime

case class ProductEntity (
                           ref: String,
                           name: String,
                           description: String,
                           quantity: Int,
                           gender: String,
                           category: String,
                           modifiedDate: ZonedDateTime,
                           brand: String, //TODO: Define entity for brand???
                           price: Double
                         )
