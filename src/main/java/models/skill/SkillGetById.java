package models.skill;

import models.Command;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.util.Map;
import java.util.Objects;

public class SkillGetById implements Command {
    private final Connection connection;

    public SkillGetById(Connection connection) {
        this.connection = connection;
    }
    @Override
    public void process(HttpServletRequest req, HttpServletResponse resp, TemplateEngine engine) throws IOException {
        if (!Objects.equals(req.getMethod(), "POST")) {
            resp.setContentType("text/html");

            Context simpleContext = new Context(
                    req.getLocale(),
                    Map.of("skill", "", "errorMessage", "")
            );
            engine.process("skill/skill-get-by-id", simpleContext, resp.getWriter());
            resp.getWriter().close();
            return;
        }
        String id = req.getParameter("setId");
        Skill skill = null;
        String error = "";

        try {
            SkillDaoService skillDaoService = new SkillDaoService(connection);
            skill = skillDaoService.getSkillById(Integer.parseInt(id));
        } catch (Exception e) {
            error = e.getMessage();
        }

        resp.setContentType("text/html");
        Context simpleContext = new Context(
                req.getLocale(),
                Map.of("skill", skill == null ? "null" : skill, "errorMessage", error)
        );
        engine.process("skill/skill-get-by-id", simpleContext, resp.getWriter());
        resp.getWriter().close();
    }
}
