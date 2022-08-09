package models.customer;

import models.Command;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.util.Map;
import java.util.Objects;

public class CustomerCreate implements Command {
    private final Connection connection;

    public CustomerCreate(Connection connection) {
        this.connection = connection;
    }
    @Override
    public void process(HttpServletRequest req, HttpServletResponse resp, TemplateEngine engine) throws IOException {
        if (!Objects.equals(req.getMethod(), "POST")) {
            resp.setContentType("text/html");

            Context simpleContext = new Context(
                    req.getLocale(),
                    Map.of("customerId", "", "errorMessage", "")
            );
            engine.process("customer/customer-create", simpleContext, resp.getWriter());
            resp.getWriter().close();
            return;
        }
        String setName = req.getParameter("setName");
        String setCustomerDescription = req.getParameter("setCustomerDescription");

        String id = "";
        String error = "";

        try {
            Customer customer = new Customer();
            customer.setCustomersName(setName);
            customer.setCustomersDescriptions(setCustomerDescription);

            CustomerDaoService customerDaoService = new CustomerDaoService(connection);
            id = String.valueOf(customerDaoService.createCustomer(customer));
        } catch (Exception e) {
            error = e.getMessage();
        }

        resp.setContentType("text/html");
        Context simpleContext = new Context(
                req.getLocale(),
                Map.of("customerId", id, "errorMessage", error)
        );

        engine.process("customer/customer-create", simpleContext, resp.getWriter());
        resp.getWriter().close();
    }
}
