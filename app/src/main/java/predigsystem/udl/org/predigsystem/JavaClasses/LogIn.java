package predigsystem.udl.org.predigsystem.JavaClasses;

/**
 * Created by Pau on 12/12/17.
 */

public class LogIn {
    public String username;
    public String password;

    public LogIn() {
    }

    public LogIn(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }


    @Override
    public String toString() {
        return "LogIn{" +
                "username='" + username + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
