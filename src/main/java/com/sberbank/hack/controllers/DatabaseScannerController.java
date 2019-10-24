package com.sberbank.hack.controllers;

import com.sberbank.hack.dto.DbInfo;
import com.sberbank.hack.dto.Instruction;
import com.sberbank.hack.dto.Transaction;
import com.sberbank.hack.filewriter.LogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.sql.DataSource;
import java.sql.DatabaseMetaData;
import java.sql.SQLException;
import java.time.Instant;
import java.util.Arrays;
import java.util.Collection;
import java.util.Random;

@RestController
@RequestMapping("/")
public class DatabaseScannerController {

    @Autowired
    private LogService logService;
    @Autowired
    private DataSource ds;

    @GetMapping("/getDatabaseInfo")
    public ResponseEntity getDatabaseInfo() throws SQLException {
        DatabaseMetaData metaData = ds.getConnection().getMetaData();
        return ResponseEntity.ok(new DbInfo(metaData.getURL(), metaData.getUserName(), true));
    }
    @GetMapping("/getLastTransactions")
    public Collection<Transaction> getLastTxs() {
        return logService.getLast();
    }
    @GetMapping("/emulate")
    public void emulate() {
        logService.log(new Transaction(new Random().nextLong(), Arrays.asList(
                new Instruction("123", "321", Instant.now()),
                new Instruction("1234", "4321", Instant.now()),
                new Instruction("12345", "54321", Instant.now())
        )));
    }
}
