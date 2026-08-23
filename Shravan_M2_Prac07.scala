import breeze.linalg.{DenseVector, euclideanDistance}
import scala.io.Source

object KnnExample {

  case class DataPoint(
                        name: String,
                        features: DenseVector[Double],
                        label: String
                      )

  def main(args: Array[String]): Unit = {

    val source = Source.fromFile("Pokemon.csv")

    val lines = source.getLines().drop(1).toSeq
    source.close()

    val dataset = lines.flatMap { line =>

      val data = line.split(",", -1)

      try {

        val name = data(1)
        val label = data(2)

        val hp = data(5).toDouble
        val attack = data(6).toDouble
        val defense = data(7).toDouble
        val speed = data(10).toDouble

        Some(
          DataPoint(
            name,
            DenseVector(hp, attack, defense, speed),
            label
          )
        )

      } catch {
        case _: Exception => None
      }
    }

    println("Pokemon dataset loaded successfully.")
    println("Total records: " + dataset.size)

    val newPointFeatures =
      DenseVector(70.0, 80.0, 70.0, 90.0)

    println("\nNew Pokemon vector:")
    println(newPointFeatures)

    var minDistance = Double.MaxValue
    var nearestPokemon = ""
    var predictedLabel = ""

    for (point <- dataset) {

      val dist =
        euclideanDistance(newPointFeatures, point.features)

      if (dist < minDistance) {
        minDistance = dist
        nearestPokemon = point.name
        predictedLabel = point.label
      }
    }

    println("\nClassification Result:")
    println("--------------------------------")
    println("Nearest Pokemon : " + nearestPokemon)
    println("Distance        : " + minDistance)
    println("Predicted Type  : " + predictedLabel)
    println("--------------------------------")
  }
}