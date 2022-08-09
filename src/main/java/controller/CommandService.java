package controller;

import models.Command;
import models.TableManagement;
import models.company.*;
import models.customer.*;
import models.developer.*;
import models.project.*;
import models.skill.*;
import org.thymeleaf.TemplateEngine;
import storage.StorageConstants;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.HashMap;
import java.util.Map;

public class CommandService {
    private Connection connection;
    private final Map<String, Command> commands;

    public CommandService(){
        try {
            DriverManager.registerDriver(new com.mysql.cj.jdbc.Driver());
            connection = DriverManager.getConnection(StorageConstants.CONNECTION_URL,
                    StorageConstants.USERNAME,
                    StorageConstants.PASSWORD);
        }catch (Exception e) {
            e.printStackTrace();
        }
        commands = new HashMap<>();

        commands.put("GET /", new TableManagement());

        commands.put("GET /company", new CompanyImpl());
        commands.put("GET /company/create", new CompanyCreate(connection));
        commands.put("POST /company/create", new CompanyCreate(connection));
        commands.put("GET /company/get-by-id", new CompanyGetById(connection));
        commands.put("POST /company/get-by-id", new CompanyGetById(connection));
        commands.put("GET /company/getAll", new CompanyGetAll(connection));
        commands.put("POST /company/getAll", new CompanyGetAll(connection));
        commands.put("GET /company/update", new CompanyUpdate(connection));
        commands.put("POST /company/update", new CompanyUpdate(connection));
        commands.put("GET /company/delete", new CompanyDeleteById(connection));
        commands.put("POST /company/delete", new CompanyDeleteById(connection));

        commands.put("GET /customer", new CustomerImpl());
        commands.put("GET /customer/create", new CustomerCreate(connection));
        commands.put("POST /customer/create", new CustomerCreate(connection));
        commands.put("GET /customer/delete", new CustomerDeleteById(connection));
        commands.put("POST /customer/delete", new CustomerDeleteById(connection));
        commands.put("GET /customer/getAll", new CustomerGetAll(connection));
        commands.put("POST /customer/getAll", new CustomerGetAll(connection));
        commands.put("GET /customer/get-by-id", new CustomerGetById(connection));
        commands.put("POST /customer/get-by-id", new CustomerGetById(connection));
        commands.put("GET /customer/update", new CustomerUpdate(connection));
        commands.put("POST /customer/update", new CustomerUpdate(connection));

        commands.put("GET /skill", new SkillImpl());
        commands.put("GET /skill/create", new SkillCreate(connection));
        commands.put("POST /skill/create", new SkillCreate(connection));
        commands.put("GET /skill/delete", new SkillDeleteById(connection));
        commands.put("POST /skill/delete", new SkillDeleteById(connection));
        commands.put("GET /skill/getAll", new SkillGetAll(connection));
        commands.put("POST /skill/getAll", new SkillGetAll(connection));
        commands.put("GET /skill/get-by-id", new SkillGetById(connection));
        commands.put("POST /skill/get-by-id", new SkillGetById(connection));
        commands.put("GET /skill/update", new SkillUpdate(connection));
        commands.put("POST /skill/update", new SkillUpdate(connection));

        commands.put("GET /developer", new DeveloperImpl());
        commands.put("GET /developer/create", new DeveloperCreate(connection));
        commands.put("POST /developer/create", new DeveloperCreate(connection));
        commands.put("GET /developer/get-by-id", new DeveloperGetById(connection));
        commands.put("POST /developer/get-by-id", new DeveloperGetById(connection));
        commands.put("GET /developer/getAll", new DeveloperGetAll(connection));
        commands.put("POST /developer/getAll", new DeveloperGetAll(connection));
        commands.put("GET /developer/update", new DeveloperUpdate(connection));
        commands.put("POST /developer/update", new DeveloperUpdate(connection));
        commands.put("GET /developer/delete", new DeveloperDeleteById(connection));
        commands.put("POST /developer/delete", new DeveloperDeleteById(connection));
        commands.put("GET /developer/getByDevLanguage", new DeveloperGetByLanguage(connection));
        commands.put("POST /developer/getByDevLanguage", new DeveloperGetByLanguage(connection));
        commands.put("GET /developer/getBySkillLevel", new DeveloperGetBySkillLevel(connection));
        commands.put("POST /developer/getBySkillLevel", new DeveloperGetBySkillLevel(connection));
        commands.put("GET /developer/getDevByProjectId", new DeveloperGetDevelopersByProjectId(connection));
        commands.put("POST /developer/getDevByProjectId", new DeveloperGetDevelopersByProjectId(connection));

        commands.put("GET /project", new ProjectImpl());
        commands.put("GET /project/create", new ProjectCreate(connection));
        commands.put("POST /project/create", new ProjectCreate(connection));
        commands.put("GET /project/delete", new ProjectDeleteById(connection));
        commands.put("POST /project/delete", new ProjectDeleteById(connection));
        commands.put("GET /project/getAll", new ProjectGetAll(connection));
        commands.put("POST /project/getAll", new ProjectGetAll(connection));
        commands.put("GET /project/get-by-id", new ProjectGetById(connection));
        commands.put("POST /project/get-by-id", new ProjectGetById(connection));
        commands.put("GET /project/update", new ProjectUpdate(connection));
        commands.put("POST /project/update", new ProjectUpdate(connection));
        commands.put("GET /project/getSalaryById", new ProjectGetDevSalaryByProjectId(connection));
        commands.put("POST /project/getSalaryById", new ProjectGetDevSalaryByProjectId(connection));
        commands.put("GET /project/getInFormat", new ProjectGetInFormat(connection));
        commands.put("POST /project/getInFormat", new ProjectGetInFormat(connection));
    }

    public void process(HttpServletRequest req, HttpServletResponse resp, TemplateEngine engine) throws IOException {
        String requestUri = req.getRequestURI();
        String commandKey = req.getMethod() + " " + requestUri;

        commands.get(commandKey).process(req, resp, engine);
    }
}
