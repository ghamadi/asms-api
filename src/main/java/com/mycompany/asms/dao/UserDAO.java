package com.mycompany.asms.dao;

import com.mycompany.asms.exceptions.BadParametersFormatException;
import com.mycompany.asms.exceptions.IntegrityConstraintException;
import com.mycompany.asms.model.Entity;
import com.mycompany.asms.model.Role;
import com.mycompany.asms.model.User;
import com.mycompany.asms.model.UserRole;
import com.mycompany.asms.utils.query.Value;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.jdbc.BadSqlGrammarException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;

@Repository("user_dao")
@EnableTransactionManagement
public class UserDAO extends EntityDAO {

    //names of the columns in the related table(s)
    private final String TBL_USERS = "users",
                        ID = "username",
                        PASS = "password",
                        FIRST_NAME = "first_name",
                        LAST_NAME = "last_name",
                        PHONE = "phone",
                        ACTIVE = "active",
                        ADDRESS = "address",
                        USERNAME = "username",
                        ROLE = "role";

    @Autowired
    public UserDAO(JdbcTemplate jdbcTemplate) {
        super(jdbcTemplate);
        fieldsToColumns.put("id", ID);
        fieldsToColumns.put("firstName", FIRST_NAME);
        fieldsToColumns.put("lastName", LAST_NAME);
        fieldsToColumns.put("phone", PHONE);
        fieldsToColumns.put("address", ADDRESS);
        fieldsToColumns.put("password", PASS);
        fieldsToColumns.put("active", ACTIVE);
        fieldsToColumns.put("username", USERNAME);
        fieldsToColumns.put("role", ROLE);
    }

    public String getRefreshToken(String username) {
        String sql = String.format("SELECT refresh_token FROM users WHERE username = '%s'", username);
        RowMapper<String> mapper = (resultSet, i) -> resultSet.getString("refresh_token");
        try {
            return jdbcTemplate.query(sql, mapper).get(0);
        } catch (BadSqlGrammarException e) {
            e.printStackTrace();
            throw new BadParametersFormatException("Bad parameters format.");
        }
    }

    public int updateRefreshToken(String newToken, String username) {
        newToken = newToken == null ? null : String.format("'%s'", newToken);
        String sql = String.format("UPDATE users SET refresh_token=%s WHERE username='%s'", newToken, username);
        return jdbcTemplate.update(sql);
    }

    @Override
    @Transactional
    public long insert(Entity newEntity) {
        User user = (User) newEntity;
        user.setPassword(new BCryptPasswordEncoder().encode(user.getPassword()));
        UserRole userRole = new UserRole(user.getId());
        long insertedUserID = super.insert(TBL_USERS, user);
        super.insert("users_roles", userRole);

        return insertedUserID;
    }

    @Override
    public void updateByID(String oldEntityID, Entity newEntity) {
        var condition = String.format("WHERE id = %s", new Value<>(oldEntityID).get());
        super.update(TBL_USERS, condition, newEntity);
    }

    @Override
    public LinkedHashMap<String, ? extends Entity> selectByIDs(String[] entityIDs) {
        return selectUsers(buildOrCondition(ID, entityIDs));
    }

    @Override
    public void deleteByIDs(String[] entityIDs) {
        super.delete(TBL_USERS, buildOrCondition(ID, entityIDs));
    }

    public List<Role> getRoles(String username) {
        String sql = "SELECT username, role as authority " +
                "FROM users_roles WHERE username = " + new Value<>(username).get();
        RowMapper<Role> mapper = ((resultSet, i) -> {
           Role role = new Role();
           role.setTitle(resultSet.getString("authority"));
           return role;
        });
        return super.selectAsList(sql, mapper);
    }

    private LinkedHashMap<String, ? extends Entity> selectUsers(String condition) {
        var sql = String.format("SELECT * FROM %s %s", TBL_USERS, condition).trim();
        RowMapper<User> mapper = (resultSet, i) -> {
            User user = new User();
            user.setId(resultSet.getString(ID));
            user.setFirstName(resultSet.getString(FIRST_NAME));
            user.setLastName(resultSet.getString(LAST_NAME));
            user.setPhone(resultSet.getString(PHONE));
            user.setAddress(resultSet.getString(ADDRESS));
            user.setPassword(resultSet.getString(PASS));
            user.setActive(resultSet.getBoolean(ACTIVE));
            return user;
        };
        return super.select(sql, mapper);
    }


}
