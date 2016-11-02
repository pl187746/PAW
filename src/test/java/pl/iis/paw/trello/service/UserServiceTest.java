package pl.iis.paw.trello.service;


import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.data.domain.*;
import org.springframework.test.context.junit4.SpringRunner;
import pl.iis.paw.trello.domain.User;
import pl.iis.paw.trello.exception.UserAlreadyExistsException;
import pl.iis.paw.trello.exception.UserNotFoundException;
import pl.iis.paw.trello.repository.UserRepository;
import pl.iis.paw.trello.web.viewmodel.RegisterVM;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.hamcrest.Matchers.*;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.*;
import static pl.iis.paw.trello.testutils.DummyUsers.bill;
import static pl.iis.paw.trello.testutils.DummyUsers.john;


@RunWith(SpringRunner.class)
public class UserServiceTest {

    @Mock
    private UserRepository usersRepository;

    @InjectMocks
    private UserService usersService;

    @Test
    public void getUsers_ShouldReturnAllUsers() {
        when(usersRepository.findAll()).thenReturn(Arrays.asList(john(), bill()));

        List<User> users = usersService.getUsers();
        List<String> usersLogins = users.stream().map(User::getLogin).collect(Collectors.toList());

        assertThat(users, hasSize(2));
        assertThat(usersLogins, containsInAnyOrder("john", "bill"));
    }

    @Test
    public void getUsers_ShouldReturnAllUsersPaginated() {
        List<User> userList = Arrays.asList(john(), bill());
        PageRequest pageRequest = new PageRequest(1, 2);
        when(usersRepository.findAll(pageRequest)).thenReturn(new PageImpl<>(userList));

        List<User> users = usersService.getUsers(pageRequest);

        assertThat(users, is(equalTo(userList)));
    }

    @Test
    public void findUserById_ForExistingId_ShouldReturnUser() {
        User user = john();
        when(usersRepository.findOne(user.getId())).thenReturn(user);

        User foundUser = usersService.findUserById(user.getId());

        assertThat(foundUser, is(equalTo(user)));
    }

    @Test(expected = UserNotFoundException.class)
    public void findUserById_ForNotExistingId_ShouldThrowUserNotFoundException() {
        when(usersRepository.findOne(anyLong())).thenReturn(null);

        usersService.findUserById(9000L);
    }

    @Test
    public void findByLogin_ForExistingLogin_ShouldReturnUser() {
        User user = john();
        when(usersRepository.findByLogin("john")).thenReturn(user);

        User foundUser = usersService.findUserByLogin(user.getLogin());

        assertThat(foundUser, is(equalTo(user)));
    }

    @Test(expected = UserNotFoundException.class)
    public void findByLogin_ForNotExistingLogin_shouldThrowUserNotFoundException() {
        when(usersRepository.findByLogin(anyString())).thenReturn(null);

        usersService.findUserByLogin("unknown");
    }

    public void findByEmail_ForExistingEmail_ShouldReturnUser() {
        User user = john();
        when(usersRepository.findByEmail(user.getEmail())).thenReturn(user);

        User foundUser = usersService.findUserByEmail(user.getEmail());

        assertThat(foundUser, is(equalTo(user)));
    }

    @Test(expected = UserNotFoundException.class)
    public void findByEmail_ForNotExistingEmail_ShouldThrowUserNotFoundException() {
        when(usersRepository.findByEmail((anyString()))).thenReturn(null);

        usersService.findUserByEmail("unknown@mail.com");
    }

    @Test
    public void addUser_ForValidUser_ShouldCallSaveInUserRepository() throws Exception {
        User expectedUser = new User("john", "secret1", "john@localhost");
        when(usersRepository.findByLogin("john")).thenReturn(null);

        usersService.registerUser(new RegisterVM("john", "secret1", "john@localhost"));

        verify(usersRepository, times(1)).save(expectedUser);
    }

    @Test(expected = UserAlreadyExistsException.class)
    public void addUser_ForLoginAlreadyExisting_ShouldThrowUserAlreadyExistsException() throws Exception {
        User user = john();

        when(usersRepository.findByLogin(user.getLogin())).thenReturn(user);

        usersService.registerUser(new RegisterVM(user.getLogin(), user.getEmail(), user.getPassword()));
    }

    @Test(expected = UserAlreadyExistsException.class)
    public void addUser_ForEmailAlreadyExisting_ShouldThrowUserAlreadyExistsException() throws Exception {
        User user = john();

        when(usersRepository.findByEmail(user.getEmail())).thenReturn(user);

        usersService.registerUser(new RegisterVM(user.getLogin(), user.getPassword(), user.getEmail()));
    }

    @Test
    public void updateUser_ForExistingUser_ShouldCallSaveInUserRepository() {
        User user = john();

        when(usersRepository.findOne(user.getId())).thenReturn(user);

        user.setLogin("john2");
        usersService.updateUser(user);

        verify(usersRepository, times(1)).save(user);
    }

    @Test(expected = UserNotFoundException.class)
    public void updateUser_ForNotExistingUser_ShouldThrowUserNotFoundException() {
        User user = john();

        when(usersRepository.findById(user.getId())).thenReturn(null);

        user.setLogin("john2");
        usersService.updateUser(user);
    }

    @Test
    public void deleteUser_ForExistingUser_ShouldCallDeleteInUserRepository() {
        User user = john();

        when(usersRepository.findOne(user.getId())).thenReturn(user);

        usersService.deleteUser(user);

        verify(usersRepository, times(1)).delete(user);
    }

    @Test(expected = UserNotFoundException.class)
    public void deleteUser_ForNotExistingUser_ShouldThrowUserNotFoundException() {
        User user = john();

        when(usersRepository.findOne(user.getId())).thenReturn(null);

        usersService.deleteUser(user);
    }
}