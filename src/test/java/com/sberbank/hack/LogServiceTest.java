package com.sberbank.hack;

import com.sberbank.hack.dto.Instruction;
import com.sberbank.hack.dto.Transaction;
import com.sberbank.hack.filewriter.LogService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.Instant;
import java.util.Arrays;
import java.util.Collections;

@SpringBootTest
public class LogServiceTest {
    @Autowired
    private LogService logService;

    @Test
    public void testInstructionLogging() {
        Instruction first = new Instruction("select sysdate from dual", "", Instant.now());
        Transaction tx1 = new Transaction(1, Collections.singletonList(first));
        logService.log(tx1);
        Instruction second = new Instruction("select some from dual", "", Instant.now());
        Instruction third = new Instruction("select data from dual", "", Instant.now());
        Transaction tx2 = new Transaction(2, Arrays.asList(second, third));
        logService.log(tx2);
    }

    public void logServiceLoadTest() {

    }
}
