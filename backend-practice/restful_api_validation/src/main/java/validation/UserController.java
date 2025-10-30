package validation;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
//import jakarta.validation.Valid;
import jakarta.validation.Valid;

@RestController
public class UserController {

    @PostMapping("/users")
    public ResponseEntity<Object> createUser(@Valid @RequestBody User user) {
        System.out.println("User received: " + user.getName());
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}
