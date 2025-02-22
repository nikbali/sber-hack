package com.sberbank.hack.controllers;

import com.sberbank.hack.dao.models.Operation;
import com.sberbank.hack.dto.DbInfo;
import com.sberbank.hack.dto.ErrorInfo;
import com.sberbank.hack.dto.Instruction;
import com.sberbank.hack.dto.Transaction;
import com.sberbank.hack.filewriter.LogService;
import com.sberbank.hack.scheduler.ProduceExecuteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.sql.DataSource;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
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

    @GetMapping("/getLastOperations")
    public Collection<Operation> getLastTxs() {
        return logService.getLast();
    }

    @GetMapping("/start")
    public ResponseEntity start() {

        produceExecuteService.enable();
        produceExecuteService.execute();
        return ResponseEntity.ok().build();
    }

    @GetMapping("/end")
    public ResponseEntity end() {

        produceExecuteService.disable();
        return ResponseEntity.ok().build();
    }

    @RequestMapping("/download")
    public ResponseEntity<InputStreamResource> downloadFile() throws IOException {
         String logFolderName = "." + File.separator + "target" + File.separator;
         String logFileName = "file.log";

        MediaType mediaType = MediaType.APPLICATION_XML;

        File file = new File(logFolderName + logFileName);
        InputStreamResource resource = new InputStreamResource(new FileInputStream(file));

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=" + file.getName())
                .contentType(mediaType)
                .contentLength(file.length())
                .body(resource);
    }

}
