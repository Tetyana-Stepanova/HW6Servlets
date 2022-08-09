package models.developer;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DeveloperDaoService {
    private final PreparedStatement selectMaxIdSt;
    private final PreparedStatement createSt;
    private final PreparedStatement getDeveloperByIdSt;
    private final PreparedStatement getAllDevelopersSt;
    private final PreparedStatement updateSt;
    private final PreparedStatement deleteByIdSt;
    private final PreparedStatement getDevelopersByProjectIdSt;
    private final PreparedStatement getByLanguageDevelopersSt;
    private final PreparedStatement getBySkillLevelDevelopersSt;

    public DeveloperDaoService(Connection connection) throws SQLException {
        selectMaxIdSt = connection.prepareStatement("SELECT max(developers_id) AS maxId FROM developers");
        createSt = connection.prepareStatement("INSERT INTO developers " +
                "(first_name, last_name, age, gender, city, salary, companies_id)" +
                " VALUES (?, ?, ?, ?, ?, ?, ?)");
        getDeveloperByIdSt = connection.prepareStatement("SELECT * FROM developers WHERE developers_id = ?");
        getAllDevelopersSt = connection.prepareStatement(
                "SELECT developers_id, first_name, last_name, age, gender, city, salary, companies_id FROM developers");
        updateSt = connection.prepareStatement("UPDATE developers SET first_name = ?, last_name = ?, " +
                "age = ?, gender = ?, city = ?, salary = ?, companies_id = ? WHERE developers_id = ?");
        deleteByIdSt = connection.prepareStatement("DELETE FROM developers WHERE developers_id = ?");
        getDevelopersByProjectIdSt = connection.prepareStatement("SELECT developers.* " +
                "FROM developers_projects " +
                "LEFT JOIN developers " +
                "ON developers.developers_id = developers_projects.developers_id " +
                "WHERE project_id = ?");
        getByLanguageDevelopersSt = connection.prepareStatement("SELECT developers.*" +
                "FROM developers " +
                "LEFT JOIN developers_skills " +
                "ON developers_skills.developers_id = developers.developers_id " +
                "LEFT JOIN skills " +
                "ON developers_skills.skills_id = skills.skills_id " +
                "WHERE skills.dev_language = ?");
        getBySkillLevelDevelopersSt = connection.prepareStatement("SELECT developers.*" +
                "FROM developers " +
                "LEFT JOIN developers_skills " +
                "ON developers_skills.developers_id = developers.developers_id " +
                "LEFT JOIN skills " +
                "ON developers_skills.skills_id = skills.skills_id " +
                "GROUP BY skills.skill_level, developers.developers_id " +
                "HAVING skill_level = ?");
    }

    public int createDeveloper(Developer developer) throws SQLException {
        createSt.setString(1, developer.getFirstName());
        createSt.setString(2, developer.getLastName());
        createSt.setInt(3, developer.getAge());
        createSt.setString(4, developer.getGender());
        createSt.setString(5, developer.getCity());
        createSt.setInt(6, developer.getSalary());
        createSt.setInt(7, developer.getCompaniesId());
        createSt.executeUpdate();
        int id;
        try (ResultSet rs = selectMaxIdSt.executeQuery()) {
            rs.next();
            id = rs.getInt("maxId");
        }
        return id;
    }

    public Developer getDeveloperById(int id) throws SQLException {
        getDeveloperByIdSt.setInt(1, id);
        try(ResultSet rs = getDeveloperByIdSt.executeQuery()){
            if (!rs.next()){
                return null;
            }
            Developer developer = mapResultSet(rs);
            return developer;
        }

    }

    public List<Developer> getAllDevelopers() throws SQLException {
        try(ResultSet rs = getAllDevelopersSt.executeQuery()){
            return getListOfDevelopers(rs);
        }
    }

    public String update(Developer developer) throws SQLException {
        try {
            updateSt.setString(1, developer.getFirstName());
            updateSt.setString(2, developer.getLastName());
            updateSt.setInt(3, developer.getAge());
            updateSt.setString(4, developer.getGender());
            updateSt.setString(5, developer.getCity());
            updateSt.setInt(6, developer.getSalary());
            updateSt.setInt(7, developer.getCompaniesId());
            updateSt.setInt(8, developer.getDevelopersId());
            updateSt.executeUpdate();
            return "true";
        }catch (SQLException e) {
            return e.getMessage();
        }
    }

    public String deleteById(int id) throws SQLException {
        try {
            deleteByIdSt.setLong(1, id);
            deleteByIdSt.executeUpdate();
            return "true";
        }catch (SQLException e) {
            return e.getMessage();
        }
    }

    public List<Developer> getDevelopersByProject(int projectId) throws SQLException {
        getDevelopersByProjectIdSt.setInt(1,projectId);
        try(ResultSet rs = getDevelopersByProjectIdSt.executeQuery()){
            return getListOfDevelopers(rs);
        }
    }

    public List<Developer> getByLanguageDevelopers(String devLanguage) throws SQLException {
        getByLanguageDevelopersSt.setString(1, devLanguage);
        try(ResultSet rs = getByLanguageDevelopersSt.executeQuery()){
            return getListOfDevelopers(rs);
        }
    }

    public List<Developer> getBySkillLevelDevelopers(String skillLevel) throws SQLException {
        getBySkillLevelDevelopersSt.setString(1, skillLevel);
        try(ResultSet rs = getBySkillLevelDevelopersSt.executeQuery()){
            return getListOfDevelopers(rs);
        }
    }

    private Developer mapResultSet(ResultSet resultSet) throws SQLException {
        int developerId = resultSet.getInt("developers_id");
        String firstName = resultSet.getString("first_name");
        String lastName = resultSet.getString("last_name");
        int age = resultSet.getInt("age");
        String gender = resultSet.getString("gender");
        String city = resultSet.getString("city");
        Integer salary = resultSet.getInt("salary");
        int companiesId = resultSet.getInt("companies_id");
        return new Developer(developerId, firstName, lastName, age, gender, city, salary, companiesId);
    }

    private List<Developer> getListOfDevelopers(ResultSet rs) throws SQLException {
        List<Developer> result = new ArrayList<>();
        while (rs.next()){
            Developer developer = mapResultSet(rs);
            result.add(developer);
        }
        return result;
    }
}
