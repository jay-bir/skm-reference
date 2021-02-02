package pl.edu.pjatk.simulator.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.edu.pjatk.simulator.security.User;

@Repository
public interface UserRepository extends JpaRepository<User, String> {
}
