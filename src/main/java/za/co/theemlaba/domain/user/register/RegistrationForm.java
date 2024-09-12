package za.co.theemlaba.domain.user.register;

import za.co.theemlaba.domain.response.*;

/*
 * This class represents a registration form for user registration.
 */
public class RegistrationForm {
    private String password;
    private String email;
    private String firstname;
    private String lastname;
    
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public String getFirstname() {
        return firstname;
    }
    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }
    public String getLastname() {
        return lastname;
    }
    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public Response findRegistrationFields() {
        return new BasicResponse("Not set yet");
    }


}
