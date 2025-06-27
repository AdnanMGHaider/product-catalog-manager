DROP TABLE IF EXISTS product;

-- Create the 'product' table
CREATE TABLE IF NOT EXISTS product (
    id INT AUTO_INCREMENT PRIMARY KEY,
    productCode VARCHAR(50) NOT NULL,
    productBrand VARCHAR(100) NOT NULL,
    quantityInHand INT NOT NULL,
    unitPrice DECIMAL(10,2) NOT NULL
);