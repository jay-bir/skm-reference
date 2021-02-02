package pl.edu.pjatk.simulator.security;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import pl.edu.pjatk.simulator.repository.UserRepository;


import java.util.List;
import java.util.NoSuchElementException;

import java.util.stream.Collectors;

@Service
public class UserService implements UserDetailsService {
    private final PasswordEncoder passwordEncoder;
    private  UserRepository userRepository;

    public UserService(UserRepository userRepository){
        this.userRepository = userRepository;
        this.passwordEncoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }



    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findById(username).orElse(null);
    }

    public List<User> findUsers(){
        return this.userRepository.findAll();
    }

    public PasswordEncoder getPasswordEncoder() {
        return passwordEncoder;
    }

    public void deleteUserByUsername(String username){
        if(userRepository.findById(username).isEmpty()) throw new NoSuchElementException();
        userRepository.deleteById(username);
    }

    public void addUser(User user){
        if(userRepository.findById(user.getUsername()).isEmpty()){
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            userRepository.save(user);
        }
        else throw new IllegalArgumentException();
    }

    public void updateuser(User user) {
        User toBeUpdated = userRepository.findById(user.getUsername()).orElseThrow(NoSuchElementException::new);
        toBeUpdated.setPassword(passwordEncoder.encode(user.getPassword()));
        toBeUpdated.setAuthorities(user.getAuthorities().stream()
        .map(GrantedAuthority::getAuthority)
        .collect(Collectors.toList()));

        userRepository.save(toBeUpdated);
    }
}
