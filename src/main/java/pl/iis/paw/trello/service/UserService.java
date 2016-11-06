package pl.iis.paw.trello.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import pl.iis.paw.trello.domain.Authority;
import pl.iis.paw.trello.domain.User;
import pl.iis.paw.trello.exception.UserAlreadyExistsException;
import pl.iis.paw.trello.exception.UserAlreadyExistsException.Field;
import pl.iis.paw.trello.exception.UserNotFoundException;
import pl.iis.paw.trello.repository.AuthorityRepository;
import pl.iis.paw.trello.repository.UserRepository;
import pl.iis.paw.trello.security.AuthoritiesConstants;
import pl.iis.paw.trello.security.SecurityUtils;
import pl.iis.paw.trello.web.viewmodel.RegisterVM;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class UserService {

    private final static Logger log = LoggerFactory.getLogger(UserService.class);

    private UserRepository userRepository;
    private AuthorityRepository authorityRepository;

    @Autowired
    public UserService(UserRepository userRepository, AuthorityRepository authorityRepository) {
        this.userRepository = userRepository;
        this.authorityRepository = authorityRepository;
    }

    public List<User> getUsers() {
        return userRepository.findAll();
    }

    public List<User> getUsers(Pageable pageable) {
        return userRepository.findAll(pageable).getContent();
    }

    public User findUserById(Long userId) {
        return Optional
            .ofNullable(userRepository.findOne(userId))
            .orElseThrow(() -> new UserNotFoundException(userId));
    }

    public User findUserByLogin(String login) {
        return Optional
            .ofNullable(userRepository.findByLogin(login))
            .orElseThrow(() -> new UserNotFoundException(login));
    }

    public User findUserByEmail(String email) {
        return Optional
            .ofNullable(userRepository.findByEmail(email))
            .orElseThrow(() -> new UserNotFoundException(email));
    }

    public User registerUser(RegisterVM registerVM) {
        validateUser(registerVM);

        Authority authority = authorityRepository.findOne(AuthoritiesConstants.USER);
        Set<Authority> authorities = new HashSet<>();
        authorities.add(authority);

        User user = new User();
        user.setLogin(registerVM.getLogin());
        user.setPassword(registerVM.getPassword());
        user.setEmail(registerVM.getEmail());
        user.setAuthorities(authorities);

        return userRepository.save(user);
    }

    private boolean emailExists(String email) {
        return userRepository.findByEmail(email) != null;
    }

    private boolean loginExists(String login) {
        return userRepository.findByLogin(login) != null;
    }

    public User updateUser(User user) {
        User existingUser = findUserById(user.getId());

        existingUser.setLogin(user.getLogin());
        existingUser.setEmail(user.getEmail());
        existingUser.setPassword(user.getPassword());

        return userRepository.save(existingUser);
    }

    public void deleteUser(User user) {
        this.deleteUser(user.getId());
    }

    public void deleteUser(Long id) {
        User user = Optional
            .ofNullable(userRepository.findOne(id))
            .orElseThrow(() -> new UserNotFoundException(id));
        userRepository.delete(user);
    }

    private void validateUser(RegisterVM registerVM) {
        boolean loginExists = loginExists(registerVM.getLogin());
        boolean emailExists = emailExists(registerVM.getEmail());

        Field field = null;
        if (loginExists && emailExists) {
            field = Field.All;
        } else if (loginExists) {
            field = Field.Login;
        } else if (emailExists) {
            field = Field.Email;
        }

        if (loginExists || emailExists) {
            throw new UserAlreadyExistsException(registerVM.getLogin(), field);
        }
    }

    public User getCurrentUser() {
        User currentUser = findUserByLogin(SecurityUtils.getCurrentUserLogin());
        currentUser.getAuthorities().size(); // eager
        return currentUser;
    }
}