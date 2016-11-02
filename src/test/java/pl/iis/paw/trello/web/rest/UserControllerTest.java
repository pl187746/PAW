package pl.iis.paw.trello.web.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Matchers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.config.EnableSpringDataWebSupport;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import pl.iis.paw.trello.domain.User;
import pl.iis.paw.trello.exception.UserNotFoundException;
import pl.iis.paw.trello.service.UserService;
import pl.iis.paw.trello.web.viewmodel.RegisterVM;

import java.util.Arrays;

import static org.hamcrest.Matchers.*;

import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static pl.iis.paw.trello.testutils.DummyUsers.jane;
import static pl.iis.paw.trello.testutils.DummyUsers.john;

@RunWith(SpringRunner.class)
@WebMvcTest(UserController.class)
@EnableSpringDataWebSupport
public class UserControllerTest {

    @MockBean
    private UserService userService;

    @Autowired
    private MockMvc mockMvc;

    private JacksonTester<User> jacksonTester;

    @Before
    public void setUp() {
        ObjectMapper objectMapper = new ObjectMapper();
        JacksonTester.initFields(this, objectMapper);
    }

    @Test
    public void getRequest_ForAllUsers_ShouldReturn2xxStatusCodeAndUsers() throws Exception {
        when(userService.getUsers(Matchers.any(Pageable.class))).thenReturn(Arrays.asList(john(), jane()));

        mockMvc.perform(get("/users")
            .accept(MediaType.APPLICATION_JSON_UTF8))
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
            .andExpect(status().is2xxSuccessful())
            .andExpect(jsonPath("$", hasSize(2)))
            .andExpect(jsonPath("$[0].id", is(equalTo(1))))
            .andExpect(jsonPath("$[0].login", is(equalTo("john"))))
            .andExpect(jsonPath("$[0].email", is(equalTo("john@localhost"))))
            .andExpect(jsonPath("$[1].id", is(equalTo(2))))
            .andExpect(jsonPath("$[1].login", is(equalTo("jane"))))
            .andExpect(jsonPath("$[1].email", is(equalTo("jane@localhost"))));
    }

    @Test
    public void getRequest_ForExistingId_ShouldReturn2xxStatusCodeAndUser() throws Exception {
        when(userService.findUserById(1L)).thenReturn(john());

        mockMvc.perform(get("/users/1")
            .accept(MediaType.APPLICATION_JSON_UTF8))
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
            .andExpect(status().is2xxSuccessful())
            .andExpect(jsonPath("id", is(equalTo(1))))
            .andExpect(jsonPath("login", is(equalTo("john"))))
            .andExpect(jsonPath("email", is(equalTo("john@localhost"))));
    }

    @Test
    public void getRequest_ForNotExistingId_ShouldReturnNotFoundStatusCodeAndErrorMessage() throws Exception {
        when(userService.findUserById(66L)).thenThrow(new UserNotFoundException(66L));

        mockMvc.perform(get("/users/66")
            .accept(MediaType.APPLICATION_JSON_UTF8))
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
            .andExpect(status().isNotFound())
            .andExpect(jsonPath("code", is(HttpStatus.NOT_FOUND.value())))
            .andExpect(jsonPath("message", is(equalTo("User with id 66 was not found"))));
    }

    @Test
    public void postRequest_ForNewUser_ShouldReturnCreatedStatusCodeAndCreatedUser() throws Exception {
        when(userService.registerUser(Matchers.any(RegisterVM.class))).thenReturn(john());

        User newUser = john();
        john().setId(null);

        mockMvc.perform(post("/users/")
            .content(jacksonTester.write(newUser).getJson())
            .contentType(MediaType.APPLICATION_JSON_UTF8)
            .accept(MediaType.APPLICATION_JSON_UTF8))
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("id", is(equalTo(1))))
            .andExpect(jsonPath("login", is(equalTo("john"))))
            .andExpect(jsonPath("email", is(equalTo("john@localhost"))));
    }

    @Test
    public void putRequest_ForExistingUser_ShouldReturn2xxStatusCode() throws Exception {
        User updatedUser = john();
        updatedUser.setLogin("john2");
        updatedUser.setEmail("john2@localhost");

        when(userService.updateUser(Matchers.any(User.class))).thenReturn(updatedUser);

        mockMvc.perform(put("/users", updatedUser)
            .content(jacksonTester.write(updatedUser).getJson())
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
            .andExpect(status().is2xxSuccessful())
            .andExpect(jsonPath("id", is(equalTo(1))))
            .andExpect(jsonPath("login", is(equalTo("john2"))))
            .andExpect(jsonPath("email", is(equalTo("john2@localhost"))));
    }

    @Test
    public void putRequest_ForNotExistingUser_ShouldReturnNotFoundStatusCodeAndErrorMessage() throws Exception {
        User updatedUser = john();
        updatedUser.setLogin("john2");
        updatedUser.setEmail("john2@localhost");

        doThrow(new UserNotFoundException(1L)).when(userService).updateUser(Matchers.any(User.class));

        mockMvc.perform(put("/users", updatedUser)
            .content(jacksonTester.write(updatedUser).getJson())
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNotFound())
            .andExpect(jsonPath("code", is(HttpStatus.NOT_FOUND.value())))
            .andExpect(jsonPath("message", is(equalTo("User with id 1 was not found"))));
    }

    @Test
    public void deleteRequest_ForExistingId_ShouldReturn2xxStatusCode() throws Exception {
        mockMvc.perform(delete("/users/1")
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().is2xxSuccessful());
    }

    @Test
    public void deleteRequest_ForNotExistingId_ShouldReturnNotFoundStatusCodeAndErrorMessage() throws Exception {
        doThrow(new UserNotFoundException(1L)).when(userService).deleteUser(1L);

        mockMvc.perform(delete("/users/1")
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNotFound())
            .andExpect(jsonPath("code", is(HttpStatus.NOT_FOUND.value())))
            .andExpect(jsonPath("message", is(equalTo("User with id 1 was not found"))));
    }
}
