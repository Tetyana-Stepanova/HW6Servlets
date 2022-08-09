package models.project;

import models.Command;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.util.Map;
import java.util.Objects;

public class ProjectGetById implements Command {
    private final Connection connection;

    public ProjectGetById(Connection connection) {
        this.connection = connection;
    }
    @Override
    public void process(HttpServletRequest req, HttpServletResponse resp, TemplateEngine engine) throws IOException {
        if (!Objects.equals(req.getMethod(), "POST")) {
            resp.setContentType("text/html");

            Context simpleContext = new Context(
                    req.getLocale(),
                    Map.of("project", "", "errorMessage", "")
            );
            engine.process("project/project-get-by-id", simpleContext, resp.getWriter());
            resp.getWriter().close();
            return;
        }
        String id = req.getParameter("setId");
        Project project = null;
        String error = "";

        try {
            ProjectDaoService projectDaoService = new ProjectDaoService(connection);
            project = projectDaoService.getProjectById(Integer.parseInt(id));
        } catch (Exception e) {
            error = e.getMessage();
        }

        resp.setContentType("text/html");
        Context simpleContext = new Context(
                req.getLocale(),
                Map.of("project", project == null ? "null" : project, "errorMessage", error)
        );
        engine.process("project/project-get-by-id", simpleContext, resp.getWriter());
        resp.getWriter().close();
    }
}
