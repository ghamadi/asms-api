package com.mycompany.asms.service;

import com.mycompany.asms.dao.PaymentDAO;
import com.mycompany.asms.model.Entity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class PaymentService implements WeakEntityService {

    @Qualifier("payment_dao")
    private final PaymentDAO paymentDAO;

    @Autowired
    public PaymentService(PaymentDAO paymentDAO) {
        this.paymentDAO = paymentDAO;
    }

    @Override
    public long add(Entity entity, String projectID) {
        return paymentDAO.insert(entity);
    }

    @Override
    public Map<String, ? extends Entity> getByIDs(String[] ids, String projectID) {
        return paymentDAO.selectByIDs(ids, projectID);
    }

    @Override
    public void updateByID(String id, Entity newEntity, String projectID) {
        paymentDAO.updateByID(id, newEntity);
    }

    @Override
    public void deleteByIDs(String[] ids, String projectID) {
        paymentDAO.deleteByIDs(ids);
    }
}
