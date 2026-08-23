import breeze.linalg._
import breeze.numerics.sigmoid
import scala.io.Source
import scala.collection.mutable.ArrayBuffer

import org.jfree.chart.ChartFactory
import org.jfree.chart.ChartFrame
import org.jfree.data.xy.XYSeries
import org.jfree.data.xy.XYSeriesCollection

object LogisticRegressionExample {

  def readCSV(file: String): ArrayBuffer[String] = {

    val records = ArrayBuffer[String]()
    val lines = Source.fromFile(file).getLines()

    var current = ""
    var quotes = 0

    for (line <- lines) {
      current += line + "\n"
      quotes += line.count(_ == '"')

      if (quotes % 2 == 0) {
        records += current.trim
        current = ""
        quotes = 0
      }
    }

    records
  }

  // Split CSV
  def splitCSV(line: String): Array[String] = {

    val result = ArrayBuffer[String]()
    val field = new StringBuilder
    var insideQuotes = false

    for (c <- line) {

      if (c == '"') {
        insideQuotes = !insideQuotes

      } else if (c == ',' && !insideQuotes) {

        result += field.toString
        field.clear()

      } else {

        field.append(c)
      }
    }

    result += field.toString
    result.toArray
  }

  def main(args: Array[String]): Unit = {

    val records = readCSV("AnimeWorld.csv").drop(1)

    val data = records.flatMap { record =>

      val x = splitCSV(record)

      try {

        val genre = x(1)
        val year = x(4).takeRight(4).toDouble
        val rating = x(5).toDouble

        // Action = 1, Non-Action = 0
        val label =
          if (genre.contains("Action")) 1.0 else 0.0

        Some((year, rating, label))

      } catch {
        case _: Exception => None
      }
    }

    println("Dataset loaded: " + data.size)

    var weights =
      DenseVector(0.0, 0.0, 0.0)

    val learningRate = 0.01
    val iterations = 1000

    for (_ <- 0 until iterations) {

      var gradient =
        DenseVector(0.0, 0.0, 0.0)

      for ((year, rating, label) <- data) {

        val scaledYear =
          (year - 2000.0) / 20.0

        val features =
          DenseVector(
            1.0,
            scaledYear,
            rating
          )

        val probability =
          sigmoid(weights.dot(features))

        val error =
          probability - label

        gradient += features * error
      }

      weights -=
        gradient * (learningRate / data.size)
    }

    val newPoint =
      DenseVector(
        1.0,
        (2020.0 - 2000.0) / 20.0,
        8.5
      )

    val probability =
      sigmoid(weights.dot(newPoint))

    val predictedClass =
      if (probability >= 0.5) 1 else 0

    println("\nTrained Weights:")
    println("Intercept = " + weights(0))
    println("Year      = " + weights(1))
    println("Rating    = " + weights(2))

    println("\nPrediction:")
    println("Probability = " + probability)
    println("Predicted Class = " + predictedClass)

    val nonAction =
      new XYSeries("Non-Action")

    val action =
      new XYSeries("Action")

    for ((year, rating, label) <- data) {

      if (label == 0)
        nonAction.add(year, rating)
      else
        action.add(year, rating)
    }

    val dataset =
      new XYSeriesCollection()

    dataset.addSeries(nonAction)
    dataset.addSeries(action)

    val chart =
      ChartFactory.createScatterPlot(
        "Logistic Regression",
        "Year",
        "Rating",
        dataset
      )

    val frame =
      new ChartFrame(
        "Logistic Regression",
        chart
      )

    frame.pack()
    frame.setVisible(true)
  }
}