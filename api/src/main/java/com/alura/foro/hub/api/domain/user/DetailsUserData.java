package com.alura.foro.hub.api.domain.user;

import com.alura.foro.hub.api.domain.profile.Profile;
import com.alura.foro.hub.api.domain.profile.ProfileData;

public record DetailsUserData(
        Long id,
        String name,
        String email,
        ProfileData profile
) {
    public DetailsUserData(User user){
        this(
                user.getId(),
                user.getName(),
                user.getEmail(),
                new ProfileData(user.getProfile())
        );

    }
}
