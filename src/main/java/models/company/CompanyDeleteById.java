package models.company;

import models.Command;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.util.Map;
import java.util.Objects;

public class CompanyDeleteById implements Command {
    private final Connection connection;

    public CompanyDeleteById(Connection connection) {
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
            engine.process("company/company-delete", simpleContext, resp.getWriter());
            resp.getWriter().close();
            return;
        }
        String setId = req.getParameter("setId");

        String error = "";

        try {
            CompanyDaoService companyDaoService = new CompanyDaoService(connection);
            error = companyDaoService.deleteCompanyById(Integer.parseInt(setId));
        } catch (Exception e) {
            error = e.getMessage();
        }

        resp.setContentType("text/html");
        Context simpleContext = new Context(
                req.getLocale(),
                Map.of("errorMessage", error)
        );

        engine.process("company/company-delete", simpleContext, resp.getWriter());
        resp.getWriter().close();
    }
}
