package models.developer;

import models.Command;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.util.Map;
import java.util.Objects;

public class DeveloperCreate implements Command {
    private final Connection connection;

    public DeveloperCreate(Connection connection) {
        this.connection = connection;
    }
    @Override
    public void process(HttpServletRequest req, HttpServletResponse resp, TemplateEngine engine) throws IOException {
        if (!Objects.equals(req.getMethod(), "POST")) {
            resp.setContentType("text/html");

            Context simpleContext = new Context(
                    req.getLocale(),
                    Map.of("developerId", "", "errorMessage", "")
            );
            engine.process("developer/developer-create", simpleContext, resp.getWriter());
            resp.getWriter().close();
            return;
        }
        String setFirstName = req.getParameter("setFirstName");
        String setLastName = req.getParameter("setLastName");
        String setAge = req.getParameter("setAge");
        String setGender = req.getParameter("setGender");
        String setCity = req.getParameter("setCity");
        String setSalary = req.getParameter("setSalary");
        String setCompaniesId = req.getParameter("setCompaniesId");

        String id = "";
        String error = "";

        try {
            Developer developer = new Developer();
            developer.setFirstName(setFirstName);
            developer.setLastName(setLastName);
            developer.setAge(Integer.parseInt(setAge));
            developer.setGender(setGender);
            developer.setCity(setCity);
            developer.setSalary(Integer.parseInt(setSalary));
            developer.setCompaniesId(Integer.parseInt(setCompaniesId));

            DeveloperDaoService developerDaoService = new DeveloperDaoService(connection);
            id = String.valueOf(developerDaoService.createDeveloper(developer));
        } catch (Exception e) {
            error = e.getMessage();
        }

        resp.setContentType("text/html");
        Context simpleContext = new Context(
                req.getLocale(),
                Map.of("developerId", id, "errorMessage", error)
        );

        engine.process("developer/developer-create", simpleContext, resp.getWriter());
        resp.getWriter().close();
    }
}
