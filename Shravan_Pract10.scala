import com.github.tototoshi.csv._
import java.io.File

object Shravan_Pract10{
  def main(args: Array[String]): Unit = {

    val reader = CSVReader.open(new File("studentcleaned_scores.csv"))
    val data = reader.allWithHeaders()
    reader.close()

    val threshold = 75

    // Filter rows where "math_score" > 98
    val filteredRows = data.filter { row =>
      row.get("math_score").exists(value => value.toDoubleOption.exists(_ > threshold))
    }

    println(s"\nTotal Rows with math_score > $threshold: ${filteredRows.length}\n")

    // Print each filtered row
    filteredRows.foreach { row =>
      println(row.values.mkString(", "))
    }
  }
}