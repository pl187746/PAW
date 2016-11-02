package pl.iis.paw.trello.testutils;

import pl.iis.paw.trello.domain.User;

public class DummyUsers {

    private DummyUsers() { }

    public static User john() {
        User user = new User("john", "secret1", "john@localhost");
        user.setId(1L);
        return user;
    }

    public static User jane() {
        User user = new User("jane", "secret2", "jane@localhost");
        user.setId(2L);
        return user;
    }

    public static User bill() {
        User user = new User("bill", "secret3", "bill@localhost");
        user.setId(3L);
        return user;
    }
}