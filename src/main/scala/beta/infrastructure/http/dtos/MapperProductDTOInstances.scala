package beta.infrastructure.http.dtos

import beta.domain.Product
import beta.infrastructure.mapper.{Mapper, MapperSugar}

class MapperProductDTOInstances extends MapperSugar {

  implicit def ProductToProductEntity: Mapper[Product, ProductDTO] =
    new Mapper[Product, ProductDTO] {
      override def to(product: Product): ProductDTO =
        ProductDTO(
          ref = product.ref,
          name = product.name,
          description = product.description,
          quantity = product.quantity,
          gender = product.gender.toString,
          category = product.category.toString,
          brand = product.brand,
          price = product.price
        )
    }

}
