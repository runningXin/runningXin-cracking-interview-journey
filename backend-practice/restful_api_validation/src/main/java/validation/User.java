package validation;

import jakarta.validation.constraints.Size;
import java.util.Date;

public class User {

    private int id;

    @Size(min = 2, message = "Name should have at least 2 characters")
    private String name;

    private Date birthDate;

    // Constructors
    public User() {}

    public User(int id, String name, Date birthDate) {
        this.id = id;
        this.name = name;
        this.birthDate = birthDate;
    }

    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public Date getBirthDate() { return birthDate; }
    public void setBirthDate(Date birthDate) { this.birthDate = birthDate; }
}
