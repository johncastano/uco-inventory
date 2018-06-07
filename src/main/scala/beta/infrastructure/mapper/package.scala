package beta.infrastructure

import beta.infrastructure.database.entities.MapperProductEntityInstances
import beta.infrastructure.http.dtos.MapperProductDTOInstances

package object mapper {
  object MapperProductEntity extends MapperProductEntityInstances
  object MapperProductDTO extends MapperProductDTOInstances
}
