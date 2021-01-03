package com.mycompany.asms.dao;

import com.mycompany.asms.model.*;
import com.mycompany.asms.utils.project_mapper.ComplexProject;
import com.mycompany.asms.utils.project_mapper.ProjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.Transactional;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedHashMap;
import java.util.List;

@EnableTransactionManagement
@Repository("project_dao")
public class ProjectDAO extends EntityDAO {

    private static final String TBL_PRJ = "projects",
            ID = "id",
            CLIENT_ID = "client_id",
            DELIVERY_DATE = "delivery_date",
            INIT_DATE = "init_date",
            STATUS = "status",
            PRJ_ID = "project_id",
            DESC = "description",
            TITLE = "title",
            PRJ_SEC_ID = "project_section_id",
            COMP_NAME = "compartment_name",
            PRD_ID = "product_id",
            LENGTH = "length",
            WIDTH = "width",
            UNIT_PRICE = "unit_price";

    private static final String DETAILED_PROJECT_QUERY = "SELECT p.id AS id, p.title AS title, p.status AS status, p.delivery_date AS delivery_date, p.init_date AS init_date, " +
            "c.id As client_id, c.name AS client_name, c.phone AS client_phone, c.address AS client_address, " +
            "dps.section_id AS section_id, dps.project_id AS project_id, " +
            "dps.section_description AS section_description, dps.compartment_id AS compartment_id, " +
            "dps.compartment_name AS compartment_name, dps.product_id AS product_id, " +
            "dps.product_type AS product_type, dps.product_name AS product_name, dps.sales_unit AS sales_unit, " +
            "dps.default_unit_price AS default_unit_price, dps.length AS length, " +
            "dps.width AS width, dps.unit_price AS unit_price, " +
            "pmt.id AS payment_id, pmt.payment_date, pmt.check_num, pmt.amount AS payment_amount " +
            "FROM projects p " +
            "JOIN clients c on p.client_id = c.id " +
            "LEFT JOIN  payments pmt ON p.id = pmt.project_id " +
            "LEFT JOIN (SELECT ps.id AS section_id, ps.project_id AS project_id, ps.description AS " +
            "section_description, sc.id AS compartment_id,sc.compartment_name AS compartment_name, " +
            "prd.id AS product_id,prd.product_type AS product_type,prd.product_name AS product_name," +
            "prd.sales_unit AS sales_unit,prd.unit_price AS default_unit_price, " +
            "sc.length AS length, sc.width AS width,sc.unit_price AS unit_price " +
            "FROM project_sections ps " +
            "LEFT JOIN section_compartments sc on ps.id = sc.project_section_id " +
            "LEFT JOIN products prd on sc.product_id = prd.id " +
            ") dps " +
            "ON p.id = dps.project_id %s";

    private final String SIMPLE_PROJECT_QUERY = "SELECT p.*, c.name as client_name, c.phone as client_phone, c.address as client_address " +
            "FROM %s p " +
            "INNER JOIN clients c ON p.client_id = c.id %s";

    @Autowired
    public ProjectDAO(JdbcTemplate jdbcTemplate) {
        super(jdbcTemplate);
        fieldsToColumns.put("id", ID);
        fieldsToColumns.put("clientId", CLIENT_ID);
        fieldsToColumns.put("deliveryDate", DELIVERY_DATE);
        fieldsToColumns.put("initDate", INIT_DATE);
        fieldsToColumns.put("status", STATUS);
        fieldsToColumns.put("title", TITLE);
        fieldsToColumns.put("projectId", PRJ_ID);
        fieldsToColumns.put("description", DESC);
        fieldsToColumns.put("projectSectionId", PRJ_SEC_ID);
        fieldsToColumns.put("compartmentName", COMP_NAME);
        fieldsToColumns.put("productId", PRD_ID);
        fieldsToColumns.put("length", LENGTH);
        fieldsToColumns.put("width", WIDTH);
        fieldsToColumns.put("unitPrice", UNIT_PRICE);

    }


    @Override
    @Transactional
    public long insert(Entity newEntity) {

        Project p = (Project) newEntity;

        long projectId = super.insert(TBL_PRJ, p);

        if (p.getSections() != null && p.getSections().size() > 0) {
            for (var sectionEntry : p.getSections().entrySet()) {
                var section = sectionEntry.getValue();
                section.setProjectId(String.valueOf(projectId));
                addSectionToProject(section);
            }
        }
        return projectId;
    }

    private void addSectionToProject(ProjectSection section) {
        var sectionId = super.insert("project_sections", section);
        section.setId(String.valueOf(sectionId));
        if (section.getCompartments() != null && section.getCompartments().size() > 0) {
            String sql = "";
            for (var compEntry : section.getCompartments().entrySet()) {
                var compartment = compEntry.getValue();
                compartment.setProjectSectionId(section.getId());
                super.insert("section_compartments", compartment);
            }
        }
    }

    @Override
    @Transactional
    public void updateByID(String oldEntityID, Entity newEntity) {
        var condition = String.format("WHERE id = %s", oldEntityID);
        var p = (Project) newEntity;
        super.update(TBL_PRJ, condition, p);

        //Because this application is meant to be used on localhost only, deleting sections and then re-adding them will be fast enough (even though updating every section and its components may be faster)
        String deleteSectionsSql = "DELETE FROM project_sections WHERE project_id = " + oldEntityID;

        jdbcTemplate.update(deleteSectionsSql);

        if (p.getSections() != null && p.getSections().size() > 0) {
            for (var sectionEntry : p.getSections().entrySet()) {
                var section = sectionEntry.getValue();
                section.setProjectId(String.valueOf(oldEntityID));
                addSectionToProject(section);
            }
        }
    }

    @Override
    public LinkedHashMap<String, ? extends Entity> selectByIDs(String[] entityIDs) {
        var condition = buildOrCondition("p.id", entityIDs);

        //Decided to skip the "simple project" approach. Having all the info related to all the projects is better for a SPA
//        if(condition.isBlank())
//            return selectProjectsSimple(condition);
        return selectProjectsDetailed(condition);
    }

    @Override
    @Transactional
    public void deleteByIDs(String[] entityIDs) {
        super.delete(TBL_PRJ, buildOrCondition(ID, entityIDs));
    }


    private LinkedHashMap<String, ? extends Entity> selectProjectsSimple(String condition) {
        var sql = String.format(SIMPLE_PROJECT_QUERY, TBL_PRJ, condition);

        RowMapper<Project> mapper = (resultSet, i) -> {
            var project = buildProjectFromResultSet(resultSet);
            project.setPayments(null);
            project.setSections(null);
            return project;
        };
        return super.select(sql, mapper);
    }

    private LinkedHashMap<String, ? extends Entity> selectProjectsDetailed(String condition) {
        var sql = String.format(DETAILED_PROJECT_QUERY, condition);
        RowMapper<ComplexProject> mapper = (resultSet, i) -> {
            var project = buildProjectFromResultSet(resultSet);
            var section = buildProjectSectionFromResultSet(resultSet);
            var compartment = buildSectionCompartmentFromResultSet(resultSet);
            var payment = buildPaymentFromResultSet(resultSet);
            return new ComplexProject(project, section, compartment, payment);
        };
        List<ComplexProject> complexProjects = super.selectAsList(sql, mapper);
        return new ProjectMapper(complexProjects).map();
    }


    private Project buildProjectFromResultSet(ResultSet resultSet) throws SQLException {
        Project project = new Project();
        project.setId(String.valueOf(resultSet.getInt(ID)));
        project.setTitle(resultSet.getString(TITLE));
        project.setClientId(String.valueOf(resultSet.getInt(CLIENT_ID)));
        project.setDeliveryDate(resultSet.getDate(DELIVERY_DATE));
        project.setInitDate(resultSet.getDate(INIT_DATE));
        project.setStatus(resultSet.getString(STATUS));
        project.setClient(buildClientFromResultSet(resultSet));
        return project;
    }

    private Client buildClientFromResultSet(ResultSet resultSet) throws SQLException {
        Client client = new Client();
        client.setId(String.valueOf(resultSet.getInt("client_id")));
        client.setName(resultSet.getString("client_name"));
        client.setPhone(resultSet.getString("client_phone"));
        client.setAddress(resultSet.getString("client_address"));
        return client;
    }

    private ProjectSection buildProjectSectionFromResultSet(ResultSet resultSet) throws SQLException {
        ProjectSection ps = new ProjectSection();
        var id = String.valueOf(resultSet.getInt("section_id"));

        if (id.equals("0")) return null;

        ps.setId(id);
        ps.setDescription(resultSet.getString("section_description"));
        ps.setProjectId(String.valueOf(resultSet.getInt("project_id")));
        return ps;
    }

    private SectionCompartment buildSectionCompartmentFromResultSet(ResultSet resultSet) throws SQLException {
        SectionCompartment sc = new SectionCompartment();

        var id = String.valueOf(resultSet.getInt("compartment_id"));

        if (id.equals("0")) return null;

        sc.setId(id);
        sc.setCompartmentName(resultSet.getString("compartment_name"));
        sc.setProductId(String.valueOf(resultSet.getInt("product_id")));
        sc.setProjectSectionId(String.valueOf(resultSet.getInt("section_id")));
        sc.setProductId(String.valueOf(resultSet.getInt("product_id")));
        sc.setUnitPrice(resultSet.getDouble("unit_price"));
        sc.setLength(resultSet.getDouble("length"));
        sc.setWidth(resultSet.getDouble("width"));
        sc.setProduct(buildProductFromResultSet(resultSet));
        return sc;
    }

    private Product buildProductFromResultSet(ResultSet resultSet) throws SQLException {
        Product p = new Product();
        p.setId(String.valueOf(resultSet.getInt("product_id")));
        p.setProductType(resultSet.getString("product_type"));
        p.setProductName(resultSet.getString("product_name"));
        p.setSalesUnit(resultSet.getString("sales_unit"));
        p.setUnitPrice(resultSet.getDouble("default_unit_price"));
        return p;
    }

    private Payment buildPaymentFromResultSet(ResultSet resultSet) throws SQLException {
        Payment p = new Payment();
        var id = String.valueOf(resultSet.getInt("payment_id"));
        if (id.equals("0")) return null;
        p.setId(id);
        p.setPaymentDate(resultSet.getDate("payment_date"));
        p.setCheckNum(resultSet.getString("check_num"));
        p.setAmount(resultSet.getDouble("payment_amount"));
        return p;
    }
}
