package com.data_flair.top_ten_questions

import org.apache.spark.sql.SparkSession
import org.apache.spark.sql.functions.asc
import com.data_flair.xmlreader.XMLReader
import com.data_flair.performance.ExecutionTime

object TopTenQuestions extends XMLReader with ExecutionTime {

    def main(args: Array[String]): Unit = {

        if (args.length != 2) {
            println("Uso <inputFile> <Tag>")
            sys exit 1
        }

        startTracking

        val inputFile = args(0)
        val tag = args(1)
        val name = "Top Ten Questions"

        val spark = SparkSession
            .builder
            .config("spark.app.id", name)
            .getOrCreate

        import spark.implicits._

        val xmlDF = readXml(inputFile, spark, "row")
        val filteredDF = xmlDF
            .filter('_Tags contains tag)
        val resultDF = filteredDF
            .orderBy('_Score.desc)
            .limit(10)
            .select(
                '_Score
                ,'_Id
                ,'_Title
                ,'_Tags
            )

        resultDF.show

        endTracking
        println(s"Tempo total: ${getTotalTime}s")

    }

}