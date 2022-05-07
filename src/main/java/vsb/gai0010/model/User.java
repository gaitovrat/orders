package vsb.gai0010.orm.model;

import lombok.*;

import java.util.Date;

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
}
