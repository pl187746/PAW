package pl.iis.paw.trello.web.viewmodel;

public class LoginVM {

    private String login;
    private String password;
    private Boolean rememberMe;

    public LoginVM() {
    }

    public LoginVM(String login, String password, Boolean rememberMe) {
        this.login = login;
        this.password = password;
        this.rememberMe = rememberMe;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Boolean getRememberMe() {
        return rememberMe;
    }

    public void setRememberMe(Boolean rememberMe) {
        this.rememberMe = rememberMe;
    }
}
