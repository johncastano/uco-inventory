package beta.infrastructure.database.entities

import beta.infrastructure.mapper.{MapperSugar, Mapper}
import beta.domain.Product

trait MapperProductEntityInstances extends MapperSugar {

  implicit def ProductToProductEntity: Mapper[Product, ProductEntity] =
    new Mapper[Product, ProductEntity] {
      override def to(product: Product): ProductEntity =
        ProductEntity(
          ref = product.ref,
          name = product.name,
          description = product.description,
          quantity = product.quantity,
          gender = product.gender.toString,
          category = product.category.toString,
          modifiedDate = product.modifiedDate,
          brand = product.brand,
          price = product.price
        )
    }

}
