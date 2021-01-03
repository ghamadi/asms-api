package com.mycompany.asms.service;

import com.mycompany.asms.dao.ProductDAO;
import com.mycompany.asms.model.Entity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class ProductService implements EntityService {

    @Qualifier("product_dao")
    private final ProductDAO productDAO;

    @Autowired
    public ProductService(ProductDAO productDAO) {
        this.productDAO = productDAO;
    }

    @Override
    public long add(Entity entity) {
        return productDAO.insert(entity);
    }

    @Override
    public Map<String, ? extends Entity> getByIDs(String[] ids) {
        return productDAO.selectByIDs(ids);
    }

    @Override
    public void updateByID(String id, Entity newEntity) {
        productDAO.updateByID(id, newEntity);
    }

    @Override
    public void deleteByIDs(String[] ids) {
        productDAO.deleteByIDs(ids);
    }
}
