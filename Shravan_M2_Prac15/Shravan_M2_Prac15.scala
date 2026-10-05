object Shravan_M2_Prac15 {

  def main(args: Array[String]): Unit = {

    val salesData = List(10, 15, 20, 25, 30, 35, 40, 45, 50, 55)

    val degree = 3

    val polynomialFeatures = salesData.flatMap { value =>
      (1 to degree).map { power =>
        Math.pow(value, power).toInt
      }
    }

    println("Original Sales Dataset:")
    println(salesData)

    println("\nPolynomial Features up to Degree 3:")
    println(polynomialFeatures)

    println("\nDetailed Polynomial Features:")
    println("Sales\tSales²\tSales³")
    println("-------------------------")

    salesData.foreach { value =>
      val square = value * value
      val cube = value * value * value

      println(s"$value\t$square\t$cube")
    }
  }
}