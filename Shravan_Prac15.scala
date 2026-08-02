import breeze.linalg._
import breeze.plot._
import com.github.tototoshi.csv._
import java.io.File

object TeenPhoneLinePlot {

  def main(args: Array[String]): Unit = {

    val reader = CSVReader.open(new File("TeenPhoneAddiction.csv"))
    val data = reader.allWithHeaders()
    reader.close()

    val exerciseHours = data.take(100).flatMap { row =>
      try {
        Some(row("Exercise_Hours").toDouble)
      } catch {
        case _: Throwable => None
      }
    }

    val x = DenseVector((0 until exerciseHours.length).map(_.toDouble).toArray)

    val y = DenseVector(exerciseHours.toArray)

    val fig = Figure("Teen Exercise Hours Trend")
    val plt = fig.subplot(0)

    plt += plot(x, y, name = "Exercise Hours", colorcode = "blue")

    plt.xlabel = "Students"
    plt.ylabel = "Exercise Hours"
    plt.title = "Exercise Hours of Teen Phone Users"

    fig.refresh()
  }
}