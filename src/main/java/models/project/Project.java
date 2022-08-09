package models.project;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Project {
    private int projectId;
    private String projectName;
    private String projectDescription;
    private LocalDate releaseDate;
    private int companiesId;
    private int customerId;
}
