import com.github.tototoshi.csv._
import java.io.File
import scala.util.Try

object Shravan_Pract09{
  def main(args: Array[String]): Unit = {

    val inputFile = new File("studentexam.csv")
    val reader = CSVReader.open(inputFile)
    val allRows = reader.allWithHeaders()
    reader.close()

    val numericColumns = Seq("math_score", "science_score", "attendance_pct")

    val stats: Map[String, (Double, Int)] = numericColumns.map { col =>
      val values = allRows.map(row => row.getOrElse(col, "").trim)
      val validNumbers = values.flatMap(v => Try(v.toDouble).toOption)
      val missingCount = values.count(v => Try(v.toDouble).isFailure)
      val mean = if (validNumbers.nonEmpty) validNumbers.sum / validNumbers.size
      else 0.0
      (col, (mean, missingCount))
    }.toMap


    println("\n--- Missing Data Report ---")
    stats.foreach { case (col, (mean, missingCount)) =>
      println(f"Column: $col, Missing values: $missingCount, Replaced with mean: $mean%.2f")
    }

    val cleanedRows = allRows.map { row =>
      numericColumns.foldLeft(row) { (accRow, col) =>
        val value = accRow.getOrElse(col, "").trim
        val replaced = Try(value.toDouble).toOption match {
          case Some(_) => value // valid number
          case None => f"${stats(col)._1}%.2f" // replace with mean
        }
        accRow.updated(col, replaced)
      }
    }

    //  Save cleaned CSV
    val outputFile = new File("studentcleaned_scores.csv")
    val writer = CSVWriter.open(outputFile)
    val headers = cleanedRows.head.keys.toSeq
    writer.writeRow(headers)
    cleanedRows.foreach(row => writer.writeRow(headers.map(row)))
    writer.close()

    println("\nMissing values replaced and saved to student_exam_scores_cleaned.csv")
  }
}