package recipes.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import recipes.Entity.Role;
import recipes.Entity.User;
import recipes.Repository.UserRepository;

import javax.validation.Valid;

@RestController
public class AuthenticationController {

    private final UserRepository userRepository;

    private final PasswordEncoder encoder;

    @Autowired
    public AuthenticationController(UserRepository userRepository, PasswordEncoder encoder) {
        this.userRepository = userRepository;
        this.encoder = encoder;
    }

    @PostMapping("/api/register")
    public ResponseEntity<?> register(@Valid @RequestBody User user) {
        if (userRepository.findUserByEmail(user.getUsername()) == null) {
            user.setRole(Role.USER);
            user.setPassword(encoder.encode(user.getPassword()));
            userRepository.save(user);
            return new ResponseEntity<>(HttpStatus.OK);
        } else
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @GetMapping("/details")
    public void details(Authentication auth) {
        UserDetails details = (UserDetails) auth.getPrincipal();
        System.out.println("Username: " + details.getUsername());
        System.out.println("User has authorities/roles: " + details.getAuthorities());
    }

    @GetMapping("/username")
    public void username(@AuthenticationPrincipal UserDetails details) {
        System.out.println(details.getUsername());
    }
}
