package com.university.ManageNotes.security;

import com.university.ManageNotes.model.Users;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.Objects;

public class UserPrincipal implements UserDetails {
    @Getter
    private Long id;
    private String username;
    @Getter
    private String firstName;
    @Getter
    private String lastName;
    @Getter
    private String email;
    private String password;
    private Collection<? extends GrantedAuthority> authorities;
    private Boolean mustChangePassword;

    public UserPrincipal(Long id, String username, String firstName, String lastName, String email, String password,
                         Collection<? extends GrantedAuthority> authorities, Boolean mustChangePassword) {
        this.id = id;
        this.username = username;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.authorities = authorities;
        this.mustChangePassword = mustChangePassword;
    }

    public static UserPrincipal create(Users user) {
        String roleName = user.getRole() != null ? user.getRole().name() : "STUDENT";
        List<GrantedAuthority> authorities = List.of(
                new SimpleGrantedAuthority("ROLE_" + roleName)
        );

        return new UserPrincipal(
                user.getId(),
                user.getUsername(),
                user.getFirstName(),
                user.getLastName(),
                user.getEmail(),
                user.getPassword(),
                authorities,
                user.getMustChangePassword()
        );
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    public boolean hasRole(String role) {
        return authorities.stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_" + role));
    }

    public boolean isTeacher() {return hasRole("TEACHER");}

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserPrincipal that = (UserPrincipal) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    public boolean isMustChangePassword() {
        return Boolean.TRUE.equals(mustChangePassword);
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
