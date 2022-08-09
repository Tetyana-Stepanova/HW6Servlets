package models.customer;

import models.Command;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class CustomerGetAll implements Command {
    private final Connection connection;

    public CustomerGetAll(Connection connection) {
        this.connection = connection;
    }
    @Override
    public void process(HttpServletRequest req, HttpServletResponse resp, TemplateEngine engine) throws IOException {
        if (!Objects.equals(req.getMethod(), "POST")) {
            resp.setContentType("text/html");

            Context simpleContext = new Context(
                    req.getLocale(),
                    Map.of("customers", "null", "errorMessage", "")
            );
            engine.process("customer/customer-getAll", simpleContext, resp.getWriter());
            resp.getWriter().close();
            return;
        }
        List<Customer> customers = new ArrayList<>();
        String error = "";

        try {
            CustomerDaoService customerDaoService = new CustomerDaoService(connection);
            customers = customerDaoService.getAllCustomers();
        } catch (Exception e) {
            error = e.getMessage();
        }

        resp.setContentType("text/html");
        Context simpleContext = new Context(
                req.getLocale(),
                Map.of("customers", customers.size() == 0 ? "null" : customers,"errorMessage", error)
        );
        engine.process("customer/customer-getAll", simpleContext, resp.getWriter());
        resp.getWriter().close();
    }
}
