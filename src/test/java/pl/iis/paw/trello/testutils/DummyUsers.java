package pl.iis.paw.trello.testutils;

import pl.iis.paw.trello.domain.User;

public class DummyUsers {

    private DummyUsers() { }

    public static User john() {
        return new User("john", "secret1", "john@locahost");
    }

    public static User jane() {
        return new User("jane", "secret2", "jane@locahost");
    }

    public static User bill() {
        return new User("bill", "secret3", "bill@locahost");
    }
}