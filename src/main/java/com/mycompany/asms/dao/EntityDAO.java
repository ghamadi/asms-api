package com.mycompany.asms.dao;

import com.mycompany.asms.exceptions.BadParametersFormatException;
import com.mycompany.asms.exceptions.IntegrityConstraintException;
import com.mycompany.asms.exceptions.RecordNotFoundException;
import com.mycompany.asms.model.Entity;
import com.mycompany.asms.utils.query.Value;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.jdbc.BadSqlGrammarException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.util.ReflectionUtils;

import java.sql.SQLIntegrityConstraintViolationException;
import java.util.*;
import java.util.stream.Collectors;

@SuppressWarnings({"unchecked"})
public abstract class EntityDAO {

    protected final JdbcTemplate jdbcTemplate;
    protected final HashMap<String, String> fieldsToColumns;


    @Autowired
    public EntityDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        fieldsToColumns = new HashMap<>();
    }

    /**
     * Selects records according to the passed SQL statement and maps the records to a LinkedHashMap of Entity objects
     * @param sql    the statement to be used for selection
     * @param mapper to map the returned rows to a List of entities
     * @param <T>    generic representing a subtype of Entity
     * @return LinkedHashMap mapping Entity id to Entity
     */
    protected <T extends Entity> LinkedHashMap<String, T> select(String sql, RowMapper<T> mapper) {
        var items = selectAsList(sql, mapper);
        return items.stream()
                .collect(Collectors.toMap(Entity::getId, entity -> entity, (e1, e2) -> e1, LinkedHashMap::new));
    }

    /**
     * Selects records according to the passed SQL statement and maps the records to a List of Entity objects
     * @param sql    the statement to be used for selection
     * @param mapper to map the returned rows to a List of entities
     * @param <T>    generic representing a subtype of Entity
     * @return Entity List with each object representing a record in the database
     */
    protected <T extends Entity> List<T> selectAsList(String sql, RowMapper<T> mapper) {
        if (sql == null || sql.isBlank() || sql.trim().indexOf("SELECT") != 0)
            throw new IllegalArgumentException("SQL statement should be a SELECT statement");

        if (mapper == null)
            throw new IllegalArgumentException("RowMapper object cannot be null");

        sql = sql.trim();
        List<T> items;
        try {
            items = jdbcTemplate.query(sql, mapper);
        } catch (BadSqlGrammarException e) {
            e.printStackTrace();
            throw new BadParametersFormatException("Bad parameters format.");
        }
        if (items.size() == 0) throw new RecordNotFoundException();
        return items;
    }

    /**
     * Inserts a record into a table using the passed tblName.
     * The fields to be set in the inserted record are those explicitly set in the passed entity.
     * Uses buildInsertStatement to create an INSERT sql statement with only those fields
     * Then uses the helper insert method to run the generated sql statement and get the return value
     *
     * @param tblName the name of the table within which the record-to-be-updated exists
     * @param entity  the Entity object that holds the new values for the fields to be updated
     * @param <T>     generic representing a subtype of Entity
     * @return the id
     */
    protected <T extends Entity> long insert(String tblName, T entity) {
        var sql = buildInsertStatement(tblName, entity);
        return insert(sql);
    }

    /**
     * Inserts a record into a table using the passed sql statement.
     *
     * @param sql     the sql statement to run and insert data
     * @param <T>     generic representing a subtype of Entity
     * @return the id
     */
    protected <T extends Entity> long insert(String sql) {
        try {
            var keyHolder = new GeneratedKeyHolder();
            jdbcTemplate.update(connection -> connection.prepareCall(sql), keyHolder);
            return keyHolder.getKey() != null ? keyHolder.getKey().longValue() : 0;
        } catch (DataIntegrityViolationException e){
            e.printStackTrace();
            throw new IntegrityConstraintException("Data violates an integrity constraint.", e.getCause());
        } catch (BadSqlGrammarException e) {
            e.printStackTrace();
            throw new BadParametersFormatException("Bad Parameters Format. Possibly no matching keys found in the JSON object.");
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    /**
     * Updates a record selected from a table using the passed tblName and condition.
     * The fields to be updated are those explicitly set in the passed newEntity.
     * Uses buildUpdateStatement to create an UPDATE sql statement with only those fields
     *
     * @param tblName   the name of the table within which the record-to-be-updated exists
     * @param condition the condition on which the record is to be found and updated
     * @param newEntity the Entity object that holds the new values for the fields to be updated
     * @param <T>       generic representing a subtype of Entity
     */
    protected <T extends Entity> void update(String tblName, String condition, T newEntity) {
        int recordsUpdated;
        var sql = buildUpdateStatement(tblName, condition, newEntity);
        try {
            recordsUpdated = jdbcTemplate.update(sql);
        } catch (DataIntegrityViolationException e) {
            throw new IntegrityConstraintException(e);
        } catch (BadSqlGrammarException e) {
            throw new BadParametersFormatException("Bad Parameters Format. Possibly no matching keys found in the JSON object.");
        }
        if (recordsUpdated == 0)
            throw new RecordNotFoundException();
    }

    /**
     * Deletes a record selected from a table using the passed tblName and condition.
     *
     * @param tblName   the name of the table within which the record-to-be-deleted exists
     * @param condition the condition on which the record is to be found and deleted
     */
    protected void delete(String tblName, String condition) {
        var sql = String.format("DELETE FROM %s %s", tblName, condition);
        try {
            if (jdbcTemplate.update(sql) == 0 && condition != null && !condition.isBlank())
                throw new RecordNotFoundException();
        } catch (DataIntegrityViolationException e){
            throw new IntegrityConstraintException(new Throwable("Foreign key constraint fails."));
        } catch (RecordNotFoundException e) {
            throw new RecordNotFoundException();
        }
    }

    /**
     * Builds a WHERE SQL clause equating the passed fieldName to each of the passed values and combining
     * all conditions with OR. Example: "WHERE id = 1 OR id = 2"
     *
     * @param fieldName the field to be used on the left-hand-side of the equal sign
     * @param values    the array of values to be used onthe right-hand-side of the equal sign
     * @return an empty String if values is null or empty and the appropriate WHERE clause otherwise
     */
    protected String buildOrCondition(String fieldName, String[] values) {
        if (values == null || values.length < 1) return "";
        var valuesString = Arrays.stream(values)
                .map(id -> String.format("%s = %s", fieldName, new Value<>(id).get()))
                .collect(Collectors.joining(" OR "));
        return String.format("WHERE %s", valuesString);
    }

    /*
     *****************************
            ABSTRACT METHODS
     *****************************
     */
    public abstract long insert(Entity newEntity);

    public abstract void updateByID(String oldEntityID, Entity newEntity);

    public abstract LinkedHashMap<String, ? extends Entity> selectByIDs(String[] entityIDs);

    public abstract void deleteByIDs(String[] entityIDs);

    /*
     *****************************
            HELPER METHODS
     *****************************
     */

    /**
     * creates an INSERT sql statement utilizing the passed table name and the passed entity representing a record in that table.
     *
     * @param tableName the table to which the sql INSERT statement is for
     * @param entity    the entity whose fields and values will be used in the INSERT statement
     * @param <T>       generic extending Entity
     * @return an INSERT sql statement as String
     */
    protected <T extends Entity> String buildInsertStatement(String tableName, T entity) {
        LinkedHashSet<String> setFields = entity.getExplicitlySetFields();
        LinkedHashMap<String, Object> fieldsToValues = getFieldsToValues(entity);

        return String.format("INSERT INTO %s (%s) VALUES (%s)",
                tableName,
                setFields.stream()
                        .map(fieldsToColumns::get)
                        .collect(Collectors.joining(", ")),
                setFields.stream()
                        .map(fieldName -> new Value<>(fieldsToValues
                                .get(fieldName)).get())
                        .collect(Collectors.joining(", ")));
    }

    /**
     * Creates an UPDATE sql statement utilizing the passed tableName, the passed entity representing a record in that table
     * as well as the passed condition
     *
     * @param tableName the table to which the sql INSERT statement is for
     * @param condition the condition with which to select records to be updated. Must include the "WHERE" keyword
     * @param entity    the entity representing a record in the table to be updated
     * @param <T>       generic extending Entity
     * @return an UPDATE sql statement as a String
     */
    private <T extends Entity> String buildUpdateStatement(String tableName, String condition, T entity) {
        Set<String> setFields = entity.getExplicitlySetFields();
        LinkedHashMap<String, Object> fieldsToValues = getFieldsToValues(entity);
        String sql = "UPDATE %s SET ";

        sql += setFields.stream().map(fieldName -> {
            String column = fieldsToColumns.get(fieldName),
                    value = new Value<>(fieldsToValues.get(fieldName)).get();
            return String.format("%s = %s ", column, value);
        }).collect(Collectors.joining(", ")) + condition;

        return String.format(sql, tableName);
    }

    /**
     * Loops over the fields of the passed Entity object and builds a map that maps field name to stored value
     *
     * @param entity the Entity object whose fields will be scanned
     * @return a map from field name to the value stored in that field of the passed entity
     */
    private LinkedHashMap<String, Object> getFieldsToValues(Entity entity) {
        LinkedHashMap<String, Object> map = new LinkedHashMap<>();
        ReflectionUtils.doWithFields(entity.getClass(), field -> {
            ReflectionUtils.makeAccessible(field);
            map.put(field.getName(), field.get(entity));
        });
        return map;
    }
}
