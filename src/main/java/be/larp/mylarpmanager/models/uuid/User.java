package be.larp.mylarpmanager.models.uuid;

import be.larp.mylarpmanager.models.Role;
import be.larp.mylarpmanager.models.UserActionHistory;
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
        role = Role.PLAYER;
        enabled = false;
        characters = new ArrayList<>();
        tickets = new ArrayList<>();
        setUuid();
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

    public User setUsername(String username) {
        this.username = username;
        return this;
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

    public User setPassword(String password) {
        this.password = password;
        return this;
    }

    public String getEmail() {
        return email;
    }

    public User setEmail(String email) {
        this.email = email;
        return this;
    }

    public User setId(Long id) {
        this.id = id;
        return this;
    }

    public Nation getNation() {
        return nation;
    }

    public User setNation(Nation nation) {
        this.nation = nation;
        return this;
    }

    @JsonIgnore
    public List<Ticket> getTickets() {
        return tickets;
    }

    public User setTickets(List<Ticket> tickets) {
        this.tickets = tickets;
        return this;
    }

    @JsonIgnore
    public List<Character> getCharacters() {
        return characters;
    }

    public User setCharacters(List<Character> characters) {
        this.characters = characters;
        return this;
    }

    @JsonIgnore
    public Long getId() {
        return id;
    }

    public Role getRole() {
        return role;
    }

    public User setRole(Role role) {
        this.role = role;
        return this;
    }

    public String getFirstName() {
        return firstName;
    }

    public User setFirstName(String firstName) {
        this.firstName = firstName;
        return this;
    }

    public String getLastName() {
        return lastName;
    }

    public User setLastName(String lastName) {
        this.lastName = lastName;
        return this;
    }

    @JsonIgnore
    public List<UserActionHistory> getUserActionHistories() {
        return userActionHistories;
    }

    public User setUserActionHistories(List<UserActionHistory> userActionHistories) {
        this.userActionHistories = userActionHistories;
        return this;
    }

    @JsonIgnore
    public List<JoinNationDemand> getJoinNationDemands() {
        return joinNationDemands;
    }

    public User setJoinNationDemands(List<JoinNationDemand> joinNationRequests) {
        this.joinNationDemands = joinNationRequests;
        return this;
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

    public User setEnabled(boolean enabled) {
        this.enabled = enabled;
        return this;
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

    public User setCurrentToken(String currentToken) {
        this.currentToken = currentToken;
        return this;
    }

    @JsonIgnore
    public String getCurrentToken() {
        return currentToken;
    }
}