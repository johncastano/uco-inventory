package beta.domain

sealed trait Gender
case object Men extends Gender
case object Women extends Gender

sealed trait Category
case object Jacket extends Category
case object Shirt extends Category
case object Jean extends Category
case object Dress extends Category
