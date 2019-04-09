package com.carrefour.genparquet

import org.apache.spark.sql.{ SparkSession, DataFrame }

object ParquetGenerator {

    def main(args: Array[String]): Unit = {

        import java.io.File

        if (args.length != 4) {
            println("Parametros: <query_path> <parquet_output_path> <parquet_file_name>")
            sys.exit(1)
        }

        val queryPath = args(0)
        val outputPath = args(1)
        val fileName = args(2)

        if (!new File(queryPath).isFile || !new File(outputPath).isDirectory) {
            throw new IllegalArgumentException("Insira uma query e uma pasta de saída válida.")
        }

        val name = "genparquet"
        val spark = SparkSession
            .builder
            .appName(name)
            .config("spark.app.id", name)
            .getOrCreate

        val success = writeParquet(spark, queryPath, outputPath, fileName)
        spark.stop
        if (success) {
            sys.exit(0)
        }
        else {
            sys.exit(1)
        }

    }

    def writeParquet(spark: SparkSession, queryPath: String, outputPath: String, fileName: String): Boolean = {
        
        import scala.util.{ Try, Success, Failure }
        import scala.io.Source

        val outputFile = s"${outputPath}/${fileName}"
        var query = ""
        Try(Source.fromFile(queryPath).getLines) match {
            case Success(lines) => query = lines.mkString
            case Failure(stackTrace) => {
                println("Não conseguiu ler o script HQL")
                println(stackTrace)
            }
        }

        val resultDF = spark.sql(query)
        Try(resultDF.count) match {
            case Success(i) => {
                if (i <= 0) {
                    println("Não foram retornados registros para serem gravados")
                    return false
                }
                else {
                    Try(
                        resultDF
                            .coalesce(1)
                            .write
                            .mode("append")
                            .option("compression", "gzip")
                            .parquet(outputFile)
                    ) match {
                        case Success(_) => return true
                        case Failure(_) => return false
                    }
                }
            }
            case Failure(trace) => {
                println("Falha durante a execucao da query")
                return false
            }
        }

    }

}