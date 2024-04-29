package org.example.storage;

import com.fasterxml.jackson.databind.ObjectMapper;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

public class MySQLStorage implements IStorage{

    private final DataSource dataSource;

    public MySQLStorage(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public void put(String key, String value, int ttlInSeconds) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        try {
            long currentTimestamp = System.currentTimeMillis(); // Current time in milliseconds
            long expiredTimestamp = currentTimestamp + (ttlInSeconds * 1000L); // Convert ttl from seconds to milliseconds

            // Get a connection from the data source
            conn = dataSource.getConnection();

            // Prepare SQL statement with parameters
            String sql = "INSERT INTO kv_store (`kv_key`, `value`, `expired_timestamp`, `is_delete`) " +
                    "VALUES (?, ?, ?, FALSE) " +
                    "ON DUPLICATE KEY UPDATE `value` = VALUES(`value`), `expired_timestamp` = VALUES(`expired_timestamp`), `is_delete` = FALSE";
            pstmt = conn.prepareStatement(sql);

            // Set parameters
            pstmt.setString(1, key);
            pstmt.setString(2, value);
            pstmt.setLong(3, expiredTimestamp);

            // Execute update
            pstmt.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // Close resources
            try {
                if (pstmt != null) pstmt.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public Optional<String> get(String key) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            // Get a connection from the data source
            conn = dataSource.getConnection();

            // Prepare SQL statement with parameter
            String sql = "SELECT value FROM kv_store WHERE `kv_key` = ? AND `is_delete` = FALSE";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, key);  // Set the key parameter

            // Execute query
            rs = pstmt.executeQuery();

            // Process the ResultSet
            if (rs.next()) {
                String resultValue = rs.getString("value");
                return Optional.of(resultValue);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // Close resources
            try {
                if (rs != null) rs.close();
                if (pstmt != null) pstmt.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return Optional.empty();
    }

    @Override
    public void delete(String key) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        try {
            // Get a connection from the data source
            conn = dataSource.getConnection();

            // Prepare the SQL statement for updating the record
            String sql = "UPDATE kv_store SET is_delete = TRUE WHERE `kv_key` = ?";
            pstmt = conn.prepareStatement(sql);

            // Set the key parameter
            pstmt.setString(1, key);

            // Execute the update
            int affectedRows = pstmt.executeUpdate();
            if (affectedRows > 0) {
                System.out.println("Record marked as deleted successfully.");
            } else {
                System.out.println("No record found with the specified key.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            // Ensure resources are closed
            try {
                if (pstmt != null) pstmt.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
