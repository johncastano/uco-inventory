package beta.domain

import java.time.{ZoneOffset, ZonedDateTime}

import beta.infrastructure.http.dtos.{ProductDTO, UpdateProductDTO}

case class Product(
    ref: String,
    name: String,
    description: String,
    quantity: Int,
    gender: Gender,
    category: Category,
    modifiedDate: ZonedDateTime,
    brand: String,
    price: Double
)

object Product {

  def validate(dto: ProductDTO): Either[DomainError, Product] = {
    for {
      ref <- validateRef(dto.ref)
      name <- validateString(dto.name, "Name")
      description <- validateString(dto.description, "Description")
      gender <- validateGender(dto.gender)
      category <- validateCategory(dto.category)
      brand <- validateString(dto.brand, "Brand")
      price <- validateDouble(dto.price, "Price")
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

  def validate(ref: String,
               dto: UpdateProductDTO): Either[DomainError, Product] = {
    for {
      name <- validateString(dto.name, "Name")
      description <- validateString(dto.description, "Description")
      gender <- validateGender(dto.gender)
      category <- validateCategory(dto.category)
      brand <- validateString(dto.brand, "Brand")
      price <- validateDouble(dto.price, "Price")
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
