package validation;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class UserValidationApplication {
    public static void main(String[] args) {
        SpringApplication.run(UserValidationApplication.class, args);
    }
}

/*
curl -i -X POST http://localhost:8080/users \
  -H "Content-Type: application/json" \
  -d '{"name": "A", "birthDate": "2000-01-01"}'
 */