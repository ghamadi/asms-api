package com.mycompany.asms.api;

import com.mycompany.asms.model.Entity;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Map;

public interface WeakEntityController {

    @PostMapping("/add")
    long add(@Valid @NonNull @RequestBody Entity entity, @PathVariable String parentID);

    @GetMapping("/get")
    Map<String, ? extends Entity> getByIDs(@RequestParam(required = false, name = "id") String[] ids, @PathVariable String parentID);

    @PutMapping("/update")
    void updateByID(@Valid @NonNull @RequestParam String id, @Valid @NonNull @RequestBody Entity entity, @PathVariable String parentID);

    @DeleteMapping("/delete")
    void deleteByIDs(@RequestParam(required = false, name = "id") String[] ids, @PathVariable String parentID);
}

