package com.alura.foro.hub.api.controller;

import com.alura.foro.hub.api.domain.answer.*;
import com.alura.foro.hub.api.domain.answer.validate.ValidateCreateAnswer;
import com.alura.foro.hub.api.domain.answer.validate.ValidateUpdateAnswer;
import com.alura.foro.hub.api.domain.topic.Topic;
import com.alura.foro.hub.api.domain.topic.TopicRepository;
import com.alura.foro.hub.api.domain.topic.TopicStatus;
import com.alura.foro.hub.api.domain.user.User;
import com.alura.foro.hub.api.domain.user.UserRepository;
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
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;

@RestController
@RequestMapping("/answers")
@SecurityRequirement(name = "bearer-key")
@Tag(name = "Answer", description = "Gestiona las respuestas de los temas. Solo una respuesta puede ser marcada como solución.")
public class AnswerController {

    @Autowired
    private TopicRepository topicRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AnswerRepository answerRepository;

    @Autowired
    List<ValidateCreateAnswer> validateCreation;

    @Autowired
    List<ValidateUpdateAnswer> validateUpdate;

    @PostMapping
    @Transactional
    @Operation(
            summary = "Registra una nueva respuesta",
            description = "Registra una nueva respuesta en la base de datos, vinculada a un usuario y tema existente.",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Datos necesarios para registrar una nueva respuesta",
                    required = true,
                    content = @Content(
                            schema = @Schema(implementation = RegisterAnswerData.class),
                            examples = @ExampleObject(
                                    name = "Ejemplo de registro de respuesta",
                                    value = "{ \"message\": \"Esta es una respuesta ejemplo.\", \"usuarioId\": 5, \"topicoId\": 3 }"
                            )
                    )
            ),
            responses = {
                    @ApiResponse(responseCode = "201", description = "Respuesta registrada con éxito", content = @Content(schema = @Schema(implementation = DetailsAnswerData.class))),
                    @ApiResponse(responseCode = "400", description = "Solicitud incorrecta"),
                    @ApiResponse(responseCode = "500", description = "Error interno del servidor")
            }
    )
    public ResponseEntity<DetailsAnswerData> createAnswer(@RequestBody @Valid RegisterAnswerData registerAnswerData,
                                                          UriComponentsBuilder uriBuilder){
        validateCreation.forEach(v -> v.validate(registerAnswerData));

        User user = userRepository.getReferenceById(registerAnswerData.usuarioId());
        Topic topic = topicRepository.findById(registerAnswerData.topicoId()).get();

        var answer = new Answer(registerAnswerData, user, topic);
        answerRepository.save(answer);

        var uri = uriBuilder.path("/answer/{id}").buildAndExpand(answer.getId()).toUri();
        return ResponseEntity.created(uri).body(new DetailsAnswerData(answer));
    }

    @GetMapping("/topic/{topicId}")
    @Operation(
            summary = "Lista las respuestas de un tema",
            description = "Obtiene una lista de todas las respuestas asociadas a un tema específico.",
            parameters = {
                    @Parameter(name = "topicId", description = "ID del tema", required = true),
                    @Parameter(name = "page", description = "Número de página para la paginación"),
            },
            responses = {
                    @ApiResponse(responseCode = "200", description = "Lista de respuestas obtenida con éxito", content = @Content(schema = @Schema(implementation = DetailsAnswerData.class))),
                    @ApiResponse(responseCode = "404", description = "Tema no encontrado"),
                    @ApiResponse(responseCode = "500", description = "Error interno del servidor")
            }
    )
    public ResponseEntity<Page<DetailsAnswerData>> listAnswerOfTopic(@PageableDefault(size = 5, sort = {"createdDate"}, direction = Sort.Direction.ASC) Pageable pageable, @PathVariable Long topicId){
        var page = answerRepository.findAllByTopicId(topicId, pageable).map(DetailsAnswerData::new);
        return ResponseEntity.ok(page);
    }

    @GetMapping("/user/{userId}")
    @Operation(
            summary = "Lista las respuestas de un usuario",
            description = "Obtiene una lista de todas las respuestas publicadas por un usuario específico.",
            parameters = {
                    @Parameter(name = "userId", description = "ID del usuario", required = true),
                    @Parameter(name = "page", description = "Número de página para la paginación"),
            },
            responses = {
                    @ApiResponse(responseCode = "200", description = "Lista de respuestas obtenida con éxito", content = @Content(schema = @Schema(implementation = DetailsAnswerData.class))),
                    @ApiResponse(responseCode = "404", description = "Usuario no encontrado"),
                    @ApiResponse(responseCode = "500", description = "Error interno del servidor")
            }
    )
    public ResponseEntity<Page<DetailsAnswerData>> listAnswerOfUser(@PageableDefault(size = 5, sort = {"createdDate"}, direction = Sort.Direction.ASC)Pageable pageable, @PathVariable Long userId){
        var page = answerRepository.findAllByUserId(userId, pageable).map(DetailsAnswerData::new);
        return ResponseEntity.ok(page);
    }

    @GetMapping("/{id}")
    @Operation(
            summary = "Obtiene una respuesta por ID",
            description = "Busca y devuelve los detalles de una respuesta específica basándose en su ID.",
            parameters = @Parameter(name = "id", description = "ID de la respuesta", required = true),
            responses = {
                    @ApiResponse(responseCode = "200", description = "Respuesta obtenida con éxito", content = @Content(schema = @Schema(implementation = DetailsAnswerData.class))),
                    @ApiResponse(responseCode = "404", description = "Respuesta no encontrada"),
                    @ApiResponse(responseCode = "500", description = "Error interno del servidor")
            }
    )
    public ResponseEntity<DetailsAnswerData> listOneAnswer(@PathVariable Long id){
        Answer answer = answerRepository.getReferenceById(id);

        var answerData = new DetailsAnswerData(answer);
        return ResponseEntity.ok(answerData);
    }

    @PutMapping("/{id}")
    @Transactional
    @Operation(
            summary = "Actualiza una respuesta",
            description = "Actualiza el contenido de una respuesta, su estado de solución o su estado general.",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Datos necesarios para actualizar una respuesta",
                    required = true,
                    content = @Content(
                            schema = @Schema(implementation = UpdateAnswerData.class),
                            examples = @ExampleObject(
                                    name = "Ejemplo de actualización de respuesta",
                                    value = "{ \"contenido\": \"Este es un contenido actualizado.\", \"solution\": true }"
                            )
                    )
            ),
            parameters = @Parameter(name = "id", description = "ID de la respuesta a actualizar", required = true),
            responses = {
                    @ApiResponse(responseCode = "200", description = "Respuesta actualizada con éxito", content = @Content(schema = @Schema(implementation = DetailsAnswerData.class))),
                    @ApiResponse(responseCode = "404", description = "Respuesta no encontrada"),
                    @ApiResponse(responseCode = "500", description = "Error interno del servidor")
            }
    )
    public ResponseEntity<DetailsAnswerData> answerUpdate(@RequestBody @Valid UpdateAnswerData updateAnswerData,
                                                          @PathVariable Long id){
        validateUpdate.forEach(v -> v.validate(updateAnswerData, id));
        Answer answer = answerRepository.getReferenceById(id);
        answer.actualizarRespuesta(updateAnswerData);
        if(updateAnswerData.solution()){
            var topicSolved = topicRepository.getReferenceById(answer.getTopic().getId());
            topicSolved.setStatus(TopicStatus.SOLVED);
        }
        var dataAnswer = new DetailsAnswerData(answer);
        return ResponseEntity.ok(dataAnswer);
    }
}