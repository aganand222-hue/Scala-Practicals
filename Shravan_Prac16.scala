import breeze.linalg._
import breeze.plot._
import com.github.tototoshi.csv._
import java.io.File

object CombinedPlot {

  def main(args: Array[String]): Unit = {

    val reader = CSVReader.open(new File("TeenPhoneAddiction.csv"))
    val data = reader.allWithHeaders()
    reader.close()

    val sleepHours = data.take(100).flatMap { row =>
      try {
        Some(row("Sleep_Hours").toDouble)
      } catch {
        case _: Throwable => None
      }
    }

    val x = DenseVector((0 until sleepHours.length).map(_.toDouble).toArray)
    val y = DenseVector(sleepHours.toArray)

    val fig = Figure("Teen Phone Users - Sleep Hours")
    val plt = fig.subplot(0)

    plt += plot(x, y, name = "Sleep Hours Line", colorcode = "blue")

    plt += plot(x, y, '.', name = "Sleep Hours Points", colorcode = "red")

    plt.xlabel = "Student Index"
    plt.ylabel = "Sleep Hours"
    plt.title = "Sleep Hours of Teen Phone Users"

    fig.refresh()
  }
}