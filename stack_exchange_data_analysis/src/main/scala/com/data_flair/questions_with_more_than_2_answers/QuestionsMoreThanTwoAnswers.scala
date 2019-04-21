package com.data_flair.questions_with_more_than_2_answers

import org.apache.spark.sql.SparkSession
import com.data_flair.xmlreader.XMLReader

object QuestionsMoreThanTwoAnswers extends XMLReader {

    def main(args: Array[String]): Unit = {

        if (args.length != 1) {
            println("Uso <inputFile>")
            sys exit 1
        }

        val inputFile = args(0)
        val name = "Number Of Questions With More Than Two Answers"

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
            .where('_ViewCount gt 2)

        resultDF.show

        println(s"Resultado ${resultDF.count}")

    }

}