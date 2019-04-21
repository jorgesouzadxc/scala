package com.data_flair.performance

trait ExecutionTime {

    var startTime: Long = 0
    var endTime: Long = 0
    var totalTime: Long = 0
    var isRecording = false
    var isFinished = false

    def startTracking: Long = {

        if (isFinished)
            println("A gravacao ja tinha sido concluida e sera sobrescrita")

        if (isRecording) {
            println("A gravacao ja tinha sido iniciada e nao sera sobrescrita.")
            return 1
        }
        
        startTime = System.currentTimeMillis / 1000
        isRecording = true

        println("Inicio da contagem de tempo do programa")
        startTime

    }

    def endTracking: Long = {

        endTime = System.currentTimeMillis / 1000
        isRecording = false
        println("Fim da contagem de tempo do programa")
        
        isFinished = true
        isRecording = false

        endTime

    }

    def getTotalTime(): Long = {
        
        if (isFinished && !isRecording) {
            totalTime = (endTime - startTime)
            println("Gravacao de tempo concluida")
            totalTime
        }
        else {
            println("A gravacao ainda esta em andamento")
            return 1
        }
        
    }

}