CREATE DATABASE IF NOT EXISTS toy_kv_store;
USE toy_kv_store;
CREATE TABLE IF NOT EXISTS kv_store (
    id INT AUTO_INCREMENT PRIMARY KEY,
    kv_key VARCHAR(100) NOT NULL UNIQUE,
    value TEXT,
    expired_timestamp BIGINT,
    is_delete BOOLEAN DEFAULT FALSE
);