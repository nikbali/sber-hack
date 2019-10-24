package com.sberbank.hack.filewriter

import java.io.BufferedWriter
import java.io.File
import java.io.FileWriter

object FileWrite {
    val logFolderName = "C:\\Work\\Log\\"
    val logFileName = "file.log"
    val instructionFileName = "rpl.sql"

    fun writeInstruction(instruction: String) {

    }

    fun writeLog(logEntry: String) {
        val os = FileWriter(File(logFolderName + logFileName), true)
        val writer = BufferedWriter(os)
        writer.append(logEntry)
        writer.newLine()
        writer.close()
    }
}