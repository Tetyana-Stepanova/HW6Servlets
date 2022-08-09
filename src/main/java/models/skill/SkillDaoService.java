package models.skill;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class SkillDaoService {
    private final PreparedStatement getMaxIdSt;
    private final PreparedStatement createSkillSt;
    private final PreparedStatement getSkillByIdSt;
    private final PreparedStatement getAllSkillsSt;
    private final PreparedStatement updateSkillSt;
    private final PreparedStatement deleteSkillSt;

    public SkillDaoService(Connection connection) throws SQLException {
        getMaxIdSt = connection.prepareStatement("SELECT max(skills_id) AS maxId FROM skills");
        createSkillSt = connection.
                prepareStatement("INSERT INTO skills (dev_language, skill_level) VALUES (?, ?)");
        getSkillByIdSt = connection.
                prepareStatement("SELECT skills.* FROM skills WHERE skills_id = ? ");
        getAllSkillsSt = connection.
                prepareStatement("SELECT skills_id, dev_language, skill_level FROM skills");

        updateSkillSt = connection.
                prepareStatement("UPDATE skills SET dev_language = ?, skill_level = ? WHERE skills_id = ?");
        deleteSkillSt = connection.
                prepareStatement("DELETE FROM skills WHERE skills_id = ?");
    }

    public int createSkill(Skill skill) throws SQLException {
        createSkillSt.setString(1, skill.getDevLanguage());
        createSkillSt.setString(2, skill.getSkillLevel());
        createSkillSt.executeUpdate();
        int id;
        try (ResultSet rs = getMaxIdSt.executeQuery()) {
            rs.next();
            id = rs.getInt("maxId");
        }
        return id;
    }

    public Skill getSkillById(int id) throws SQLException {
        getSkillByIdSt.setInt(1, id);
        try(ResultSet rs = getSkillByIdSt.executeQuery()){
            if (!rs.next()){
                return null;
            }
            Skill skill = mapResultSet(rs);
            return skill;
        }
    }

    public List<Skill> getAllSkills() throws SQLException {
        try(ResultSet rs = getAllSkillsSt.executeQuery()){
            return getListOfSkills(rs);
        }
    }

    public String updateSkill(Skill skill) throws SQLException {
        try {
            updateSkillSt.setString(1, skill.getDevLanguage());
            updateSkillSt.setString(2, skill.getSkillLevel());
            updateSkillSt.setInt(3, skill.getSkillsId());
            updateSkillSt.executeUpdate();
            return "true";
        }catch (SQLException e) {
            return e.getMessage();
        }
    }

    public String deleteSkill(int id) throws SQLException {
        try {
            deleteSkillSt.setInt(1, id);
            deleteSkillSt.executeUpdate();
            return "true";
        }catch (SQLException e) {
            return e.getMessage();
        }
    }

    private Skill mapResultSet(ResultSet rs) throws SQLException {
        int skillsId = rs.getInt("skills_id");
        String devLanguage = rs.getString("dev_language");
        String skillLevel = rs.getString("skill_level");
        return new Skill(skillsId, devLanguage, skillLevel);
    }

    private List<Skill> getListOfSkills(ResultSet rs) throws SQLException {
        List<Skill> result = new ArrayList<>();
        while (rs.next()){
            Skill skill = mapResultSet(rs);
            result.add(skill);
        }
        return  result;
    }
}
