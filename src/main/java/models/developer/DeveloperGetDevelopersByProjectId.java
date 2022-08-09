package models.developer;

import models.Command;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class DeveloperGetDevelopersByProjectId implements Command {
    private final Connection connection;

    public DeveloperGetDevelopersByProjectId(Connection connection) {
        this.connection = connection;
    }
    @Override
    public void process(HttpServletRequest req, HttpServletResponse resp, TemplateEngine engine) throws IOException {
        if (!Objects.equals(req.getMethod(), "POST")) {
            resp.setContentType("text/html");

            Context simpleContext = new Context(
                    req.getLocale(),
                    Map.of("developers", "null", "errorMessage", "")
            );
            engine.process("developer/developer-getDevByProjectId", simpleContext, resp.getWriter());
            resp.getWriter().close();
            return;
        }
        String setProjectId = req.getParameter("setProjectId");
        List<Developer> developers = null;
        String error = "";

        try {
            DeveloperDaoService developerDaoService = new DeveloperDaoService(connection);
            developers = developerDaoService.getDevelopersByProject(Integer.parseInt(setProjectId));
        } catch (Exception e) {
            error = e.getMessage();
        }

        resp.setContentType("text/html");
        Context simpleContext = new Context(
                req.getLocale(),
                Map.of("developers", developers == null ? "null" : developers, "errorMessage", error)
        );
        engine.process("developer/developer-getDevByProjectId", simpleContext, resp.getWriter());
        resp.getWriter().close();
    }
}
