package com.sberbank.hack;

import com.sberbank.hack.filewriter.FileWriter;
import org.junit.jupiter.api.Test;

public class FileWriterTest {
    @Test
    public void writeToLog() {
        FileWriter.INSTANCE.writeLog("{1}");
        FileWriter.INSTANCE.writeLog("{2}");
        FileWriter.INSTANCE.writeLog("{3}");
        FileWriter.INSTANCE.writeLog("{4}");
        FileWriter.INSTANCE.writeLog("{5}");
        FileWriter.INSTANCE.writeLog("{6}");
    }

    @Test
    public void test2() {
        System.out.println(FileWriter.INSTANCE.readCdn());
    }
}
