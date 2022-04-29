package be.larp.mylarpmanager.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.*;

@Entity
@Table(name = "USER")
public class User extends UuidModel implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "USER_ID")
    private Long id;

    @Column(name = "ENABLED")
    private boolean enabled;
    @Column(name = "USERNAME", nullable = false, unique = true)
    private String username;

    @Column(name = "PASSWORD", nullable = false)
    private String password;

    @Column(name = "FIRST_NAME", nullable = false)
    private String firstName;

    @Column(name = "LAST_NAME", nullable = false)
    private String lastName;

    @Column(name = "EMAIL", nullable = false, unique = true)
    private String email;

    @Column(name = "CURRENT_TOKEN", unique = true)
    private String currentToken;

    @OneToMany(mappedBy = "player")
    private List<Ticket> tickets;

    @OneToMany(mappedBy = "player")
    private List<Character> characters;

    @OneToMany(mappedBy = "candidate")
    private List<JoinNationDemand> joinNationDemands;

    @OneToMany(mappedBy = "user")
    private List<UserActionHistory> userActionHistories;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "fk_nation_players")
    private Nation nation;

    @Enumerated(EnumType.STRING)
    @Column(name = "ROLE", nullable = false)
    private Role role;

    public User() {
        super();
        this.enabled = false;
        characters = new ArrayList<>();
        tickets = new ArrayList<>();
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    @JsonIgnore
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    @JsonIgnore
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    @JsonIgnore
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    @JsonIgnore
    public boolean isEnabled() {
        return enabled;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    @JsonIgnore
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singleton(new SimpleGrantedAuthority(role.name()));
    }

    @Override
    @JsonIgnore
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

    public void setId(Long id) {
        this.id = id;
    }

    public Nation getNation() {
        return nation;
    }

    public void setNation(Nation nation) {
        this.nation = nation;
    }

    @JsonIgnore
    public List<Ticket> getTickets() {
        return tickets;
    }

    public void setTickets(List<Ticket> tickets) {
        this.tickets = tickets;
    }

    @JsonIgnore
    public List<Character> getCharacters() {
        return characters;
    }

    public void setCharacters(List<Character> characters) {
        this.characters = characters;
    }

    @JsonIgnore
    public Long getId() {
        return id;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    @JsonIgnore
    public List<UserActionHistory> getUserActionHistories() {
        return userActionHistories;
    }

    public void setUserActionHistories(List<UserActionHistory> userActionHistories) {
        this.userActionHistories = userActionHistories;
    }

    @JsonIgnore
    public List<JoinNationDemand> getJoinNationDemands() {
        return joinNationDemands;
    }

    public void setJoinNationDemands(List<JoinNationDemand> joinNationRequests) {
        this.joinNationDemands = joinNationRequests;
    }

    @Override
    public String toString() {
        return "User{" +
                "enabled=" + enabled +
                ", username='" + username + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", email='" + email + '\'' +
                ", role=" + role +
                '}';
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    @JsonIgnore
    public boolean isNationAdmin() {
        return role.equals(Role.NATION_ADMIN);
    }


    @JsonIgnore
    public boolean isAdmin() {
        return role.equals(Role.ADMIN);
    }


    @JsonIgnore
    public boolean isNationSheriff() {
        return role.equals(Role.NATION_SHERIFF);
    }


    @JsonIgnore
    public boolean isOrga() {
        return role.equals(Role.ORGA);
    }

    public void setCurrentToken(String currentToken) {
        this.currentToken = currentToken;
    }

    @JsonIgnore
    public String getCurrentToken() {
        return currentToken;
    }
}