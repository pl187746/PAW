package pl.iis.paw.trello.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import pl.iis.paw.trello.domain.User;
import pl.iis.paw.trello.web.viewmodel.LoginVM;
import pl.iis.paw.trello.web.viewmodel.RegisterVM;

import java.net.URI;
import java.net.URISyntaxException;

@RestController
public class AccountController {

    private UserService userService;

    @Autowired
    public AccountController(UserService userService) {
        this.userService = userService;
    }

    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public ResponseEntity<?> register(@RequestBody RegisterVM registerVM) throws URISyntaxException {
        User registeredUser = userService.registerUser(registerVM);

        return ResponseEntity
            .created(new URI("/users/" + registeredUser.getId()))
            .body(registeredUser);
    }

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public ResponseEntity<?> authenticate(@RequestBody LoginVM loginVm) {
        User user = userService.findUserByLogin(loginVm.getLogin());

        if (user != null && loginVm.getPassword().equals(user.getPassword())) {
            return ResponseEntity.ok().body(user);
        } else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
    }
}
