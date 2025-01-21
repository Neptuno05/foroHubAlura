package com.alura.foro.hub.api.domain.profile;

import com.alura.foro.hub.api.domain.user.RegisterUserData;
import jakarta.persistence.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Table(name = "profiles")
@Entity(name = "profile")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class Profile {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    //    private String name;
    @Enumerated(EnumType.STRING)
    private ProfileType name;

    public Profile(@Valid RegisterUserData registerUserData) {
        this.name = registerUserData.profileType();
    }
}
