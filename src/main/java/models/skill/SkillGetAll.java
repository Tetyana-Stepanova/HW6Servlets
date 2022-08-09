package models.skill;

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

public class SkillGetAll implements Command {
    private final Connection connection;

    public SkillGetAll(Connection connection) {
        this.connection = connection;
    }
    @Override
    public void process(HttpServletRequest req, HttpServletResponse resp, TemplateEngine engine) throws IOException {
        if (!Objects.equals(req.getMethod(), "POST")) {
            resp.setContentType("text/html");

            Context simpleContext = new Context(
                    req.getLocale(),
                    Map.of("skills", "null", "errorMessage", "")
            );
            engine.process("skill/skill-getAll", simpleContext, resp.getWriter());
            resp.getWriter().close();
            return;
        }
        List<Skill> skills = new ArrayList<>();
        String error = "";

        try {
            SkillDaoService skillDaoService = new SkillDaoService(connection);
            skills = skillDaoService.getAllSkills();
        } catch (Exception e) {
            error = e.getMessage();
        }

        resp.setContentType("text/html");
        Context simpleContext = new Context(
                req.getLocale(),
                Map.of("skills", skills.size() == 0 ? "null" : skills,"errorMessage", error)
        );
        engine.process("skill/skill-getAll", simpleContext, resp.getWriter());
        resp.getWriter().close();
    }
}
