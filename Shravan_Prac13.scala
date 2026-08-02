import breeze.linalg._
import breeze.plot._
import com.github.tototoshi.csv._
import java.io.File

object TeenPhoneScatterPlot {

  def main(args: Array[String]): Unit = {

    val reader = CSVReader.open(new File("TeenPhoneAddiction.csv"))
    val data = reader.allWithHeaders()
    reader.close()

    println(data.head.keys.mkString(", "))

    val male = data.filter(_("Gender") == "Male")
    val female = data.filter(_("Gender") == "Female")

    def extractXY(rows: List[Map[String, String]]) = {
      val x = DenseVector(rows.map(_("Daily_Usage_Hours").toDouble).toArray)
      val y = DenseVector(rows.map(_("Addiction_Level").toDouble).toArray)
      (x, y)
    }

    val (xMale, yMale) = extractXY(male)
    val (xFemale, yFemale) = extractXY(female)

    val fig = Figure()
    val plt = fig.subplot(0)

    plt.title = "Daily Usage Hours vs Addiction Level"
    plt.xlabel = "Daily Usage Hours"
    plt.ylabel = "Addiction Level"

    plt += plot(xMale, yMale, '.', name = "Male", colorcode = "blue")
    plt += plot(xFemale, yFemale, '.', name = "Female", colorcode = "red")

    fig.refresh()
  }
}