package models.developer;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Developer {
    private int developersId;
    private String firstName;
    private String lastName;
    private int age;
    private String gender;
    private String city;
    private Integer salary;
    private int companiesId;
}
