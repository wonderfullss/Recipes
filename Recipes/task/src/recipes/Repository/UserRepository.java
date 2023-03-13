package recipes.Repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import recipes.Entity.User;

@Repository
public interface UserRepository extends CrudRepository<User, Long> {
    User findUserByEmail(String email);
}
