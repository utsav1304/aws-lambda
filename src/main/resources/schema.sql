CREATE TABLE IF NOT EXISTS product (product_code VARCHAR(255) NOT NULL , product_name VARCHAR(255), product_description VARCHAR(255), imagage_url VARCHAR(255) ,price DOUBLE PRECISION, is_sellable BOOLEAN, PRIMARY KEY (product_code));