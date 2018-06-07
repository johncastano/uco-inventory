package beta

import java.util.UUID

package object domain {

  def validateString(value: String,
                     field: String): Either[DomainError, String] = {
    if (value.isEmpty)
      Left(FieldIsEmpty(field))
    else
      Right(value)
  }

  def validateRef(ref: Option[String]): Either[DomainError, String] = {
    ref.fold[Either[DomainError, String]](Right(UUID.randomUUID.toString)) {
      r =>
        validateString(r, "Ref")
    }
  }

  def validatePrice(value: Double): Either[DomainError, Double] = {
    if (value <= 0)
      Left(InvalidPrice())
    else
      Right(value)
  }

  def validateGender(gender: String): Either[DomainError, Gender] =
    Gender(gender) match {
      case UnknownGender => Left(InvalidGender())
      case g @ _         => Right(g)
    }

  def validateCategory(category: String): Either[DomainError, Category] =
    Category(category) match {
      case UnknownCategory => Left(InvalidCategory())
      case c @ _           => Right(c)
    }
}
