package com.alura.foro.hub.api.controller;


import com.alura.foro.hub.api.domain.profile.Profile;
import com.alura.foro.hub.api.domain.profile.ProfileRepository;
import com.alura.foro.hub.api.domain.user.*;
import com.alura.foro.hub.api.domain.user.validation.ValidateCreateUser;
import com.alura.foro.hub.api.domain.user.validation.ValidateUpdateUser;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;

@RestController
@RequestMapping("/users")
@SecurityRequirement(name = "bearer-key")
@Tag(name = "User", description = "Operaciones relacionadas con la gestión de usuarios, incluyendo la creación, actualización y consulta de usuarios.")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProfileRepository profileRepository;

    @Autowired
    List<ValidateCreateUser> validateCreation;

    @Autowired
    List<ValidateUpdateUser> validateUpdate;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @PostMapping
    @Transactional
    @Operation(
            summary = "Registra un nuevo usuario",
            description = "Crea un nuevo usuario en la base de datos con los datos proporcionados.",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Datos necesarios para registrar un nuevo usuario",
                    required = true,
                    content = @Content(
                            schema = @Schema(implementation = RegisterUserData.class),
                            examples = @ExampleObject(
                                    name = "Ejemplo de registro de usuario",
                                    value = "{ \"name\": \"johndoe\", \"email\": \"johndoe@email.or\", \"password\": \"password123\", \"profileType\": \"STUDENT\" }"
                            )
                    )
            ),
            responses = {
                    @ApiResponse(responseCode = "201", description = "Usuario creado con éxito", content = @Content(schema = @Schema(implementation = DetailsUserData.class))),
                    @ApiResponse(responseCode = "400", description = "Solicitud incorrecta"),
                    @ApiResponse(responseCode = "500", description = "Error interno del servidor")
            }
    )
    public ResponseEntity<DetailsUserData> createUser(@RequestBody @Valid RegisterUserData registerUserData,
                                                      UriComponentsBuilder uriBuilder){

        validateCreation.forEach(v -> v.validate(registerUserData));

        String hashedPassword = passwordEncoder.encode(registerUserData.password());

        // Check if profile exists, if not create new one
        Profile profile = profileRepository.findByName(registerUserData.profileType())
                .orElseGet(() -> {
                    Profile newProfile = new Profile(registerUserData);
                    return profileRepository.save(newProfile);
                });

        User dataUser = new User(registerUserData, hashedPassword, profile);
        userRepository.save(dataUser);

        var uri = uriBuilder.path("/usuarios/{username}").buildAndExpand(dataUser.getName()).toUri();
        return ResponseEntity.created(uri).body(new DetailsUserData(dataUser));
    }

    @GetMapping("/all")
    @Operation(
            summary = "Lista todos los usuarios",
            description = "Obtiene una lista de todos los usuarios registrados, independientemente de su estado.",
            parameters = @Parameter(name = "page", description = "Número de página para la paginación"),
            responses = {
                    @ApiResponse(responseCode = "200", description = "Lista de usuarios obtenida con éxito", content = @Content(schema = @Schema(implementation = DetailsUserData.class))),
                    @ApiResponse(responseCode = "500", description = "Error interno del servidor")
            }
    )
    public ResponseEntity<Page<DetailsUserData>> listAllUsers(@PageableDefault(size = 5, sort = {"id"}) Pageable pageable){
        var page = userRepository.findAll(pageable).map(DetailsUserData::new);
        return ResponseEntity.ok(page);
    }

    @GetMapping("/userName/{userName}")
    @Operation(
            summary = "Obtiene un usuario por nombre de usuario",
            description = "Busca y devuelve los detalles de un usuario específico basándose en su nombre de usuario.",
            parameters = @Parameter(name = "userName", description = "Nombre de usuario a buscar", required = true),
            responses = {
                    @ApiResponse(responseCode = "200", description = "Usuario encontrado con éxito", content = @Content(schema = @Schema(implementation = DetailsUserData.class))),
                    @ApiResponse(responseCode = "404", description = "Usuario no encontrado"),
                    @ApiResponse(responseCode = "500", description = "Error interno del servidor")
            }
    )
    public ResponseEntity<DetailsUserData> listOneUser(@PathVariable String userName){
        User user = (User) userRepository.findByNameContainingIgnoreCase(userName);
        var dataUser = new DetailsUserData(user);
        return ResponseEntity.ok(dataUser);
    }

    @PutMapping("/{userName}")
    @Transactional
    @Operation(
            summary = "Actualiza un usuario",
            description = "Actualiza la contraseña, nombre y correo electrónico de un usuario existente.",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Datos necesarios para actualizar el usuario",
                    required = true,
                    content = @Content(
                            schema = @Schema(implementation = UpdateUserData.class),
                            examples = @ExampleObject(
                                    name = "Ejemplo de actualización de usuario",
                                    value = "{ \"name\": \"johndoe\", \"email\": \"johndoe@email.or\", \"password\": \"password123\", \"profileType\": \"STUDENT\" }"
                            )
                    )
            ),
            parameters = @Parameter(name = "userName", description = "Nombre de usuario del usuario a actualizar", required = true),
            responses = {
                    @ApiResponse(responseCode = "200", description = "Usuario actualizado con éxito", content = @Content(schema = @Schema(implementation = DetailsUserData.class))),
                    @ApiResponse(responseCode = "400", description = "Solicitud incorrecta"),
                    @ApiResponse(responseCode = "404", description = "Usuario no encontrado"),
                    @ApiResponse(responseCode = "500", description = "Error interno del servidor")
            }
    )
    public ResponseEntity<DetailsUserData> userUpdate(@RequestBody @Valid UpdateUserData updateUser,
                                                      @PathVariable String userName){
        validateUpdate.forEach(v -> v.validate(updateUser));
        User user = (User) userRepository.findByName(userName);

        if (updateUser.clave() != null){
            String hashedPassword = passwordEncoder.encode(updateUser.clave());
            user.updateUserWithPassword(updateUser, hashedPassword);

        }else {
            user.updateOnlyUserData(updateUser);
        }

        var dataUser = new DetailsUserData(user);
        return ResponseEntity.ok(dataUser);
    }

}
