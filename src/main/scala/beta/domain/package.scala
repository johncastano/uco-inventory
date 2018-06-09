package beta

import java.time.{ZoneOffset, ZonedDateTime}
import java.util.UUID

package object domain {

  type Demand = Int
  type OrderingCost = Double
  type MaintenanceCost = Double
  type AnnualWorkDays = Int
  type EOQ = Int
  type NumberOfOrders = Int
  type DayPerOrder = Int
  type ReorderPoint = Int

  def timeNow: ZonedDateTime = ZonedDateTime.now(ZoneOffset.UTC)

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

  def validateInt(value: Int, field: String): Either[DomainError, Int] = {
    if (value <= 0)
      Left(InvalidIntValue(field))
    else
      Right(value)
  }

  def validateDouble(value: Double,
                     field: String): Either[DomainError, Double] = {
    if (value <= 0)
      Left(InvalidDoubleValue(field))
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
