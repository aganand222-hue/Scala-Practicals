import breeze.linalg._
import breeze.plot._
import com.github.tototoshi.csv._
import java.io.File

object TeenPhoneHistogram {

  def main(args: Array[String]): Unit = {

    val reader = CSVReader.open(new File("TeenPhoneAddiction.csv"))
    val data = reader.allWithHeaders()
    reader.close()

    val weekendUsage =
      DenseVector(data.map(_("Weekend_Usage_Hours").toDouble).toArray)

    val fig = Figure("Histogram of Weekend Usage Hours")

    val binSizes = List(5, 10, 20)

    for ((bins, idx) <- binSizes.zipWithIndex) {

      val plt = fig.subplot(1, binSizes.length, idx)

      plt += hist(weekendUsage, bins)

      plt.title = s"Histogram with $bins bins"
      plt.xlabel = "Weekend Usage Hours"
      plt.ylabel = "Frequency"
    }

    fig.refresh()
  }
}