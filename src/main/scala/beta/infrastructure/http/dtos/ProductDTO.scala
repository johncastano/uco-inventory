package beta.infrastructure.http.dtos

case class ProductDTO(
    ref: String,
    name: String,
    description: String,
    quantity: Int,
    gender: String,
    category: String,
    brand: String,
    price: Double
)
