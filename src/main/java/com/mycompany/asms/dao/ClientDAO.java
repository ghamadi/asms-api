package com.mycompany.asms.dao;

import com.mycompany.asms.model.Client;
import com.mycompany.asms.model.Entity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.LinkedHashMap;

@Repository("client_dao")
public class ClientDAO extends EntityDAO {

    private static final String TBL_CLIENTS = "clients",
    ID = "id", NAME = "name", PHONE = "phone", ADDRESS = "address";

    @Autowired
    public ClientDAO(JdbcTemplate jdbcTemplate) {
        super(jdbcTemplate);
        //it happens that the fields and columns have the same names, so I used the same constants
        fieldsToColumns.put(ID, ID);
        fieldsToColumns.put(NAME, NAME);
        fieldsToColumns.put(PHONE, PHONE);
        fieldsToColumns.put(ADDRESS, ADDRESS);
    }

    @Override
    public long insert(Entity newEntity) {

        return super.insert(TBL_CLIENTS, newEntity);
    }

    @Override
    public void updateByID(String oldEntityID, Entity newEntity) {
        var condition = String.format("WHERE id = %s", oldEntityID);
        super.update(TBL_CLIENTS, condition, newEntity);
    }

    @Override
    public LinkedHashMap<String, ? extends Entity> selectByIDs(String[] entityIDs) {
        return selectClients(buildOrCondition(ID, entityIDs));
    }

    @Override
    public void deleteByIDs(String[] entityIDs) {
        super.delete(TBL_CLIENTS, buildOrCondition(ID, entityIDs));
    }

    private LinkedHashMap<String, ? extends Entity> selectClients(String condition){
        var sql = String.format("SELECT * FROM %s %s", TBL_CLIENTS, condition);
        RowMapper<Client> mapper = (resultSet, i) -> {
            Client client = new Client();
            client.setId(String.valueOf(resultSet.getInt(ID)));
            client.setName(resultSet.getString(NAME));
            client.setPhone(resultSet.getString(PHONE));
            client.setAddress(resultSet.getString(ADDRESS));
            return client;
        };
        return super.select(sql, mapper);
    }
}
