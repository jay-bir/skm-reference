package pl.edu.pjatk.simulator.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.parameters.P;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import pl.edu.pjatk.simulator.security.User;
import pl.edu.pjatk.simulator.security.UserService;

import javax.validation.Valid;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/users")
public class UserController {
    private UserService userService;

    public UserController(UserService userService){
        this.userService = userService;
    }

    @GetMapping
    public ResponseEntity <List<Map<String, Object>>> showUsers(){
        try {
            List<User> users = this.userService.findUsers();
            List<Map<String, Object>> payload = users.stream()
                    .map(user -> transformToDto().apply(user))
                    .collect(Collectors.toList());

            return new ResponseEntity<>(payload, HttpStatus.OK);
        }
        catch (Exception e){
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{username}")
    public ResponseEntity<Map<String, Object>> showSpecificUser(@PathVariable("username") String username){
        try {
            UserDetails user = userService.loadUserByUsername(username);
            Map<String, Object> payload = transformToDto().apply(user);
            return new ResponseEntity<>(payload, HttpStatus.OK);
        }
        catch (Exception e){
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @DeleteMapping("/{username}")
    public ResponseEntity<Void> deleteUserByUsername(@PathVariable("username") String username){
        try{
            userService.deleteUserByUsername(username);
            return new ResponseEntity<>(HttpStatus.ACCEPTED);
        }
        catch (Exception e){
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping
    public ResponseEntity<Void> createUser(@Valid @RequestBody User user){
        try{
            userService.addUser(user);
            return new ResponseEntity<>(HttpStatus.ACCEPTED);
        }
        catch (Exception e){
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @PutMapping
    public ResponseEntity<Void> updateUser(@Valid @RequestBody User user){
        try{
            userService.updateuser(user);
            return new ResponseEntity<>(HttpStatus.ACCEPTED);
        }
        catch (Exception e){
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public Function<UserDetails, Map<String, Object>> transformToDto(){
        return user -> {
            Map<String, Object> payload = new LinkedHashMap<>();
            payload.put("username:", user.getUsername());
            payload.put("authorities", user.getAuthorities().stream()
                    .map(GrantedAuthority::getAuthority)
                    .collect(Collectors.toList()));
            return payload;
        };
    }
}
