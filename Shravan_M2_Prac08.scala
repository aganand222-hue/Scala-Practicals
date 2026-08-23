import breeze.linalg._
import scala.io.Source

object KMeansExample {

  def main(args: Array[String]): Unit = {

    val k = 2
    val maxIterations = 100

    val data = Source.fromFile("Pokemon.csv")
      .getLines()
      .drop(1)
      .map { line =>
        val x = line.split(",")

        DenseVector(
          x(5).toDouble,  
          x(6).toDouble,  
          x(7).toDouble,   
          x(10).toDouble   
        )
      }.toVector

    println(s"Dataset loaded: ${data.length} Pokemon")
    println("Features: HP, Attack, Defense, Speed")

    var centroids = Array(
      data(0).copy,
      data(data.length / 2).copy
    )

    println(s"\nInitial centroids:")
    println(centroids(0))
    println(centroids(1))

    var assignments =
      Array.fill(data.length)(0)

    var previousAssignments =
      Array.fill(data.length)(-1)

    var iteration = 0
    var converged = false

    while (iteration < maxIterations && !converged) {

      println(s"\n--- Iteration ${iteration + 1} ---")

      for (i <- data.indices) {

        val distance0 =
          euclideanDistance(data(i), centroids(0))

        val distance1 =
          euclideanDistance(data(i), centroids(1))

        assignments(i) =
          if (distance0 < distance1) 0 else 1
      }

      converged =
        assignments.sameElements(previousAssignments)

      previousAssignments =
        assignments.clone()

      for (c <- 0 until k) {

        val points =
          data.indices.filter(i =>
            assignments(i) == c
          )

        if (points.nonEmpty) {

          var sum =
            DenseVector.zeros[Double](4)

          for (i <- points) {
            sum += data(i)
          }

          centroids(c) =
            sum / points.length.toDouble
        }
      }

      println("Updated centroids:")
      println(centroids(0))
      println(centroids(1))

      iteration += 1
    }

    println("\n--- Final Results ---")

    println(
      s"K-Means algorithm converged in $iteration iterations."
    )

    println("\nFinal centroids:")

    println("Cluster 0: " + centroids(0))
    println("Cluster 1: " + centroids(1))

    println("\nCluster sizes:")

    println(
      "Cluster 0: " +
        assignments.count(_ == 0) +
        " Pokemon"
    )

    println(
      "Cluster 1: " +
        assignments.count(_ == 1) +
        " Pokemon"
    )
  }
}