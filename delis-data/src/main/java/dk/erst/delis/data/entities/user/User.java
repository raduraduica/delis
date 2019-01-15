package dk.erst.delis.data.entities.user;

import dk.erst.delis.data.entities.AbstractEntity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.Email;

/**
 * @author Iehor Funtusov, created by 02.01.19
 */

@Getter
@Setter
@Entity
@Table(name = "user")
public class User extends AbstractEntity {

    @Column(unique = true, nullable = false)
    private String username;

    @Column(nullable = false)
    private String password;

    private String firstName;
    private String lastName;

    @Email
    @Column(unique = true)
    private String email;
}
