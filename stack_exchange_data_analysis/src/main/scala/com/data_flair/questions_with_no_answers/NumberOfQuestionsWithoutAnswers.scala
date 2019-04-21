package com.data_flair.questions_without_answers

import org.apache.spark.sql.SparkSession
import com.data_flair.xmlreader.XMLReader

object NumberOfQuestionsWithoutAnswers extends XMLReader {

    def main(args: Array[String]): Unit = {

        if (args.length != 1) {
            println("Uso <inputFile>")
            sys exit 1
        }

        val inputFile = args(0)
        val name = "Number Of Questions Without Answers"

        val spark = SparkSession
            .builder
            .config("spark.app.id", name)
            .getOrCreate

        import spark.implicits._

        val xmlDF = readXml(inputFile, spark, "row")
        val resultDF = xmlDF
            .select(
                '_Id
                ,'_Title
                ,'_Body
            )
            .where('_ViewCount.isNull)

        resultDF.show

        println(s"Resultado ${resultDF.count}")

    }

}