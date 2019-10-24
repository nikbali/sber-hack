package com.sberbank.hack.filewriter

import java.io.BufferedWriter
import java.io.File
import java.io.FileWriter
import java.nio.file.Files
import java.nio.file.Paths
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

object FileWrite {
    private val logFolderName = "." + File.separator + "target" + File.separator
    private const val logFileName = "file.log"
    private const val instructionFileName = "rpl.sql"
    private val dateFormatter = DateTimeFormatter.ofPattern("yy-MM-dd_HH-mm-ss")

    fun writeInstruction(instruction: String, txId: Long) {
        val instructionFileName = logFolderName + instructionFileName
        if (checkSizeLimit(instructionFileName)) rotateFile(instructionFileName)
        val os = FileWriter(File(instructionFileName), true)
        val writer = BufferedWriter(os)
        writer.append("$instruction; -- txId=$txId")
        writer.newLine()
        writer.close()
    }

    fun writeLog(logEntry: String) {
        val logFilename = logFolderName + logFileName
        if (checkSizeLimit(logFilename)) rotateFile(logFilename)
        val os = FileWriter(File(logFilename), true)
        val writer = BufferedWriter(os)
        writer.append(logEntry)
        writer.newLine()
        writer.close()
    }

    private fun rotateFile(filename: String): String {
        val start = filename.substring(0, filename.length - 4)
        val end = filename.substring(filename.length - 4)
        val newFileName = start + "_" + dateFormatter.format(LocalDateTime.now()) + end
        Files.move(Paths.get(filename), Paths.get(newFileName))
        return newFileName;
    }

    private fun checkSizeLimit(filename: String): Boolean = File(filename).length() / 1024 / 1024 > 10
}