from pyspark.sql import SparkSession

from pyspark.sql.functions import col

from pyspark.ml import Pipeline

from pyspark.ml.feature import (
    StringIndexer,
    OneHotEncoder,
    VectorAssembler
)

from pyspark.ml.classification import LogisticRegression

from pyspark.ml.evaluation import MulticlassClassificationEvaluator

spark = SparkSession.builder \
    .appName("Customer Churn Classification") \
    .master("local[*]") \
    .getOrCreate()

print("\n==========================================")
print("       SPARK MLlib CLASSIFICATION")
print("==========================================")

file_path = "Churn_Modelling.csv"

df = spark.read.csv(
    file_path,
    header=True,
    inferSchema=True
)

print("\n========== FIRST 10 RECORDS ==========")

df.show(10)

print("\n========== DATASET SCHEMA ==========")

df.printSchema()

total_records = df.count()

print("\nTotal number of records:", total_records)

print("\n========== COLUMN NAMES ==========")

for column in df.columns:
    print(column)

data = df.select(
    "CreditScore",
    "Geography",
    "Gender",
    "Age",
    "Tenure",
    "Balance",
    "NumOfProducts",
    "HasCrCard",
    "IsActiveMember",
    "EstimatedSalary",
    "Exited"
)

data = data.dropna()

print("\nRecords after removing missing values:",
      data.count())

print("\n========== SELECTED DATA ==========")

data.show(10)

print("\n========== CUSTOMER CHURN DISTRIBUTION ==========")

data.groupBy("Exited") \
    .count() \
    .orderBy("Exited") \
    .show()

geography_indexer = StringIndexer(
    inputCol="Geography",
    outputCol="GeographyIndex",
    handleInvalid="keep"
)

gender_indexer = StringIndexer(
    inputCol="Gender",
    outputCol="GenderIndex",
    handleInvalid="keep"
)

encoder = OneHotEncoder(
    inputCols=[
        "GeographyIndex",
        "GenderIndex"
    ],
    outputCols=[
        "GeographyVector",
        "GenderVector"
    ]
)

assembler = VectorAssembler(
    inputCols=[
        "CreditScore",
        "GeographyVector",
        "GenderVector",
        "Age",
        "Tenure",
        "Balance",
        "NumOfProducts",
        "HasCrCard",
        "IsActiveMember",
        "EstimatedSalary"
    ],
    outputCol="features"
)

logistic_regression = LogisticRegression(
    featuresCol="features",
    labelCol="Exited",
    maxIter=100
)

pipeline = Pipeline(
    stages=[
        geography_indexer,
        gender_indexer,
        encoder,
        assembler,
        logistic_regression
    ]
)

train_data, test_data = data.randomSplit(
    [0.80, 0.20],
    seed=42
)


print("\n========== DATA SPLIT ==========")

print("Training records:", train_data.count())

print("Testing records :", test_data.count())

print("\n========== MODEL TRAINING ==========")

print("Training Logistic Regression model...")

model = pipeline.fit(train_data)

print("Model training completed successfully.")

predictions = model.transform(test_data)

print("\n========== MODEL PREDICTIONS ==========")

predictions.select(
    "CreditScore",
    "Geography",
    "Gender",
    "Age",
    "Balance",
    "Exited",
    "prediction"
).show(20)

print("\n========== PREDICTION PROBABILITY ==========")

predictions.select(
    "Exited",
    "prediction",
    "probability"
).show(10, truncate=False)

accuracy_evaluator = MulticlassClassificationEvaluator(
    labelCol="Exited",
    predictionCol="prediction",
    metricName="accuracy"
)

accuracy = accuracy_evaluator.evaluate(predictions)

precision_evaluator = MulticlassClassificationEvaluator(
    labelCol="Exited",
    predictionCol="prediction",
    metricName="weightedPrecision"
)

precision = precision_evaluator.evaluate(predictions)

recall_evaluator = MulticlassClassificationEvaluator(
    labelCol="Exited",
    predictionCol="prediction",
    metricName="weightedRecall"
)

recall = recall_evaluator.evaluate(predictions)

f1_evaluator = MulticlassClassificationEvaluator(
    labelCol="Exited",
    predictionCol="prediction",
    metricName="f1"
)

f1_score = f1_evaluator.evaluate(predictions)

print("\n==========================================")
print("          MODEL EVALUATION")
print("==========================================")

print("Accuracy  :", round(accuracy, 4))

print("Precision :", round(precision, 4))

print("Recall    :", round(recall, 4))

print("F1 Score  :", round(f1_score, 4))

print("\n========== RESULTS IN PERCENTAGE ==========")

print("Accuracy  :", round(accuracy * 100, 2), "%")

print("Precision :", round(precision * 100, 2), "%")

print("Recall    :", round(recall * 100, 2), "%")

print("F1 Score  :", round(f1_score * 100, 2), "%")

print("\n========== CONFUSION MATRIX ==========")

confusion_matrix = predictions.groupBy(
    "Exited",
    "prediction"
).count().orderBy(
    "Exited",
    "prediction"
)

confusion_matrix.show()

print("\n========== CLASSIFICATION SUMMARY ==========")

predictions.groupBy(
    "Exited",
    "prediction"
).count().orderBy(
    "Exited",
    "prediction"
).show()

print("\n==========================================")
print("             FINAL RESULT")
print("==========================================")

print("Dataset           : Churn_Modelling.csv")

print("Machine Learning  : Logistic Regression")

print("Framework         : Apache Spark MLlib")

print("Pipeline           :")

print("StringIndexer")
print("      ↓")
print("OneHotEncoder")
print("      ↓")
print("VectorAssembler")
print("      ↓")
print("Logistic Regression")

print("\nAccuracy  :", round(accuracy * 100, 2), "%")

print("Precision :", round(precision * 100, 2), "%")

print("Recall    :", round(recall * 100, 2), "%")

print("F1 Score  :", round(f1_score * 100, 2), "%")

spark.stop()

print("\n==========================================")
print("Program completed successfully.")
print("Spark session stopped.")
print("==========================================")