package pl.iis.paw.trello.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import pl.iis.paw.trello.domain.User;
import pl.iis.paw.trello.exception.UserAlreadyExistsException;
import pl.iis.paw.trello.exception.UserNotFoundException;
import pl.iis.paw.trello.repository.UserRepository;

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

    public User addUser(User user) {
        if (userRepository.findByLogin(user.getLogin()) != null) {
            throw new UserAlreadyExistsException(user.getLogin());
        }

        if (userRepository.findByEmail(user.getEmail()) != null) {
            throw new UserAlreadyExistsException(user.getEmail());
        }

        return userRepository.save(user);
    }

    public User updateUser(User user) {
        return updateUser(user.getId(), user);
    }

    public User updateUser(Long id, User user) {
        User existingUser = userRepository.findOne(id);
        if (existingUser == null) {
            throw new UserNotFoundException(user.getId());
        }

        existingUser.setLogin(user.getLogin());
        existingUser.setEmail(user.getEmail());
        existingUser.setPassword(user.getPassword());

        return userRepository.save(user);
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
}