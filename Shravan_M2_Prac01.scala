import scala.io.Source

object PearsonCorrelation extends App {

  val file = Source.fromFile("insurance.csv")

  val data = file.getLines().drop(1).flatMap { line =>
    val cols = line.split(",")

    for {
      bmi <- cols(2).trim.toDoubleOption
      charges <- cols(6).trim.toDoubleOption
    } yield (bmi, charges)

  }.toList

  file.close()

  val (x, y) = data.unzip

  val n = x.length.toDouble

  val meanX = x.sum / n
  val meanY = y.sum / n

  val numerator = x.zip(y).map {
    case (xi, yi) =>
      (xi - meanX) * (yi - meanY)
  }.sum

  val denominator = math.sqrt(
    x.map(xi => math.pow(xi - meanX, 2)).sum *
      y.map(yi => math.pow(yi - meanY, 2)).sum
  )

  val r =
    if (denominator == 0) 0.0
    else numerator / denominator

  val relationship =
    if (r >= 0.7) "Strong Positive"
    else if (r > 0) "Weak Positive"
    else if (r <= -0.7) "Strong Negative"
    else "Weak Negative"

  val df = n - 2

  val tStat =
    if (math.abs(r) == 1.0) Double.PositiveInfinity
    else r * math.sqrt(df / (1 - r * r))

  val isSignificant = math.abs(tStat) > 1.96

  println("------------ Pearson Correlation ------------")
  println(s"Dataset Size              : ${n.toInt}")
  println(f"Pearson Correlation (r)   : $r%.4f")
  println(s"Relationship              : $relationship")
  println(f"t-Statistic               : $tStat%.4f")
  println(s"Significant at 5% Level   : $isSignificant")
}