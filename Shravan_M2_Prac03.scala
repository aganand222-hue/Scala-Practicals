import com.github.tototoshi.csv._
import java.io.File

object FrequencyDistribution {

  def main(args: Array[String]): Unit = {

    val reader = CSVReader.open(new File("fastfoodordering.csv"))
    val data = reader.allWithHeaders()
    reader.close()

    val values = data.map(_("items_count").toInt)

    val frequency = values.groupBy(identity).view.mapValues(_.size).toMap

    println("\nFrequency Distribution and Cumulative Frequency")
    println("=" * 60)

    printf("%-10s %-15s %-25s%n",
      "Items", "Frequency", "Cumulative Frequency")

    println("-" * 60)

    var cumulative = 0

    frequency.toSeq.sortBy(_._1).foreach {
      case (value, freq) =>
        cumulative += freq

        printf(
          "%-10d %-15d %-25d%n",
          value,
          freq,
          cumulative
        )
    }

    println("=" * 60)
  }
}