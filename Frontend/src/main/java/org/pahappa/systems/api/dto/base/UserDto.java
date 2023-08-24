package org.pahappa.systems.api.dto.base;

import lombok.Getter;
import lombok.Setter;

/**
 * This class represents the user data transfer object
 *
 * @version 1.0
 * @see org.sers.webutils.model.security.User
 * @see <a href="https://en.wikipedia.org/wiki/Data_transfer_object">Data transfer object</a>
 * @since 1.0
 */
@Getter
@Setter
public class UserDto {
    private String username;
    private String password;
    private String userId;
    private String firstName;
    private String lastName;
    private String gender;
    private String emailAddress;
    private String phoneNumber;
    private String token;
}
