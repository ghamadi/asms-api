package com.mycompany.asms.api;


import com.mycompany.asms.model.Entity;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Map;


public interface EntityController {

    @PostMapping("/add")
    long add(@Valid @NonNull @RequestBody Entity entity);

    @GetMapping("/get")
    Map<String, ? extends Entity> getByIDs(@RequestParam(required = false, name = "id") String[] ids);

    @PutMapping("/update")
    void updateByID(@Valid @NonNull @RequestParam String id, @Valid @NonNull @RequestBody Entity entity);

    @DeleteMapping("/delete")
    void deleteByIDs(@RequestParam(required = false, name = "id") String[] ids);
}
