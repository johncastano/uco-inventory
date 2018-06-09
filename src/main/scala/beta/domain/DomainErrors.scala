package beta.domain

import akka.io.Tcp.Message

final case class ItemNotFound(code: Int = 100001,
                              message: String = "Product not found")
    extends DomainError

final case class InvalidCategory(
    code: Int = 100002,
    message: String = "Product category is invalid")
    extends DomainError

final case class InvalidGender(code: Int = 100004,
                               message: String = "No human gender")
    extends DomainError

final case class EOQIsNecessary(
    code: Int = 30004,
    message: String =
      "It's necessary to calculate the product EOQ before doing the operation")
    extends DomainError

final case class ErrorCalculatingReorder(
    code: Int = 300003,
    message: String = "Error while calculating reorder point"
) extends DomainError

final case class InvalidDoubleValue(code: Int = 10005, message: String)
    extends DomainError

final case class InvalidIntValue(code: Int = 10009, message: String)
    extends DomainError
final case class Miscalculation(
    code: Int = 10023,
    message: String = "Error calculating EOQ model based on EOQ inputs")
    extends DomainError
final case class ProductAlreadyExists(
    code: Int = 10006,
    message: String = "A Product with the same Ref already exists")
    extends DomainError

final case class EOQAlreadyExists(
    code: Int = 10006,
    message: String = "EOQ was already calculated for this product")
    extends DomainError

final case class UnexpectedError(code: Int = 10007,
                                 message: String = "Unexpected error")
    extends DomainError

final case class GenericError(code: Int, message: String) extends DomainError

final case class InsufficientProduct(
    code: Int = 20001,
    message: String = "Insufficient products to make the operation")
    extends DomainError

final case class FieldIsEmpty(code: Int = 100003, message: String)
    extends DomainError

object FieldIsEmpty {
  def apply(field: String): FieldIsEmpty =
    new FieldIsEmpty(message = s"$field is empty")
}

object InvalidDoubleValue {
  def apply(field: String): InvalidDoubleValue =
    new InvalidDoubleValue(message = s"$field cannot be less than 1")
}

object InvalidIntValue {
  def apply(field: String): InvalidDoubleValue =
    new InvalidDoubleValue(message = s"$field cannot be less than 0")
}

trait DomainError {
  val code: Int
  val message: String
}
