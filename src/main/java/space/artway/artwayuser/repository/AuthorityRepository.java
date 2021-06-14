package space.artway.artwayuser.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import space.artway.artwayuser.domain.Authority;

public interface AuthorityRepository extends JpaRepository<String, Authority> {

}
