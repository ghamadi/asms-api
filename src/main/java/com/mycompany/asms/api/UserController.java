package com.mycompany.asms.api;

import com.mycompany.asms.model.Entity;
import com.mycompany.asms.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Map;

@RestController
@CrossOrigin
@RequestMapping("/api/v1/users")
public class UserController implements EntityController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @Override
    public long add(@Valid Entity entity) {
        return userService.add(entity);
    }

    @Override
    public Map<String, ? extends Entity> getByIDs(String[] ids) {
        return userService.getByIDs(ids);
    }

    @Override
    public void updateByID(@Valid String id, @Valid Entity entity) {
        userService.updateByID(id, entity);
    }

    @Override
    public void deleteByIDs(@Valid String[] ids) {
        userService.deleteByIDs(ids);
    }

}
