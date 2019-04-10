package com.carrefour.genparquet

import org.apache.spark.sql.{ SparkSession, DataFrame }

object ParquetGen {

    def main(args: Array[String]): Unit = {

        import java.io.File

        if (args.length != 3) {
            println("Parametros: <query_path> <parquet_output_path> <parquet_file_name>")
            sys.exit(1)
        }

        val queryPath = args(0)
        val outputPath = args(1)
        val fileName = args(2)

        // if (!new File(queryPath).isFile || !new File(outputPath).isDirectory) {
        //     throw new IllegalArgumentException("Insira uma query e uma pasta de saída válida.")
        // }

        val name = "genparquet"
        val spark = SparkSession
            .builder
            .appName(name)
            .config("spark.history.kerberos.keytab","/etc/security/keytabs/spark.headless.keytab")
            .config("spark.driver.host","slvmlxbdedgq001.br.wcorp.carrefour.com")
            .config("spark.history.fs.logDirectory","hdfs:///spark2-history/")
            .config("spark.eventLog.enabled","true")
            .config("spark.driver.port","42621")
            .config("spark.driver.extraLibraryPath","/usr/hdp/current/hadoop-client/lib/native:/usr/hdp/current/hadoop-client/lib/native/Linux-amd64-64")
            .config("spark.yarn.queue","default")
            .config("spark.repl.class.uri","spark://slvmlxbdedgq001.br.wcorp.carrefour.com:42621/classes")
            .config("spark.yarn.historyServer.address","slvmlxbdmstq003.br.wcorp.carrefour.com:18081")
            .config("spark.repl.class.outputDir","/tmp/spark-96554611-0c81-4972-b6f8-462b6cb6b033/repl-2c8ae85f-53f8-4a95-9d04-45174ecab4ff")
            .config("spark.history.kerberos.principal","spark-artic@BR.WCORP.CARREFOUR.COM")
            .config("spark.ui.showConsoleProgress","true")
            .config("spark.executor.id","driver")
            .config("spark.history.provider","org.apache.spark.deploy.history.FsHistoryProvider")
            .config("spark.executor.extraLibraryPath","/usr/hdp/current/hadoop-client/lib/native:/usr/hdp/current/hadoop-client/lib/native/Linux-amd64-64")
            .config("spark.home","/usr/hdp/current/spark2-client")
            .config("spark.eventLog.dir","hdfs:///spark2-history/")
            .config("spark.sql.catalogImplementation","hive")
            .config("spark.history.ui.port","18081")
            .config("spark.history.kerberos.enabled","true")
            .config("spark.app.id","local-1554920011658")
            .config("spark.driver.extraClassPath","/usr/hdp/current/phoenix-client/*")
            .config("spark.executor.extraClassPath","/usr/hdp/current/phoenix-client/*")
            .enableHiveSupport
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
                return false
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
                            .option("header", "true")
                            .parquet(outputFile)
                    ) match {
                        case Success(_) => return true
                        case Failure(_) => return false
                    }
                }
            }
            case Failure(trace) => {
                println("Falha durante a execucao da query")
                println(trace)
                return false
            }
        }

    }

}