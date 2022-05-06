package be.larp.mylarpmanager.requests;

import javax.validation.constraints.NotBlank;

public class CreateUserRequest {

    @NotBlank(message = "Username is required.")
    private String username;

    @NotBlank(message = "An email address is required.")
    private String email;

    @NotBlank(message = "A first name is required.")
    private String firstName;

    @NotBlank(message = "A last name is required.")
    private String lastName;

    @NotBlank(message = "A password is required.")
    private String password;

    @NotBlank(message = "A password confirmation is required.")
    private String passwordConfirmation;

    public CreateUserRequest() {
    }

    public String getUsername() {
        return username;
    }

    public CreateUserRequest setUsername(String username) {
        this.username = username;
        return this;
    }

    public String getEmail() {
        return email;
    }

    public CreateUserRequest setEmail(String email) {
        this.email = email;
        return this;
    }

    public String getFirstName() {
        return firstName;
    }

    public CreateUserRequest setFirstName(String firstName) {
        this.firstName = firstName;
        return this;
    }

    public String getLastName() {
        return lastName;
    }

    public CreateUserRequest setLastName(String lastName) {
        this.lastName = lastName;
        return this;
    }

    public String getPassword() {
        return password;
    }

    public CreateUserRequest setPassword(String password) {
        this.password = password;
        return this;
    }

    public String getPasswordConfirmation() {
        return passwordConfirmation;
    }

    public CreateUserRequest setPasswordConfirmation(String passwordConfirmation) {
        this.passwordConfirmation = passwordConfirmation;
        return this;
    }
}
