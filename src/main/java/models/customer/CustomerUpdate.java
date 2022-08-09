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

public class CustomerUpdate implements Command {
    private final Connection connection;

    public CustomerUpdate(Connection connection) {
        this.connection = connection;
    }
    @Override
    public void process(HttpServletRequest req, HttpServletResponse resp, TemplateEngine engine) throws IOException {
        if (!Objects.equals(req.getMethod(), "POST")) {
            resp.setContentType("text/html");

            Context simpleContext = new Context(
                    req.getLocale(),
                    Map.of("errorMessage", "")
            );
            engine.process("customer/customer-update", simpleContext, resp.getWriter());
            resp.getWriter().close();
            return;
        }

        String setId = req.getParameter("setId");
        String setName = req.getParameter("setName");
        String setDescription = req.getParameter("setCustomerDescription");

        String error = "";

        try {
            Customer customer = new Customer();
            customer.setCustomerId(Integer.parseInt(setId));
            customer.setCustomersName(setName);
            customer.setCustomersDescriptions(setDescription);

            CustomerDaoService customerDaoService = new CustomerDaoService(connection);
            error = customerDaoService.updateCustomer(customer);
        } catch (Exception e) {
            error = e.getMessage();
        }

        resp.setContentType("text/html");
        Context simpleContext = new Context(
                req.getLocale(),
                Map.of("errorMessage", error)
        );

        engine.process("customer/customer-update", simpleContext, resp.getWriter());
        resp.getWriter().close();
    }
}
