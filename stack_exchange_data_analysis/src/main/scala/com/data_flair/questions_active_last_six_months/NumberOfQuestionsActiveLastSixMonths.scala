package com.data_flair.questions_active_last_six_months

import org.apache.spark.sql.SparkSession
import com.data_flair.xmlreader.XMLReader
import java.time.ZonedDateTime
import java.time.ZoneId

object NumberOfQuestionsActiveLastSixMonths extends XMLReader {

    def main(args: Array[String]): Unit = {

        if (args.length != 1) {
            println("Uso: <inputFile>")
            sys exit 1
        }

        val inputFile = args(0)
        val name = "Number Of Questions Active Last Six Months"
        val spark = SparkSession
            .builder
            .config("spark.app.id", name)
            .getOrCreate
        import spark.implicits._

        val xmlDf = readXml(inputFile, spark, "row")

        val format1 = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS")
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS");

        val current = ZonedDateTime now ZoneId of "America/New_York"
        val resultDf = xmlDf
            .filter({
                '_LastActivityDate.
            })

    }

}