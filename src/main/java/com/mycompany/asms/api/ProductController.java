package com.mycompany.asms.api;

import com.mycompany.asms.model.Entity;
import com.mycompany.asms.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.Map;

@CrossOrigin
@RestController
@RequestMapping("/api/v1/products")
public class ProductController implements EntityController{

    private final ProductService productService;

    @Autowired
    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @Override
    public long add(@Valid Entity entity) {
        return productService.add(entity);
    }

    @Override
    public Map<String, ? extends Entity> getByIDs(String[] ids) {
        return productService.getByIDs(ids);
    }

    @Override
    public void updateByID(@Valid String id, @Valid Entity entity) {
        productService.updateByID(id, entity);
    }

    @Override
    public void deleteByIDs(String[] ids) {
        productService.deleteByIDs(ids);
    }
}
