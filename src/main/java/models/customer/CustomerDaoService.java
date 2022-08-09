package models.customer;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CustomerDaoService {
    private final PreparedStatement createCustomerSt;
    private final PreparedStatement getMaxIdSt;
    private final PreparedStatement getCustomerByIdSt;
    private final PreparedStatement getAllCustomersSt;
    private final PreparedStatement updateCustomerSt;
    private final PreparedStatement deleteCustomerByIdSt;

    public CustomerDaoService(Connection connection) throws SQLException {
        getMaxIdSt = connection.prepareStatement("SELECT max(customer_id) AS maxId FROM customers");
        createCustomerSt = connection.prepareStatement("INSERT INTO customers (customers_name, customers_descriptions) " +
                "VALUES(?, ?)");
        getCustomerByIdSt = connection.prepareStatement("SELECT customers.* FROM customers WHERE customer_id = ?");
        getAllCustomersSt = connection.prepareStatement("SELECT customer_id, customers_name, customers_descriptions " +
                "FROM customers");
        updateCustomerSt = connection.prepareStatement("UPDATE customers " +
                "SET customers_name = ?, customers_descriptions = ? WHERE customer_id = ?");
        deleteCustomerByIdSt = connection.prepareStatement("DELETE FROM customers WHERE customer_id = ?");
    }

    public int createCustomer(Customer customer) throws SQLException {
        createCustomerSt.setString(1, customer.getCustomersName());
        createCustomerSt.setString(2, customer.getCustomersDescriptions());
        createCustomerSt.executeUpdate();
        int id;
        try (ResultSet rs = getMaxIdSt.executeQuery()) {
            rs.next();
            id = rs.getInt("maxId");
        }
        return id;
    }

    public Customer getCustomerById(int id) throws SQLException {
        getCustomerByIdSt.setInt(1, id);
        try(ResultSet rs = getCustomerByIdSt.executeQuery()){
            if (!rs.next()){
                return null;
            }
            Customer customer = mapResultSet(rs);
            return customer;
        }
    }

    public List<Customer> getAllCustomers() throws SQLException {
        try(ResultSet rs = getAllCustomersSt.executeQuery()){
            return getListOfCustomers(rs);
        }
    }

    public String updateCustomer(Customer customer) throws SQLException {
        try {
            updateCustomerSt.setString(1, customer.getCustomersName());
            updateCustomerSt.setString(2, customer.getCustomersDescriptions());
            updateCustomerSt.setInt(3, customer.getCustomerId());
            updateCustomerSt.executeUpdate();
            return "true";
        }catch (SQLException e) {
            return e.getMessage();
        }
    }

    public String deleteCustomerById(int id) throws SQLException {
        try {
            deleteCustomerByIdSt.setInt(1, id);
            deleteCustomerByIdSt.executeUpdate();
            return "true";
        }catch (SQLException e) {
            return e.getMessage();
        }
    }

    private Customer mapResultSet(ResultSet rs) throws SQLException {
        int customerId = rs.getInt("customer_id");
        String customersName = rs.getString("customers_name");
        String customersDescriptions = rs.getString("customers_descriptions");
        return new Customer(customerId, customersName, customersDescriptions);
    }

    private List<Customer> getListOfCustomers(ResultSet rs) throws SQLException {
        List<Customer> result = new ArrayList<>();
        while (rs.next()){
            Customer customer = mapResultSet(rs);
            result.add(customer);
        }
        return result;
    }
}
