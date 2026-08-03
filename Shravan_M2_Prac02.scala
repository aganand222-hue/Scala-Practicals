import scala.io.Source

object MovingAverage extends App {

  val file = Source.fromFile("Dailyminimumtemps.csv")

  val temperature = file.getLines().drop(1).flatMap { line =>
    val cols = line.split(",")
    cols(1).trim.toDoubleOption
  }.toVector

  file.close()

  val window = 5

  val sma =
    temperature.sliding(window)
      .map(_.sum / window)
      .toVector

  val weights = Vector(1,2,3,4,5)

  val totalWeight = weights.sum.toDouble

  val wma =
    temperature.sliding(window).map { values =>
      values.zip(weights)
        .map { case (v,w) => v*w }
        .sum / totalWeight
    }.toVector

  val alpha = 2.0/(window+1)

  val ema =
    temperature.tail.scanLeft(temperature.head){
      (previous,current) =>
        alpha*current + (1-alpha)*previous
    }

  println("First 10 SMA")

  sma.take(10).foreach(println)

  println("\nFirst 10 WMA")

  wma.take(10).foreach(println)

  println("\nFirst 10 EMA")

  ema.take(10).foreach(println)

}