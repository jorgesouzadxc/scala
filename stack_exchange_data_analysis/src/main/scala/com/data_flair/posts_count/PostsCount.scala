package com.data_flair.posts_count

import org.apache.spark.sql.{ SparkSession, DataFrame }
import com.data_flair.xmlreader.XMLReader

object PostsCount extends XMLReader {

    def main(args: Array[String]): Unit = {

        if (args.length != 1) {
            println("Uso <inputFile>")
            sys exit 1
        }

        val inputFile = args(0)
        val name = "PostsCount"

        val spark = SparkSession
            .builder
            .config("spark.app.id", name)
            .getOrCreate

        val xmlDF: DataFrame = readXml(inputFile, spark, "row")

        println(s"Numero de entradas: ${xmlDF.count}")

    }

}