package beta.infrastructure.database.entities

import beta.infrastructure.mapper.{Mapper, MapperSugar}
import beta.domain._

trait MapperProductEntityInstances extends MapperSugar {

  implicit def productToProductEntity: Mapper[Product, ProductEntity] =
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

  implicit def eoqModelToEOQEntity: Mapper[EOQModel, EOQEntity] =
    new Mapper[EOQModel, EOQEntity] {
      override def to(value: EOQModel): EOQEntity =
        EOQEntity(
          ref = value.inputs.ref,
          name = value.name,
          annualDemand = value.inputs.d,
          orderingCost = value.inputs.s,
          maintenanceCost = value.inputs.h,
          annualWorkDays = value.inputs.workDays,
          eoq = value.q,
          numberOfOrders = value.n,
          dayPerOrder = value.l,
          reorderPoint = value.r,
          quantity = value.quantity,
          modifiedDate = value.modifiedDate
        )
    }

  implicit def productEntityToProduct: Mapper[ProductEntity, Product] =
    new Mapper[ProductEntity, Product] {
      override def to(entity: ProductEntity): Product =
        Product(
          ref = entity.ref,
          name = entity.name,
          description = entity.description,
          quantity = entity.quantity,
          gender = Gender(entity.gender),
          category = Category(entity.category),
          modifiedDate = entity.modifiedDate,
          brand = entity.brand,
          price = entity.price
        )
    }

  implicit def eoqEntityToEoqModel: Mapper[EOQEntity, EOQModel] =
    new Mapper[EOQEntity, EOQModel] {
      override def to(entity: EOQEntity): EOQModel =
        EOQModel(
          inputs = EOQInputs(
            ref = entity.ref,
            d = entity.annualDemand,
            s = entity.orderingCost,
            h = entity.maintenanceCost,
            workDays = entity.annualWorkDays
          ),
          q = entity.eoq,
          n = entity.numberOfOrders,
          l = entity.dayPerOrder,
          r = entity.reorderPoint,
          name = entity.name,
          quantity = entity.quantity,
          modifiedDate = entity.modifiedDate
        )
    }
}
