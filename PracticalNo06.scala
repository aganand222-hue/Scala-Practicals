import breeze.linalg._

object PracticalNo06 extends App {

  val matrix = DenseMatrix(
    (1.0, 2.0, 3.0, 4.0),
    (5.0, 6.0, 7.0, 8.0),
    (9.0, 10.0, 11.0, 12.0),
    (13.0, 14.0, 15.0, 16.0)
  )

  val subMatrix = matrix(1 to 2, 1 to 3)

  println("Sub-matrix:")
  println(subMatrix)

  val rowSums = sum(subMatrix(::, *))
  println("Row sums: " + rowSums)

  val columnSums = sum(subMatrix(*, ::))
  println("Column sums: " + columnSums)
}