package com.mycompany.asms.dao;

import com.mycompany.asms.model.Entity;
import com.mycompany.asms.model.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.LinkedHashMap;

@Repository("product_dao")
public class ProductDAO extends EntityDAO{

    private final static String TBL_PRD = "products",
                ID = "id", TYPE = "product_type",
                NAME = "product_name", SALES_UNIT = "sales_unit",
                UNIT_PRICE = "unit_price";

    @Autowired
    public ProductDAO(JdbcTemplate jdbcTemplate) {
        super(jdbcTemplate);
        fieldsToColumns.put("id", ID);
        fieldsToColumns.put("productType", TYPE);
        fieldsToColumns.put("productName", NAME);
        fieldsToColumns.put("salesUnit", SALES_UNIT);
        fieldsToColumns.put("unitPrice", UNIT_PRICE);
    }

    @Override
    public long insert(Entity newEntity) {
        return super.insert(TBL_PRD, newEntity);
    }

    @Override
    public void updateByID(String oldEntityID, Entity newEntity) {
        var condition = String.format("WHERE id = %s", oldEntityID);
        super.update(TBL_PRD, condition, newEntity);
    }

    @Override
    public LinkedHashMap<String, ? extends Entity> selectByIDs(String[] entityIDs) {
        return selectProducts(super.buildOrCondition(ID, entityIDs));
    }

    @Override
    public void deleteByIDs(String[] entityIDs) {
        super.delete(TBL_PRD, buildOrCondition(ID, entityIDs));
    }

    private LinkedHashMap<String, ? extends Entity> selectProducts(String condition) {
        var sql = String.format("SELECT * FROM %s %s", TBL_PRD, condition);
        RowMapper<Product> mapper = ((resultSet, i) -> {
            Product p = new Product();
            p.setId(String.valueOf(resultSet.getInt(ID)));
            p.setProductName(resultSet.getString(NAME));
            p.setProductType(resultSet.getString(TYPE));
            p.setSalesUnit(resultSet.getString(SALES_UNIT));
            p.setUnitPrice(resultSet.getDouble(UNIT_PRICE));
            return p;
        });
        return super.select(sql, mapper);
    }

}
