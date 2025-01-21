package com.alura.foro.hub.api.domain.user;

import com.alura.foro.hub.api.domain.profile.Profile;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Table(name = "user_profile")
@Entity(name = "User")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String email;
    private String clave;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "profile_id")
    private Profile profile;

    public User(RegisterUserData data, String hashedPassword, Profile profile) {
        this.name = data.name();
        this.email = data.email();
        this.clave = hashedPassword;
        this.profile = profile;
    }

    public void updateUserWithPassword(UpdateUserData updateUser, String hashedPassword) {
        if (updateUser.name() != null){
            this.name = updateUser.name();
        }
        if (updateUser.clave() != null){
            this.clave = hashedPassword;
        }
        if (updateUser.email() != null){
            this.email = updateUser.email();
        }
    }

    public void updateOnlyUserData(UpdateUserData updateUser) {
        if (updateUser.name() != null){
            this.name = updateUser.name();
        }
        if (updateUser.email() != null){
            this.email = updateUser.email();
        }
    }


    private String capitalLetters(String string) {
        return string.substring(0,1).toUpperCase()+string.substring(1).toLowerCase();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_USER"));
    }

    @Override
    public String getPassword() {
        return clave;
    }

    @Override
    public String getUsername() {
        return email;
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
