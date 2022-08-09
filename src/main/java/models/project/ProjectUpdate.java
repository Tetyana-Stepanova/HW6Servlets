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

public class ProjectUpdate implements Command {
    private final Connection connection;

    public ProjectUpdate(Connection connection) {
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
            engine.process("project/project-update", simpleContext, resp.getWriter());
            resp.getWriter().close();
            return;
        }

        String setId = req.getParameter("setId");
        String setProjectName = req.getParameter("setProjectName");
        String setProjectDescription = req.getParameter("setProjectDescription");
        String setReleaseDate = req.getParameter("setReleaseDate");
        String setCompaniesId = req.getParameter("setCompaniesId");
        String setCustomerId = req.getParameter("setCustomerId");

        String error = "";

        try {
            Project project = new Project();
            project.setProjectId(Integer.parseInt(setId));
            project.setProjectName(setProjectName);
            project.setProjectDescription(setProjectDescription);
            project.setReleaseDate(LocalDate.parse(setReleaseDate));
            project.setCompaniesId(Integer.parseInt(setCompaniesId));
            project.setCustomerId(Integer.parseInt(setCustomerId));

            ProjectDaoService projectDaoService = new ProjectDaoService(connection);
            error = projectDaoService.updateProject(project);
        } catch (Exception e) {
            error = e.getMessage();
        }

        resp.setContentType("text/html");
        Context simpleContext = new Context(
                req.getLocale(),
                Map.of("errorMessage", error)
        );

        engine.process("project/project-update", simpleContext, resp.getWriter());
        resp.getWriter().close();
    }
}
