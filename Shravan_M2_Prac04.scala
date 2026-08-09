import com.github.tototoshi.csv._
import java.io.File

object SortTop5Rows {

  def main(args: Array[String]): Unit = {

    val reader = CSVReader.open(new File("fastfoodordering.csv"))
    val data = reader.allWithHeaders()
    reader.close()

    val sortedData = data.sortBy(row => -row("order_value").toDouble)

    val top5 = sortedData.take(5)

    println("\nTop 5 Orders by Order Value")
    println("=" * 90)

    printf("%-12s %-15s %-20s %-15s%n",
      "Order ID", "City", "Cuisine", "Order Value")

    println("-" * 90)

    top5.foreach { row =>
      printf(
        "%-12s %-15s %-20s %-15s%n",
        row("order_id"),
        row("city"),
        row("cuisine_type"),
        row("order_value")
      )
    }

    println("=" * 90)
  }
}