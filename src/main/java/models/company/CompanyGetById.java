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

public class CompanyGetById implements Command {
    private final Connection connection;

    public CompanyGetById(Connection connection) {
        this.connection = connection;
    }
    @Override
    public void process(HttpServletRequest req, HttpServletResponse resp, TemplateEngine engine) throws IOException {
        if (!Objects.equals(req.getMethod(), "POST")) {
            resp.setContentType("text/html");

            Context simpleContext = new Context(
                    req.getLocale(),
                    Map.of("company", "", "errorMessage", "")
            );
            engine.process("company/company-get-by-id", simpleContext, resp.getWriter());
            resp.getWriter().close();
            return;
        }
        String id = req.getParameter("setId");
        Company company = null;
        String error = "";

        try {
            CompanyDaoService companyDaoService = new CompanyDaoService(connection);
            company = companyDaoService.getCompanyById(Integer.parseInt(id));
        } catch (Exception e) {
            error = e.getMessage();
        }

        resp.setContentType("text/html");
        Context simpleContext = new Context(
                req.getLocale(),
                Map.of("company", company == null ? "null" :
                        company.getCompaniesId() == 0 ? "null" : company, "errorMessage", error)
        );
        engine.process("company/company-get-by-id", simpleContext, resp.getWriter());
        resp.getWriter().close();
    }
}
