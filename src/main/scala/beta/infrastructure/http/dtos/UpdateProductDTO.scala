package beta.infrastructure.http.dtos

case class UpdateProductDTO(
    name: String,
    description: String,
    quantity: Int,
    gender: String,
    category: String,
    brand: String,
    price: Double
)
