package beta.domain

import java.time.{ZoneOffset, ZonedDateTime}

import beta.infrastructure.http.dtos.ProductDTO

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

object Product {

  private[this] def timeNow: ZonedDateTime = ZonedDateTime.now(ZoneOffset.UTC)

  def validate(dto: ProductDTO): Either[DomainError, Product] = {
    for {
      ref <- validateString(dto.ref, "Ref")
      name <- validateString(dto.name, "Name")
      description <- validateString(dto.description, "Description")
      gender <- validateGender(dto.gender)
      category <- validateCategory(dto.category)
      brand <- validateString(dto.brand, "Brand")
      price <- validatePrice(dto.price)
    } yield
      new Product(
        ref = ref,
        name = name,
        description = description,
        quantity = dto.quantity,
        gender = gender,
        category = category,
        modifiedDate = timeNow,
        brand = brand,
        price = price
      )
  }
}
