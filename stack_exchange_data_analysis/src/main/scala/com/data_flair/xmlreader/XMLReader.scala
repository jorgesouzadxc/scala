package com.data_flair.xmlreader

import org.apache.spark.sql.{ SparkSession, DataFrame }
import com.databricks.spark.xml._


trait XMLReader {

    def readXml(xmlFilePath: String, spark: SparkSession, rowTag: String): DataFrame = {

        spark
            .read
            .option("rowTag", rowTag)
            .xml(xmlFilePath)

    }

}