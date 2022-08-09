package models.project;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ProjectDaoService {
    private final PreparedStatement getMaxIdSt;
    private final PreparedStatement createProjectSt;
    private final PreparedStatement getAllProjectsSt;
    private final PreparedStatement getProjectByIdSt;
    private final PreparedStatement updateProjectSt;
    private final PreparedStatement deleteProjectByIdSt;
    private final PreparedStatement getDevelopersSalaryOfProjectSt;
    private final PreparedStatement getProjectsInSpecialFormatSt;

    public ProjectDaoService(Connection connection) throws SQLException {
        getMaxIdSt = connection.prepareStatement("SELECT max(project_id) AS maxId FROM projects");
        createProjectSt = connection.prepareStatement("INSERT INTO projects " +
                "(project_name, project_description, release_date, companies_id, customer_id) VALUES (?, ?, ?, ?, ?)");
        getAllProjectsSt = connection.prepareStatement(
                "SELECT project_id, project_name, project_description, release_date, companies_id, customer_id " +
                        "FROM projects");
        getProjectByIdSt = connection.prepareStatement(
                "SELECT project_name, project_description, release_date, companies_id, customer_id " +
                        "WHERE project_id = ?");
        updateProjectSt = connection.prepareStatement("UPDATE projects " +
                "SET project_name = ?, project_description = ?, release_date = ?, companies_id = ?, customer_id = ? " +
                "WHERE project_id = ?");
        deleteProjectByIdSt = connection.prepareStatement("DELETE FROM projects WHERE project_id = ?");
        getDevelopersSalaryOfProjectSt = connection.prepareStatement("SELECT SUM(salary) AS projectCost " +
                "FROM projects " +
                "LEFT JOIN developers_projects ON projects.project_id = developers_projects.project_id " +
                "LEFT JOIN developers ON developers.developers_id = developers_projects.developers_id " +
                "WHERE projects.project_id = ? ");
        getProjectsInSpecialFormatSt = connection.prepareStatement(
                "SELECT projects.release_date, projects.project_name, COUNT(developers_projects.project_id) " +
                        "AS developer_count " +
                        "FROM projects " +
                        "JOIN developers_projects ON projects.project_id = developers_projects.project_id " +
                        "GROUP BY project_name");
    }

    public int createProject(Project project) throws SQLException {
        createProjectSt.setString(1, project.getProjectName());
        createProjectSt.setString(2, project.getProjectDescription());
        createProjectSt.setString(3, project.getReleaseDate().toString());
        createProjectSt.setInt(4, project.getCompaniesId());
        createProjectSt.setInt(5, project.getCustomerId());
        createProjectSt.executeUpdate();
        int id;
        try (ResultSet rs = getMaxIdSt.executeQuery()) {
            rs.next();
            id = rs.getInt("maxId");
        }
        return id;
    }

    public Project getProjectById(int id) throws SQLException {
        getProjectByIdSt.setInt(1, id);
        try(ResultSet rs = getProjectByIdSt.executeQuery()){
            if (!rs.next()){
                return null;
            }
            Project project = mapResultSet(rs);
            return project;
        }
    }

    public List<Project> getAllProjects() throws SQLException {
        try(ResultSet rs = getAllProjectsSt.executeQuery()){
            return getListOfProjects(rs);
        }
    }

    public String updateProject(Project project) throws SQLException {
        try {
            updateProjectSt.setString(1, project.getProjectName());
            updateProjectSt.setString(2, project.getProjectDescription());
            updateProjectSt.setString(3, project.getReleaseDate().toString());
            updateProjectSt.setInt(4, project.getCompaniesId());
            updateProjectSt.setInt(5, project.getCustomerId());
            updateProjectSt.setInt(6, project.getProjectId());
            updateProjectSt.executeUpdate();
            return "true";
        }catch (SQLException e) {
            return e.getMessage();
        }
    }

    public String deleteProjectById(int id) throws SQLException {
        try {
            deleteProjectByIdSt.setInt(1, id);
            deleteProjectByIdSt.executeUpdate();
            return "true";
        }catch (SQLException e) {
            return e.getMessage();
        }
    }

    public int getDevelopersSalaryOfProject(int projectId) throws SQLException {
        getDevelopersSalaryOfProjectSt.setInt(1, projectId);
        int projectCost;
        try(ResultSet rs = getDevelopersSalaryOfProjectSt.executeQuery()){
            rs.next();
            projectCost = rs.getInt("projectCost");
        }
        return projectCost;
    }

    public List<project> getProjectsInSpecialFormat() throws SQLException {
        List<project> result = new ArrayList<>();
        try(ResultSet rs = getProjectsInSpecialFormatSt.executeQuery()){
            while (rs.next()){
                project projectFormat = new project();
                projectFormat.setReleaseDate(LocalDate.parse(rs.getString("release_date")));
                projectFormat.setProjectName(rs.getString("project_name"));
                projectFormat.setDeveloperCount(rs.getInt("developer_count"));
                result.add(projectFormat);
            }
            return result;
        }
    }

    private Project mapResultSet(ResultSet rs) throws SQLException {
        int projectId = rs.getInt("project_id");
        String projectName = rs.getString("project_name");
        String projectDescription = rs.getString("project_description");
        LocalDate releaseDate = LocalDate.parse(rs.getString("release_date"));
        int companiesId = rs.getInt("companies_id");
        int customerId = rs.getInt("customer_id");
        return new Project(projectId, projectName, projectDescription, releaseDate, companiesId, customerId);
    }

    private List<Project> getListOfProjects(ResultSet rs) throws SQLException {
        List<Project> result = new ArrayList<>();
        while (rs.next()){
            Project project = mapResultSet(rs);
            result.add(project);
        }
        return  result;
    }

    @EqualsAndHashCode
    @Data
    public static class project {
        private LocalDate releaseDate;
        private String projectName;
        private int developerCount;

        @Override
        public String toString() {
            return "Project{" +
                    "creationDate=" + releaseDate +
                    ", name='" + projectName + '\'' +
                    ", developerCount=" + developerCount +
                    '}';
        }
    }
}
