package beta.domain

sealed trait Gender
case object Men extends Gender
case object Women extends Gender
case object UnknownGender extends Gender

object Gender {
  def apply(value: String): Gender = value.toLowerCase match {
    case "men"   => Men
    case "women" => Women
    case _       => UnknownGender
  }
}

sealed trait Category
case object Jacket extends Category
case object Shirt extends Category
case object Jean extends Category
case object Dress extends Category
case object UnknownCategory extends Category

object Category {
  def apply(value: String): Category = value.toLowerCase match {
    case "jacket" => Jacket
    case "shirt"  => Shirt
    case "jean"   => Jean
    case "dress"  => Dress
    case _        => UnknownCategory
  }
}
