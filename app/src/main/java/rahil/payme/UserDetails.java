package rahil.payme;

/**
 * Created by Rahil on 9/27/16.
 */
public class UserDetails {
    private String userName;
    private String password;
    private String personName;

    public UserDetails(String userName, String password, String personName) {
        this.userName = userName;
        this.password = password;
        this.personName = personName;
    }

    public String getUserName() {
        return userName;
    }

    public String getPassword() {
        return password;
    }

    public String getPersonName() {
        return personName;
    }
}

