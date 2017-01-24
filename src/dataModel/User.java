package dataModel;

import java.io.Serializable;

/**
 * Created by krzysiek on 07.01.2017.
 */
public class User implements Serializable {

    private String name;
    private String address;
    private String phoneNumber;
    private String email;

    public User(String name, String address, String phoneNumber, String email) {
        this.name = name;
        this.address = address;
        this.phoneNumber = phoneNumber;
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public String getAddress() {
        return address;
    }

    public static User fake() {
        return new User("Jan Kowalski", "Warszawska 24, Kraków", "555555555", "pk@pk.pl");
    }

    @Override
    public String toString() {
        return "User{" +
                "name='" + name + '\'' +
                ", address='" + address + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}
