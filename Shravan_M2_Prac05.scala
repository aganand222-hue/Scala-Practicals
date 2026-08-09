import breeze.linalg._
import breeze.plot._
import com.github.tototoshi.csv._
import java.io.File

object LinearRegressionGraph {

  def main(args: Array[String]): Unit = {

    val reader = CSVReader.open(new File("SalaryData.csv"))
    val data = reader.allWithHeaders()
    reader.close()

    val x = DenseVector(data.map(_("YearsExperience").toDouble).toArray)
    val y = DenseVector(data.map(_("Salary").toDouble).toArray)

    val meanX = sum(x) / x.length
    val meanY = sum(y) / y.length

    val numerator =
      (0 until x.length).map(i => (x(i) - meanX) * (y(i) - meanY)).sum

    val denominator =
      (0 until x.length).map(i => math.pow(x(i) - meanX, 2)).sum

    val slope = numerator / denominator
    val intercept = meanY - slope * meanX

    println("========== Linear Regression ==========")
    println(f"Intercept = $intercept%.2f")
    println(f"Slope     = $slope%.2f")

    val predictedY = DenseVector(
      x.toArray.map(value => intercept + slope * value)
    )

    val newExperience = 6.5
    val newSalary = intercept + slope * newExperience

    println()
    println(s"Years of Experience : $newExperience")
    println(f"Predicted Salary    : ₹$newSalary%.2f")

    val fig = Figure("Linear Regression")
    val plt = fig.subplot(0)

    plt += plot(x, y, '.', name = "Actual Data", colorcode = "red")

    plt += plot(x, predictedY, name = "Regression Line", colorcode = "blue")

    plt.title = "Salary Prediction using Linear Regression"
    plt.xlabel = "Years of Experience"
    plt.ylabel = "Salary"

    fig.refresh()
  }
}