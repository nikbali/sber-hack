package com.sberbank.hack;

import dao.Select;
import dao.models.LogFile;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Collection;

import static java.lang.System.exit;

@SpringBootTest
public class SelectTest {

@Test
public void operationsTest() throws SQLException {
    final String url = String.format("jdbc:oracle:thin:@%s:%d/%s", "172.30.13.84", 1521, "orclcdb.localdomain");

    Connection connection = null;
    try {
        connection = DriverManager.getConnection(url, "sergonas", "password");
        Collection<LogFile> logFile = new Select().operations(connection);
System.out.println(logFile.size());
    } catch (SQLException e) {
        System.out.println("Connection Failed : " + e.getMessage());
        exit (-1);
    }
    if (connection != null) {
        System.out.println("You made it, take control your database now!");
    } else {
        System.out.println("Failed to make connection!");
    }
    connection.close();
}

}
