package models.project;

import models.Command;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.time.LocalDate;
import java.util.Map;
import java.util.Objects;

public class ProjectCreate implements Command {
    private final Connection connection;

    public ProjectCreate(Connection connection) {
        this.connection = connection;
    }
    @Override
    public void process(HttpServletRequest req, HttpServletResponse resp, TemplateEngine engine) throws IOException {
        if (!Objects.equals(req.getMethod(), "POST")) {
            resp.setContentType("text/html");

            Context simpleContext = new Context(
                    req.getLocale(),
                    Map.of("projectId", "", "errorMessage", "")
            );
            engine.process("project/project-create", simpleContext, resp.getWriter());
            resp.getWriter().close();
            return;
        }

        String setProjectName = req.getParameter("setProjectName");
        String setProjectDescription = req.getParameter("setProjectDescription");
        String setReleaseDate = req.getParameter("setReleaseDate");
        String setCompaniesId = req.getParameter("setCompaniesId");
        String setCustomerId = req.getParameter("setCustomerId");

        String id = "";
        String error = "";

        try {
            Project project = new Project();
            project.setProjectName(setProjectName);
            project.setProjectDescription(setProjectDescription);
            project.setReleaseDate(LocalDate.parse(setReleaseDate));
            project.setCompaniesId(Integer.parseInt(setCompaniesId));
            project.setCustomerId(Integer.parseInt(setCustomerId));

            ProjectDaoService projectDaoService = new ProjectDaoService(connection);
            id = String.valueOf(projectDaoService.createProject(project));
        } catch (Exception e) {
            error = e.getMessage();
        }

        resp.setContentType("text/html");
        Context simpleContext = new Context(
                req.getLocale(),
                Map.of("projectId", id, "errorMessage", error)
        );

        engine.process("project/project-create", simpleContext, resp.getWriter());
        resp.getWriter().close();
    }
}
