package pl.iis.paw.trello.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import pl.iis.paw.trello.domain.User;
import pl.iis.paw.trello.exception.UserAlreadyExistsException;
import pl.iis.paw.trello.exception.UserAlreadyExistsException.Field;
import pl.iis.paw.trello.exception.UserNotFoundException;
import pl.iis.paw.trello.repository.UserRepository;
import pl.iis.paw.trello.web.viewmodel.RegisterVM;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private final static Logger log = LoggerFactory.getLogger(UserService.class);

    private UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
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

        User user = new User();
        user.setLogin(registerVM.getLogin());
        user.setPassword(registerVM.getPassword());
        user.setEmail(registerVM.getEmail());

        return userRepository.save(user);
    }

    private boolean emailExists(String email) {
        return userRepository.findByEmail(email) != null;
    }

    private boolean loginExists(String login) {
        return userRepository.findByLogin(login) != null;
    }

    public User updateUser(User user) {
        return updateUser(user.getId(), user);
    }

    public User updateUser(Long id, User user) {
        User existingUser = findUserById(id);

        existingUser.setLogin(user.getLogin());
        existingUser.setEmail(user.getEmail());
        existingUser.setPassword(user.getPassword());

        return userRepository.save(existingUser);
    }

    public void deleteUser(User user) {
        userRepository.delete(user);
    }

    public void deleteUser(Long id) {
        User user = Optional
            .ofNullable(userRepository.findOne(id))
            .orElseThrow(() -> new UserNotFoundException(id));
        this.deleteUser(user);
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
}