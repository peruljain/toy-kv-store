package org.example;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.example.client.IKVStore;
import org.example.client.KVStore;
import org.example.storage.IStorage;
import org.example.storage.MySQLStorage;

import javax.sql.DataSource;
import java.util.List;
import java.util.Optional;

public class Main {
    public static void main(String[] args) {
        IStorage storageOne = new MySQLStorage(getDataSourceOne());
        IStorage storageTwo = new MySQLStorage(getDataSourceTwo());
        IStorage storageThree = new MySQLStorage(getDataSourceThree());

        IKVStore kvStore = new KVStore(List.of(storageOne, storageTwo, storageThree));

        for(int i = 0; i<100; i++) {
            String key = String.valueOf(i);
            String value = "perul-" + key;
            kvStore.put(key, value, 10);
        }

        for(int i = 0; i<100; i++) {
            if(i%2==0) {
                String key = String.valueOf(i);
                Optional<String> value = kvStore.get(key);
                System.out.println(value.get());
            }
        }


    }

    public static DataSource getDataSourceOne() {
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl("jdbc:mysql://localhost:3306/toy_kv_store?useSSL=false");
        config.setUsername("root");
        config.setPassword("password");
        config.addDataSourceProperty("cachePrepStmts", "true");
        config.addDataSourceProperty("prepStmtCacheSize", "250");
        config.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");
        config.setMaximumPoolSize(100);  // Set the maximum number of connections here
        return new HikariDataSource(config);
    }

    public static DataSource getDataSourceTwo() {
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl("jdbc:mysql://localhost:3307/toy_kv_store?useSSL=false");
        config.setUsername("root");
        config.setPassword("password");
        config.addDataSourceProperty("cachePrepStmts", "true");
        config.addDataSourceProperty("prepStmtCacheSize", "250");
        config.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");
        config.setMaximumPoolSize(100);  // Set the maximum number of connections here
        return new HikariDataSource(config);
    }

    public static DataSource getDataSourceThree() {
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl("jdbc:mysql://localhost:3308/toy_kv_store?useSSL=false");
        config.setUsername("root");
        config.setPassword("password");
        config.addDataSourceProperty("cachePrepStmts", "true");
        config.addDataSourceProperty("prepStmtCacheSize", "250");
        config.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");
        config.setMaximumPoolSize(100);  // Set the maximum number of connections here
        return new HikariDataSource(config);
    }


}