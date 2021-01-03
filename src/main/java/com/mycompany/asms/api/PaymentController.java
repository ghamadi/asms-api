package com.mycompany.asms.api;

import com.mycompany.asms.model.Entity;
import com.mycompany.asms.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.Map;

@RestController
@CrossOrigin
@RequestMapping("/api/v1/projects/{projectID}/payments")
public class PaymentController implements WeakEntityController {

    private final PaymentService paymentService;

    @Autowired
    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @Override
    public long add(@Valid Entity entity, @PathVariable String projectID) {
        return paymentService.add(entity, projectID);
    }

    @Override
    public Map<String, ? extends Entity> getByIDs(String[] ids, @PathVariable String projectID) {
        return paymentService.getByIDs(ids, projectID);
    }

    @Override
    public void updateByID(@Valid String id, @Valid Entity entity, @PathVariable String projectID) {
        paymentService.updateByID(id, entity, projectID);
    }

    @Override
    public void deleteByIDs(String[] ids, @PathVariable String projectID) {
        paymentService.deleteByIDs(ids, projectID);
    }
}
