package com.alura.foro.hub.api.domain.profile;

import jakarta.validation.constraints.NotNull;

//public record ProfileData(
//        @NotNull String name
//) {
//}
public record ProfileData(Long id, String name) {
    public ProfileData(Profile profile) {
        this(profile.getId(), profile.getName().toString());
    }
}