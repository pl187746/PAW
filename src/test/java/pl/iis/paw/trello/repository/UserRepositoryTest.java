package pl.iis.paw.trello.repository;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;
import pl.iis.paw.trello.domain.User;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;

@RunWith(SpringRunner.class)
@DataJpaTest
@DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
public class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    public void isInitialized() {
        assertThat(userRepository.count(), is(3L));
    }

    @Test
    public void findById_WithExistingId_ShouldReturnUser() {
        User user = userRepository.findById(1L);

        assertThat(user, is(notNullValue()));
        assertThat(user.getId(), is(equalTo(1L)));
        assertThat(user.getLogin(), is(equalTo("john")));
        assertThat(user.getEmail(), is(equalTo("john@localhost")));
    }

    @Test
    public void findById_WithNotExistingId_ShouldReturnNull() {
        User user = userRepository.findById(66L);

        assertThat(user, is(nullValue()));
    }

    @Test
    public void findByLogin_WithExistingLogin_ShouldReturnUser() {
        User user = userRepository.findByLogin("john");

        assertThat(user, is(notNullValue()));
        assertThat(user.getId(), is(equalTo(1L)));
        assertThat(user.getLogin(), is(equalTo("john")));
        assertThat(user.getEmail(), is(equalTo("john@localhost")));
    }

    @Test
    public void findByLogin_WithNotExistingLogin_ShouldReturnNull() {
        User user = userRepository.findByLogin("unknown");

        assertThat(user, is(nullValue()));
    }

    @Test
    public void findByEmail_WithExistingEmail_ShouldReturnUser() {
        User user = userRepository.findByEmail("john@localhost");

        assertThat(user, is(notNullValue()));
        assertThat(user.getId(), is(equalTo(1L)));
        assertThat(user.getLogin(), is(equalTo("john")));
        assertThat(user.getEmail(), is(equalTo("john@localhost")));
    }

    @Test
    public void findAll_ShouldReturnAllUsers() {
        List<User> users = userRepository.findAll();
        Set<String> usersLogins = users.stream().map(User::getLogin).collect(Collectors.toSet());

        assertThat(users, hasSize(3));
        assertThat(usersLogins, containsInAnyOrder("john", "jane", "bill"));
    }

    @Test
    public void save_ForNewUser_ShouldAddHimToRepository() {
        User user = new User("kate", "secret4", "kate@localhost");

        userRepository.save(user);

        User addedUser = userRepository.findOne(4L);
        assertThat(addedUser, is(notNullValue()));
        assertThat(addedUser.getLogin(), is(equalTo("kate")));
        assertThat(addedUser.getEmail(), is(equalTo("kate@localhost")));
    }

    @Test
    public void save_ForExistingUser_ShouldUpdateHimInRepository() {
        User user = userRepository.findById(1L);
        String oldPassword = user.getPassword();

        user.setPassword("new-password");
        userRepository.save(user);

        userRepository.findById(1L);
        assertThat(user.getLogin(), is(equalTo("john")));
        assertThat(user.getPassword(), is(not(equalTo(oldPassword))));
        assertThat(user.getPassword(), is(equalTo("new-password")));
    }

    @Test
    public void delete_ExistingUser_ShouldRemoveHimFromRepository() {
        userRepository.delete(1L);

        assertThat(userRepository.count(), is(equalTo(2L)));
    }
}