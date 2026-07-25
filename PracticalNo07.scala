import breeze.linalg._

object PracticalNo07 extends App {

  val matrix1 = DenseMatrix(
    (10.0, 20.0, 30.0),
    (40.0, 50.0, 60.0),
    (70.0, 80.0, 90.0)
  )

  val matrix2 = DenseMatrix(
    (2.0, 4.0, 5.0),
    (8.0, 10.0, 12.0),
    (14.0, 16.0, 18.0)
  )

  println("Matrix 1:")
  println(matrix1)

  println("\nMatrix 2:")
  println(matrix2)

  println("\nAddition:")
  println(matrix1 + matrix2)

  println("\nSubtraction:")
  println(matrix1 - matrix2)


  val multiplication = DenseMatrix.tabulate(matrix1.rows, matrix1.cols) {
    (i, j) => matrix1(i, j) * matrix2(i, j)
  }

  println("\nElement-wise Multiplication:")
  println(multiplication)


  val division = DenseMatrix.tabulate(matrix1.rows, matrix1.cols) {
    (i, j) => matrix1(i, j) / matrix2(i, j)
  }

  println("\nElement-wise Division:")
  println(division)
}