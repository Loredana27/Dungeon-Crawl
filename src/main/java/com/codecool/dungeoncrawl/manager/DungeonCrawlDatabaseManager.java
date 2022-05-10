package com.codecool.dungeoncrawl.manager;

import org.postgresql.ds.PGSimpleDataSource;

import javax.sql.DataSource;
import java.sql.SQLException;

public class DungeonCrawlDatabaseManager {

    public DataSource connect() throws SQLException {
        PGSimpleDataSource dataSource = new PGSimpleDataSource();
        dataSource.setDatabaseName(System.getenv("PSQL_DATABASE"));
        dataSource.setUser(System.getenv("PSQL_USERNAME"));
        dataSource.setPassword(System.getenv("PSQL_PASSWORD"));

        System.out.println("Trying to connect...");
        dataSource.getConnection().close();
        System.out.println("Connection OK");

        return dataSource;
    }
}
