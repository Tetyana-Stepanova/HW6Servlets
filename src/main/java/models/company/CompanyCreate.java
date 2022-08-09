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

public class CompanyCreate implements Command {
    private final Connection connection;

    public CompanyCreate(Connection connection) {
        this.connection = connection;
    }
    @Override
    public void process(HttpServletRequest req, HttpServletResponse resp, TemplateEngine engine) throws IOException {
        if (!Objects.equals(req.getMethod(), "POST")) {
            resp.setContentType("text/html");

            Context simpleContext = new Context(
                    req.getLocale(),
                    Map.of("companyId", "", "errorMessage", "")
            );
            engine.process("company/company-create", simpleContext, resp.getWriter());
            resp.getWriter().close();
            return;
        }
        String setName = req.getParameter("setName");
        String setDescription = req.getParameter("setDescription");

        String id = "";
        String error = "";

        try {
            Company company = new Company();
            company.setCompaniesName(setName);
            company.setCompaniesDescription(setDescription);

            CompanyDaoService companyDaoService = new CompanyDaoService(connection);
            id = String.valueOf(companyDaoService.createCompany(company));
        } catch (Exception e) {
            error = e.getMessage();
        }

        resp.setContentType("text/html");
        Context simpleContext = new Context(
                req.getLocale(),
                Map.of("companyId", id, "errorMessage", error)
        );

        engine.process("company/company-create", simpleContext, resp.getWriter());
        resp.getWriter().close();
    }
}
