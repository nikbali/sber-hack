package com.sberbank.hack.controllers;

import com.sberbank.hack.dto.DbInfo;
import com.sberbank.hack.dto.ErrorInfo;
import com.sberbank.hack.dto.Instruction;
import com.sberbank.hack.dto.Transaction;
import com.sberbank.hack.filewriter.LogService;
import com.sberbank.hack.scheduler.ProduceExecuteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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

    private final LogService logService;
    private final ProduceExecuteService produceExecuteService;
    private final DataSource ds;

    @Autowired
    public DatabaseScannerController(LogService logService, ProduceExecuteService produceExecuteService, DataSource ds) {
        this.logService = logService;
        this.produceExecuteService = produceExecuteService;
        this.ds = ds;
    }

    @GetMapping("/getDatabaseInfo")
    public ResponseEntity getDatabaseInfo() {

        try {

            DatabaseMetaData metaData = ds.getConnection().getMetaData();
            return ResponseEntity.ok(
                    new DbInfo(metaData.getURL(), metaData.getUserName(), true)
            );

        } catch (SQLException ex) {
            return ResponseEntity
                    .status(HttpStatus.I_AM_A_TEAPOT)
                    .body(new ErrorInfo(ex.getMessage()));
        }
    }

    @GetMapping("/getLastTransactions")
    public Collection<Transaction> getLastTxs() {
        return logService.getLast();
    }

    @GetMapping("/emulate")
    public void emulate() {
        logService.log(
                new Transaction(new Random().nextLong(), Arrays.asList(
                new Instruction("123", "321", Instant.now()),
                new Instruction("1234", "4321", Instant.now()),
                new Instruction("12345", "54321", Instant.now())
        )));
    }

    @GetMapping("/start")
    public void start() {
        produceExecuteService.execute();
    }
}
