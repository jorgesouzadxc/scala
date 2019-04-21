package com.data_flair.monthly_questions_count

import org.apache.spark.sql.{ SparkSession, DataFrame }
import org.apache.spark.sql.functions._
import com.data_flair.xmlreader.XMLReader

object MonthlyQuestionsCount extends XMLReader {

    def main(args: Array[String]): Unit = {

        if (args.length != 1) {
            println("Uso: <inputFile>")
            sys exit 1
        }

        val name = "Monthly Questions Count"
        val inputFile = args(0)

        val spark = SparkSession
            .builder
            .config("spark.app.id", name)
            .getOrCreate

        import spark.implicits._
        
        val xmlDF = readXml(inputFile, spark, "row")
        val resultDF = xmlDF
            .select(split('_CreationDate, "-").getItem(1).alias("_CreationDate"))
            .groupBy('_CreationDate)
            .count

        println("Resultado:")
        resultDF.show

    }

}