package vsb.gai0010.model;

import lombok.*;

import java.util.Date;
import java.util.Locale;

@Data
@AllArgsConstructor
@Builder
public class User {
    private int id;
    private String login;
    private String password;
    private String email;
    private String phoneNumber;
    private String firstName;
    private String secondName;
    private String country;
    private String city;
    private String street;
    private String houseNumber;
    private int zip;
    private Date deletedAt;
    private Status status;
    private Role role;

    public String getFullName() {
        return firstLetterCapitalize(this.getFirstName()) + " " + firstLetterCapitalize(this.getSecondName());
    }

    public String getAddress() {
        return firstLetterCapitalize((this.country)) + ", " + firstLetterCapitalize(this.city);
    }

    private static String firstLetterCapitalize(String str) {
        return str.substring(0, 1).toUpperCase() + str.substring(1);
    }
}
