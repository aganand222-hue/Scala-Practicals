import scala.util.Random

object Shravan_M2_Prac14 {

  def main(args: Array[String]): Unit = {

    val days = 1 to 30

    val random = new Random(42)

    val salesData = days.map { day =>
      val sales = 5000 + (day * 100) + random.nextInt(1500)
      (day, sales)
    }

    println("Daily Sales Time Series")
    println("-----------------------")

    salesData.foreach {
      case (day, sales) =>
        println(f"Day $day%2d : ₹$sales%d")
    }

    val totalSales = salesData.map(_._2).sum

    val averageSales = totalSales.toDouble / salesData.length

    val minSales = salesData.minBy(_._2)

    val maxSales = salesData.maxBy(_._2)

    println("\nTime Series Analysis")
    println("--------------------")
    println(f"Total Sales   : ₹$totalSales%d")
    println(f"Average Sales : ₹$averageSales%.2f")
    println(f"Minimum Sales : Day ${minSales._1} - ₹${minSales._2}")
    println(f"Maximum Sales : Day ${maxSales._1} - ₹${maxSales._2}")

    // Determine trend
    val firstDaySales = salesData.head._2
    val lastDaySales = salesData.last._2

    if (lastDaySales > firstDaySales) {
      println("Overall Trend : Increasing")
    } else if (lastDaySales < firstDaySales) {
      println("Overall Trend : Decreasing")
    } else {
      println("Overall Trend : Stable")
    }
  }
}