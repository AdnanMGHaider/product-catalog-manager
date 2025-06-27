package ca.sheridan.golamhai.dao;

import ca.sheridan.golamhai.model.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Author: Adnan Haider
 *
 * Description:
 * The ProductsDatabaseAccess class serves as the Data Access Object (DAO) for
 * the Product entity.
 * It provides methods to perform CRUD operations and specific queries on the
 * product database table.
 */
@Repository
public class ProductsDatabaseAccess {

    @Autowired
    protected NamedParameterJdbcTemplate jdbc;

    /**
     * Adds a new product to the database.
     *
     * @param product The Product object to be added.
     * @return The number of rows affected.
     */
    public long addProduct(Product product) {
        String insert = "INSERT INTO product (productCode, productBrand, quantityInHand, unitPrice) " +
                "VALUES (:productCode, :productBrand, :quantityInHand, :unitPrice)";
        MapSqlParameterSource namedParameters = new MapSqlParameterSource()
                .addValue("productCode", product.getProductCode())
                .addValue("productBrand", product.getProductBrand())
                .addValue("quantityInHand", product.getQuantityInHand())
                .addValue("unitPrice", product.getUnitPrice());
        return jdbc.update(insert, namedParameters);
    }

    /**
     * Retrieves all products from the database.
     *
     * @return A list of Product objects.
     */
    public List<Product> selectProducts() {
        String query = "SELECT * FROM product";
        return jdbc.query(query, new MapSqlParameterSource(), new BeanPropertyRowMapper<>(Product.class));
    }

    /**
     * Retrieves a product by its ID.
     *
     * @param id The ID of the product.
     * @return The Product object if found, otherwise null.
     */
    public Product getProductById(int id) {
        String query = "SELECT * FROM product WHERE id = :id";
        MapSqlParameterSource namedParameters = new MapSqlParameterSource().addValue("id", id);
        List<Product> products = jdbc.query(query, namedParameters, new BeanPropertyRowMapper<>(Product.class));
        return products.isEmpty() ? null : products.get(0);
    }

    /**
     * Updates an existing product in the database.
     *
     * @param product The Product object with updated information.
     * @return The number of rows affected.
     */
    public int updateProduct(Product product) {
        String update = "UPDATE product SET productCode = :productCode, productBrand = :productBrand, " +
                "quantityInHand = :quantityInHand, unitPrice = :unitPrice WHERE id = :id";
        MapSqlParameterSource namedParameters = new MapSqlParameterSource()
                .addValue("productCode", product.getProductCode())
                .addValue("productBrand", product.getProductBrand())
                .addValue("quantityInHand", product.getQuantityInHand())
                .addValue("unitPrice", product.getUnitPrice())
                .addValue("id", product.getId());
        return jdbc.update(update, namedParameters);
    }

    /**
     * Deletes a product from the database by its ID.
     *
     * @param id The ID of the product to delete.
     * @return The number of rows affected.
     */
    public int deleteProduct(int id) {
        String delete = "DELETE FROM product WHERE id = :id";
        MapSqlParameterSource namedParameters = new MapSqlParameterSource().addValue("id", id);
        return jdbc.update(delete, namedParameters);
    }

    /**
     * Retrieves products by their brand.
     *
     * @param productBrand The brand of the products to retrieve.
     * @return A list of Product objects matching the brand.
     */
    public List<Product> selectProductsByBrand(String productBrand) {
        String query = "SELECT * FROM product WHERE productBrand = :productBrand";
        MapSqlParameterSource namedParameters = new MapSqlParameterSource().addValue("productBrand", productBrand);
        return jdbc.query(query, namedParameters, new BeanPropertyRowMapper<>(Product.class));
    }

    /**
     * Retrieves products with quantity in hand less than a specified threshold.
     *
     * @param quantityInHand The quantity threshold.
     * @return A list of Product objects below the specified quantity.
     */
    public List<Product> selectProductsByQuantity(int quantityInHand) {
        String query = "SELECT * FROM product WHERE quantityInHand < :quantityInHand";
        MapSqlParameterSource namedParameters = new MapSqlParameterSource().addValue("quantityInHand", quantityInHand);
        return jdbc.query(query, namedParameters, new BeanPropertyRowMapper<>(Product.class));
    }
}
