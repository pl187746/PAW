package pl.iis.paw.trello.web.rest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.iis.paw.trello.domain.User;
import pl.iis.paw.trello.security.SecurityUtils;
import pl.iis.paw.trello.service.UserService;
import pl.iis.paw.trello.web.viewmodel.RegisterVM;

import java.net.URI;
import java.net.URISyntaxException;

@RestController
public class AccountController {

    private static final Logger log = LoggerFactory.getLogger(AccountController.class);

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

    @RequestMapping(value = "/account", method = RequestMethod.GET)
    public ResponseEntity<?> getAccount() {
        User currentUser = userService.findUserByLogin(SecurityUtils.getCurrentUserLogin());
        currentUser.getAuthorities().size();
        return new ResponseEntity<>(currentUser, HttpStatus.OK);
    }
}
