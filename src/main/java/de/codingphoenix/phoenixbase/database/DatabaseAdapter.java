package de.codingphoenix.phoenixbase.database;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import de.codingphoenix.phoenixbase.check.Checks;
import de.codingphoenix.phoenixbase.database.request.DatabaseRequest;
import de.codingphoenix.phoenixbase.exception.DriverNotLoadedException;
import de.codingphoenix.phoenixbase.exception.RequestNotExecutableException;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.concurrent.CompletableFuture;

public class DatabaseAdapter {
    private final DriverType driverType;
    private final String host;
    private final int port;
    private final String database;
    private final String user;
    private final String password;
    private final HikariConfig hikariConfig;
    private HikariDataSource dataSource;

    private DatabaseAdapter(DriverType driverType, String host, int port, String database, String user, String password) {
        this.driverType = driverType;
        this.host = host;
        this.port = port;
        this.database = database;
        this.user = user;
        this.password = password;
        this.hikariConfig = new HikariConfig();

        this.hikariConfig.setJdbcUrl("jdbc:" + driverType.name().toLowerCase() + "://" + this.host + ":" + this.port + "/" + this.database + "?autoReconnect=true");
        this.hikariConfig.setUsername(this.user);
        this.hikariConfig.setPassword(this.password);
        this.hikariConfig.addDataSourceProperty("cachePrepStmts", "true");
        this.hikariConfig.addDataSourceProperty("prepStmtCacheSize", "250");
        this.hikariConfig.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");
        this.hikariConfig.setMaxLifetime(540000);
    }


    public DatabaseAdapter connect() {
        System.out.println("Loading driver class: " + driverType.driver());
        try {
            Class.forName(driverType.driver());
        } catch (ClassNotFoundException e) {
            throw new DriverNotLoadedException(e.getCause());
        }
        if (Checks.DEBUG)
            System.out.println("Starting database connection with credentials: '" + this.user + "': '" + this.password + "'");
        this.dataSource = new HikariDataSource(this.hikariConfig);
        return this;
    }


    public void executeRequest(DatabaseRequest request) {
        if (request.async()) {
            CompletableFuture.runAsync(() -> {
                try {
                    if (Checks.DEBUG)
                        System.out.printf("Executing '%s' async%n", request.getClass().getSimpleName());
                    try (Connection connection = dataSource.getConnection()) {
                        request.execute(connection);
                    } catch (Exception e) {
                        throw new RequestNotExecutableException(e);
                    }
                    if (Checks.DEBUG)
                        System.out.printf("Executed '%s'%n", request.getClass().getSimpleName());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
            return;
        } else {
            if (Checks.DEBUG)
                System.out.printf("Executing '%s' synced%n", request.getClass().getSimpleName());
            try (Connection connection = dataSource.getConnection()) {
                request.execute(connection);
                return;
            } catch (SQLException e) {
                throw new RequestNotExecutableException(e);
            }
        }
    }

    @Getter
    @Setter
    @Accessors(fluent = true, chain = true)
    public static class Builder {
        private DriverType driverType;
        private String host;
        private int port = 3306;
        private String database;
        private String user;
        private String password;

        public DatabaseAdapter build() {
            Checks.checkIfNull(driverType, "drivertype");
            Checks.checkIfNull(host, "host");
            Checks.checkIfNull(database, "database");
            Checks.checkIfNull(user, "user");
            Checks.checkIfNull(password, "password");
            return new DatabaseAdapter(driverType, host, port, database, user, password);
        }
    }

    @Getter
    @Accessors(fluent = true)
    public enum DriverType {
        MYSQL("com.mysql.cj.jdbc.Driver"),
        MARIADB("org.mariadb.jdbc.Driver");

        private final String driver;

        DriverType(String driver) {
            this.driver = driver;
        }
    }
}
