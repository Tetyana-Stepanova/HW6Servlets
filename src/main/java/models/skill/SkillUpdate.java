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

public class SkillUpdate implements Command {
    private final Connection connection;

    public SkillUpdate(Connection connection) {
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
            engine.process("skill/skill-update", simpleContext, resp.getWriter());
            resp.getWriter().close();
            return;
        }

        String setId = req.getParameter("setId");
        String setDevLanguage = req.getParameter("setDevLanguage");
        String setSkillLevel = req.getParameter("setSkillLevel");

        String error = "";

        try {
            Skill skill = new Skill();
            skill.setSkillsId(Integer.parseInt(setId));
            skill.setDevLanguage(setDevLanguage);
            skill.setSkillLevel(setSkillLevel);

            SkillDaoService skillDaoService = new SkillDaoService(connection);
            error = skillDaoService.updateSkill(skill);
        } catch (Exception e) {
            error = e.getMessage();
        }

        resp.setContentType("text/html");
        Context simpleContext = new Context(
                req.getLocale(),
                Map.of("errorMessage", error)
        );

        engine.process("skill/skill-update", simpleContext, resp.getWriter());
        resp.getWriter().close();
    }
}
