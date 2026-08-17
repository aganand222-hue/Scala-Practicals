import os

python_path = r"D:\Shravan S117\PYTHON\cign vg\.venv\Scripts\python.exe"

os.environ["PYSPARK_PYTHON"] = python_path
os.environ["PYSPARK_DRIVER_PYTHON"] = python_path

from pyspark.sql import SparkSession

spark = SparkSession.builder \
    .appName("FilterRows") \
    .master("local[*]") \
    .getOrCreate()

df = spark.read.csv(
    "students.csv",
    header=True,
    inferSchema=True
)

print("Original Data:")
df.show()

threshold = 75

filtered_df = df.filter(df["Marks"] > threshold)

print("Students with Marks greater than", threshold)
filtered_df.show()

spark.stop()
