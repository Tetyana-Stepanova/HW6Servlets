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

public class CustomerGetById implements Command {
    private final Connection connection;

    public CustomerGetById(Connection connection) {
        this.connection = connection;
    }
    @Override
    public void process(HttpServletRequest req, HttpServletResponse resp, TemplateEngine engine) throws IOException {
        if (!Objects.equals(req.getMethod(), "POST")) {
            resp.setContentType("text/html");

            Context simpleContext = new Context(
                    req.getLocale(),
                    Map.of("customer", "", "errorMessage", "")
            );
            engine.process("customer/customer-get-by-id", simpleContext, resp.getWriter());
            resp.getWriter().close();
            return;
        }
        String id = req.getParameter("setId");
        Customer customer = null;
        String error = "";

        try {
            CustomerDaoService customerDaoService = new CustomerDaoService(connection);
            customer = customerDaoService.getCustomerById(Integer.parseInt(id));
        } catch (Exception e) {
            error = e.getMessage();
        }

        resp.setContentType("text/html");
        Context simpleContext = new Context(
                req.getLocale(),
                Map.of("customer", customer == null ? "null" : customer, "errorMessage", error)
        );
        engine.process("customer/customer-get-by-id", simpleContext, resp.getWriter());
        resp.getWriter().close();
    }
}
