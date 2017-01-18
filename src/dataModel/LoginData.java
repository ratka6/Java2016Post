package dataModel;

import java.io.Serializable;

/**
 * Created by krzysiek on 07.01.2017.
 */
public class LoginData implements Serializable {
    public String id;
    public String password;

    public LoginData(String id, String password) {
        this.id = id;
        this.password = password;
    }

    @Override
    public String toString() {
        return "LoginData{" +
                "id='" + id + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
