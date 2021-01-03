package com.mycompany.asms.dao;

import com.mycompany.asms.model.Entity;
import com.mycompany.asms.model.Payment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.LinkedHashMap;

@Repository("payment_dao")
public class PaymentDAO extends EntityDAO {

    private final String TBL_PAYMENTS = "payments",
                        ID = "id",
                        PRJ_ID = "project_id",
                        PMT_DATE = "payment_date",
                        CHECK_NUM = "check_num",
                        AMOUNT = "amount";

    @Autowired
    public PaymentDAO(JdbcTemplate jdbcTemplate) {
        super(jdbcTemplate);
        fieldsToColumns.put("id", ID);
        fieldsToColumns.put("projectId", PRJ_ID);
        fieldsToColumns.put("paymentDate", PMT_DATE);
        fieldsToColumns.put("checkNum", CHECK_NUM);
        fieldsToColumns.put("amount", AMOUNT);
    }

    @Override
    public long insert(Entity newEntity) {
        return super.insert(TBL_PAYMENTS, newEntity);
    }

    @Override
    public void updateByID(String oldEntityID, Entity newEntity) {
        var condition = String.format("WHERE id = %s", oldEntityID);
        super.update(TBL_PAYMENTS, condition, newEntity);
    }

    @Override
    public LinkedHashMap<String, ? extends Entity> selectByIDs(String[] entityIDs) {
        //this is never called in the current api because I want payments relative to a certain project
        return selectPayments(buildOrCondition(ID, entityIDs));
    }

    public LinkedHashMap<String, ? extends Entity> selectByIDs(String[] entityIDs, String projectID) {
        var condition = buildOrCondition(ID, entityIDs);
        condition += condition.indexOf("WHERE") >= 0 ? " AND project_id = " + projectID : "WHERE project_id = " + projectID;
        return selectPayments(condition);
    }

    @Override
    public void deleteByIDs(String[] entityIDs) {
        super.delete(TBL_PAYMENTS, buildOrCondition(ID, entityIDs));
    }

    private LinkedHashMap<String, ? extends Entity> selectPayments(String condition) {
        var sql = String.format("SELECT * FROM %s %s", TBL_PAYMENTS, condition);
        RowMapper<Payment> mapper = (resultSet, i) -> {
            Payment payment = new Payment();
            payment.setId(String.valueOf(resultSet.getInt(ID)));
            payment.setPaymentDate(resultSet.getDate(PMT_DATE));
            payment.setAmount(resultSet.getDouble(AMOUNT));
            payment.setCheckNum(resultSet.getString(CHECK_NUM));
            payment.setProjectId(String.valueOf(resultSet.getInt(PRJ_ID)));
            return payment;
        };
        return super.select(sql, mapper);
    }
}
