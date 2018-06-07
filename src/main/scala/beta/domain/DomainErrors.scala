package beta.domain

final case class ItemNotFound(code: Int = 100001,
                              message: String = "Item not found")
    extends DomainError
final case class InvalidCategory(
    code: Int = 100002,
    message: String = "Product category is invalid")
    extends DomainError
final case class InvalidGender(code: Int = 100004,
                               message: String = "No human gender")
    extends DomainError
final case class InvalidPrice(code: Int = 10005,
                              message: String = "Price cannot be less than 0")
    extends DomainError
final case class ProductAlreadyExists(
    code: Int = 10006,
    message: String = "A Product with the same Ref already exists")
    extends DomainError
final case class UnexpectedError(code: Int = 10007,
                                 message: String = "Unexpected error")
    extends DomainError
final case class GenericError(code: Int, message: String) extends DomainError
final case class FieldIsEmpty(code: Int = 100003, message: String)
    extends DomainError
object FieldIsEmpty {
  def apply(field: String): FieldIsEmpty =
    new FieldIsEmpty(message = s"$field is empty")
}

trait DomainError {
  val code: Int
  val message: String
}
