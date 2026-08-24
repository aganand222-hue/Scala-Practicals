from pyspark.sql import SparkSession
from pyspark.sql.functions import avg

spark = SparkSession.builder \
    .appName("GroupByAverage") \
    .master("local[*]") \
    .getOrCreate()

df = spark.read.csv(
    "students.csv",
    header=True,
    inferSchema=True
)

print("Original Data:")
df.show()

result = df.groupBy("Department").agg(
    avg("Marks").alias("Average_Marks")
)

print("Average Marks by Department:")
result.show()

spark.stop()
