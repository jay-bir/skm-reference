package pl.edu.pjatk.simulator.service;


import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import pl.edu.pjatk.simulator.repository.UserRepository;
import pl.edu.pjatk.simulator.security.User;
import pl.edu.pjatk.simulator.security.UserService;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest
public class UserServiceTest {

    @Mock
    UserRepository userRepository;


    @Test
    public void findUserByUsernameTestWithExistingUser(){
        String username = "user";
        String password = "password";
        List<String> authorities = List.of("ROLE_USER");

        User user = new User(username,password,authorities);

        when(userRepository.findById(username)).thenReturn(Optional.of(user));

        UserService userService = new UserService(userRepository);
        UserDetails found = userService.loadUserByUsername(username);

        verify(userRepository).findById(username);
        Assertions.assertEquals(user.getUsername(),found.getUsername());
        Assertions.assertEquals(user.getPassword(), found.getPassword());
        Assertions.assertIterableEquals(authorities, found.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList()));
    }

    @Test
    public void findUserByUsernameWithNullUser(){
        String username = "user";

        when(userRepository.findById(username)).thenReturn(Optional.empty());

        UserService userService = new UserService(userRepository);
        UserDetails found = userService.loadUserByUsername(username);

        verify(userRepository).findById(username);
        Assertions.assertNull(found);
    }

    @Test
    public void deleteUserByIdWithNullUser(){
        String username = "user";

        when(userRepository.findById(username)).thenReturn(Optional.empty());

        UserService userService = new UserService(userRepository);
        Assertions.assertThrows(NoSuchElementException.class,() -> userService.deleteUserByUsername(username));
    }

    @Test
    public void deleteUserByIdWithExistingUser(){
        String username = "user";

        when(userRepository.findById(username)).thenReturn(Optional.of(new User("username","b", List.of("empty"))));

        UserService userService = new UserService(userRepository);
        userService.deleteUserByUsername(username);

        verify(userRepository).deleteById(username);
    }

    @Test
    public void addUser(){
        String username = "user";
        String password = "password";
        List<String> authorities = List.of("ROLE_USER");

        User user = new User(username, password, authorities);
        when(userRepository.findById(username)).thenReturn(Optional.empty());

        UserService userService = new UserService(userRepository);
        userService.addUser(user);
        verify(userRepository).save(user);
    }

    @Test
    public void addUserExpectedException(){
        String username = "user";
        String password = "password";
        List<String> authorities = List.of("ROLE_USER");

        User user = new User(username, password, authorities);
        when(userRepository.findById(username)).thenReturn(Optional.of(user));

        UserService userService = new UserService(userRepository);
        Assertions.assertThrows(IllegalArgumentException.class, () -> userService.addUser(user));
    }

    @Test
    public void updateUserWithNoUser(){
        String username = "user";
        String password = "password";
        List<String> authorities = List.of("ROLE_USER");

        User user = new User(username, password, authorities);
        when(userRepository.findById(username)).thenReturn(Optional.empty());

        UserService userService = new UserService(userRepository);
        Assertions.assertThrows(NoSuchElementException.class, () -> userService.updateuser(user));
    }

    @Test
    public void updateUserTest(){
        String username = "user";
        String password = "password";
        List<String> authorities = List.of("ROLE_USER");
        User user = new User(username, password, authorities);

        String oldPassword = "oldPassword";
        List<String> oldAuthorities = List.of("empty");
        User userInDb = new User(username, password, oldAuthorities);

        when(userRepository.findById(username)).thenReturn(Optional.of(userInDb));

        UserService userService = new UserService(userRepository);
        userService.updateuser(user);
        verify(userRepository).save(userInDb);

        Assertions.assertIterableEquals(
                user.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList()),
                userInDb.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList()));
        Assertions.assertNotEquals(oldPassword, userInDb.getPassword());
    }

}
