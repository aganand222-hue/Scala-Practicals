from pyspark.sql import SparkSession

spark = SparkSession.builder \
    .appName("JoinCSV") \
    .master("local[*]") \
    .getOrCreate()

students_df = spark.read.csv(
    "students.csv",
    header=True,
    inferSchema=True
)

departments_df = spark.read.csv(
    "departments.csv",
    header=True,
    inferSchema=True
)

print("Students Data:")
students_df.show()

print("Departments Data:")
departments_df.show()

result_df = students_df.join(
    departments_df,
    students_df["Department"] == departments_df["Department"],
    "inner"
).select(
    students_df["Name"],
    students_df["Department"],
    students_df["Marks"],
    departments_df["HOD"]
)

print("Joined Data:")
result_df.show()

result_df.write \
    .mode("overwrite") \
    .option("header", True) \
    .csv("joined_students")

spark.stop()
