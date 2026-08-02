import com.github.tototoshi.csv._
import java.io.File

object OneHotEncoding {

  def main(args: Array[String]): Unit = {

    val reader = CSVReader.open(new File("TeenPhoneAddiction.csv"))
    val data = reader.allWithHeaders()
    reader.close()

    val categories = data.map(_("Phone_Usage_Purpose")).distinct.sorted

    val newData = data.map { row =>
      val purpose = row("Phone_Usage_Purpose")

      val oneHot = categories.map { category =>
        category -> (if (category == purpose) "1" else "0")
      }.toMap

      (row - "Phone_Usage_Purpose") ++ oneHot
    }

    val headers = newData.head.keys.toList

    println(headers.mkString(", "))

    newData.foreach { row =>
      println(headers.map(row).mkString(", "))
    }

    val writer = CSVWriter.open(new File("TeenPhoneAddiction_Encoded.csv"))
    writer.writeRow(headers)

    newData.foreach { row =>
      writer.writeRow(headers.map(row))
    }

    writer.close()

    println("One-hot encoded file written to TeenPhoneAddiction_Encoded.csv")
  }
}