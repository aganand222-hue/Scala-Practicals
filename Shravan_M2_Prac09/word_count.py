import os

python_path = r"D:\Shravan S117\PYTHON\cign vg\.venv\Scripts\python.exe"

os.environ["PYSPARK_PYTHON"] = python_path
os.environ["PYSPARK_DRIVER_PYTHON"] = python_path

from pyspark.sql import SparkSession

spark = SparkSession.builder \
    .appName("WordCount") \
    .master("local[*]") \
    .getOrCreate()

text_file = spark.sparkContext.textFile("input.txt")

word_counts = (
    text_file
    .flatMap(lambda line: line.split())
    .map(lambda word: (word, 1))
    .reduceByKey(lambda a, b: a + b)
)

for word, count in word_counts.collect():
    print(word, ":", count)

spark.stop()
