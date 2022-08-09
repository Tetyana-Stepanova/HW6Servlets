package models.company;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CompanyDaoService {
    private final PreparedStatement createCompanySt;
    private final PreparedStatement getMaxIdSt;
    private final PreparedStatement getCompanyByIdSt;
    private final PreparedStatement getAllCompaniesSt;
    private final PreparedStatement updateCompanySt;
    private final PreparedStatement deleteCompanyByIdSt;

    public CompanyDaoService(Connection connection) throws SQLException {
        getMaxIdSt = connection.prepareStatement("SELECT max(companies_id) AS maxId FROM companies");
        createCompanySt = connection.prepareStatement("INSERT INTO companies (companies_name, companies_description) " +
                "VALUES (?, ?)");
        getCompanyByIdSt = connection.prepareStatement("SELECT companies.* FROM companies WHERE companies_id = ?");
        getAllCompaniesSt = connection.prepareStatement("SELECT companies_id, companies_name, companies_description " +
                "FROM companies");
        updateCompanySt = connection.prepareStatement("UPDATE companies " +
                "SET companies_name = ?, companies_description = ? WHERE companies_id = ?");
        deleteCompanyByIdSt = connection.prepareStatement("DELETE FROM companies WHERE companies_id = ?");
    }

    public int createCompany(Company company) throws SQLException {
        createCompanySt.setString(1, company.getCompaniesName());
        createCompanySt.setString(2, company.getCompaniesDescription());
        createCompanySt.executeUpdate();
        int id;
        try (ResultSet rs = getMaxIdSt.executeQuery()) {
            rs.next();
            id = rs.getInt("maxId");
        }
        return id;
    }

    public Company getCompanyById(int companyId) throws SQLException {
        getCompanyByIdSt.setInt(1, companyId);
        try(ResultSet rs = getCompanyByIdSt.executeQuery()){
            if (!rs.next()){
                return null;
            }
            Company company = mapResultSet(rs);
            return company;
        }
    }

    public List<Company> getAllCompanies() throws SQLException {
        try(ResultSet rs = getAllCompaniesSt.executeQuery()){
            return getListOfCompanies(rs);
        }
    }

    public String updateCompany(Company company) throws SQLException {
        try {
            updateCompanySt.setString(1, company.getCompaniesName());
            updateCompanySt.setString(2, company.getCompaniesDescription());
            updateCompanySt.setInt(3, company.getCompaniesId());
            updateCompanySt.executeUpdate();
            return "true";
        }catch (SQLException e) {
            return e.getMessage();
        }
    }

    public String deleteCompanyById(int id) throws SQLException {
        try {
            deleteCompanyByIdSt.setInt(1, id);
            deleteCompanyByIdSt.executeUpdate();
            return "true";
        }catch (SQLException e) {
            return e.getMessage();
        }
    }

    private Company mapResultSet(ResultSet rs) throws SQLException {
        int companiesId = rs.getInt("companies_id");
        String companiesName = rs.getString("companies_name");
        String companiesDescription = rs.getString("companies_description");
        return new Company(companiesId, companiesName, companiesDescription);
    }

    private List<Company> getListOfCompanies(ResultSet rs) throws SQLException {
        List<Company> result = new ArrayList<>();
        while (rs.next()){
            Company company = mapResultSet(rs);
            result.add(company);
        }
        return  result;
    }
}
