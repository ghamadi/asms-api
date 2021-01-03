package com.mycompany.asms.api;

import com.mycompany.asms.model.Client;
import com.mycompany.asms.model.Entity;
import com.mycompany.asms.service.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.Map;

@RestController
@CrossOrigin
@RequestMapping("/api/v1/clients")
public class ClientController implements EntityController {

    private final ClientService clientService;

    @Autowired
    public ClientController(ClientService clientService) {
        this.clientService = clientService;
    }

    @Override
    public long add(@Valid Entity entity) {
        return clientService.add(entity);
    }

    @Override
    public Map<String, ? extends Entity> getByIDs(String[] ids) {
        return clientService.getByIDs(ids);
    }

    @Override
    public void updateByID(@Valid String id, @Valid Entity entity) {
        clientService.updateByID(id, entity);
    }

    @Override
    public void deleteByIDs(String[] ids) {
        clientService.deleteByIDs(ids);
    }
}
