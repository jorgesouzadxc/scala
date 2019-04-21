package com.data_flair.question_word_count

import com.data_flair.xmlreader.XMLReader
import org.apache.spark.sql.SparkSession
import org.apache.spark.sql.types._

object QuestionWordCount extends XMLReader {

    def main(args: Array[String]): Unit = {

        if (args.length != 2) {
            println("Uso: <inputFile> <Word>")
            sys exit 1
        }

        val name = "Question Word Count"
        val inputFile = args(0)
        val word = args(1)
        
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
            .where('_Title.contains(word))
        
        resultDF.show
        println(s"Resultado: ${resultDF.count}")

    }

}