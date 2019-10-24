package com.sberbank.hack;

import com.sberbank.hack.filewriter.FileWrite;
import org.junit.jupiter.api.Test;

public class FileWriteTest {
    @Test
    public void writeToLog() {
        FileWrite.INSTANCE.writeLog("{1}");
        FileWrite.INSTANCE.writeLog("{2}");
        FileWrite.INSTANCE.writeLog("{3}");
        FileWrite.INSTANCE.writeLog("{4}");
        FileWrite.INSTANCE.writeLog("{5}");
        FileWrite.INSTANCE.writeLog("{6}");
    }
}
