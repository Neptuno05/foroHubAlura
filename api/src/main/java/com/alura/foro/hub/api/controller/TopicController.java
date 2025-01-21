package com.alura.foro.hub.api.controller;

import com.alura.foro.hub.api.domain.answer.Answer;
import com.alura.foro.hub.api.domain.answer.AnswerRepository;
import com.alura.foro.hub.api.domain.answer.DetailsAnswerData;
import com.alura.foro.hub.api.domain.course.Course;
import com.alura.foro.hub.api.domain.course.CourseRepository;
import com.alura.foro.hub.api.domain.topic.*;
import com.alura.foro.hub.api.domain.topic.validate.ValidateCreateTopic;
import com.alura.foro.hub.api.domain.topic.validate.ValidateUpdateTopic;
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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/Topics")
@SecurityRequirement(name = "bearer-key")
@Tag(name = "Topic", description = "Gestiona los temas vinculados a cursos y usuarios específicos.")
public class TopicController {

    @Autowired
    private TopicRepository topicRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private AnswerRepository answerRepository;

    @Autowired
    List<ValidateCreateTopic> validateCreation;

    @Autowired
    List<ValidateUpdateTopic> validateUpdate;

    @PostMapping
    @Transactional
    @Operation(
            summary = "Registra un nuevo tema",
            description = "Registra un nuevo tema en la base de datos, vinculado a un usuario y curso existentes.",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Datos necesarios para registrar un nuevo tema",
                    required = true,
                    content = @Content(
                            schema = @Schema(implementation = RegisterTopicData.class),
                            examples = @ExampleObject(
                                    name = "Ejemplo de registro de tema",
                                    value = "{ \"title\": \"Nuevo Tema\", \"message\": \"Contenido del tema\", \"userid\": 1, \"courseid\": 2 }"
                            )
                    )
            ),
            responses = {
                    @ApiResponse(responseCode = "201", description = "Tema registrado con éxito", content = @Content(schema = @Schema(implementation = DetailsTopicData.class))),
                    @ApiResponse(responseCode = "400", description = "Solicitud incorrecta"),
                    @ApiResponse(responseCode = "500", description = "Error interno del servidor")
            }
    )
    public ResponseEntity<DetailsTopicData> createTopic(@RequestBody @Valid RegisterTopicData registerTopico,
                                                        UriComponentsBuilder uriBuilder){
        validateCreation.forEach(v -> v.validate(registerTopico));

        User user = userRepository.findById(registerTopico.userid()).get();
        Course course = courseRepository.findById(registerTopico.courseid()).get();
        Topic topic = new Topic(registerTopico, user, course);

        topicRepository.save(topic);

        var uri = uriBuilder.path("/topics/{id}").buildAndExpand(topic.getId()).toUri();
        return ResponseEntity.created(uri).body(new DetailsTopicData(topic));
    }

    @GetMapping("/all")
    @Operation(
            summary = "Lista todos los temas",
            description = "Obtiene una lista de todos los temas registrados, independientemente de su estado.",
            parameters = @Parameter(name = "page", description = "Número de página para la paginación"),
            responses = {
                    @ApiResponse(responseCode = "200", description = "Lista de temas obtenida con éxito", content = @Content(schema = @Schema(implementation = DetailsTopicData.class))),
                    @ApiResponse(responseCode = "500", description = "Error interno del servidor")
            }
    )
    public ResponseEntity<Page<DetailsTopicData>> listAllTopics(@PageableDefault(size = 10, sort = {"createdDate"},
            direction = Sort.Direction.ASC) Pageable pageable){
        var page = topicRepository.findAll(pageable).map(DetailsTopicData::new);
        return ResponseEntity.ok(page);
    }

    @GetMapping
    @Operation(
            summary = "Lista de temas activos y cerrados",
            description = "Obtiene una lista de todos los temas que no han sido eliminados.",
            parameters = @Parameter(name = "page", description = "Número de página para la paginación"),
            responses = {
                    @ApiResponse(responseCode = "200", description = "Lista de temas obtenida con éxito", content = @Content(schema = @Schema(implementation = DetailsTopicData.class))),
                    @ApiResponse(responseCode = "500", description = "Error interno del servidor")
            }
    )
    public ResponseEntity<Page<DetailsTopicData>> listUndeletedTopics(@PageableDefault(size = 10, sort = {"createdDate"},
            direction = Sort.Direction.ASC) Pageable pageable){
        var page = topicRepository.findAllByStatusIsNot(TopicStatus.DELETED, pageable).map(DetailsTopicData::new);
        return ResponseEntity.ok(page);
    }

    @GetMapping("/{id}")
    @Operation(
            summary = "Obtiene un tema por ID",
            description = "Busca y devuelve los detalles de un tema específico basándose en su ID.",
            parameters = @Parameter(name = "id", description = "ID del tema", required = true),
            responses = {
                    @ApiResponse(responseCode = "200", description = "Tema obtenido con éxito", content = @Content(schema = @Schema(implementation = DetailsTopicData.class))),
                    @ApiResponse(responseCode = "404", description = "Tema no encontrado"),
                    @ApiResponse(responseCode = "500", description = "Error interno del servidor")
            }
    )
    public ResponseEntity<DetailsTopicData> listOneTopic(@PathVariable Long id){
        Topic topic = topicRepository.getReferenceById(id);
        var dataTopic = new DetailsTopicData(topic);
        return ResponseEntity.ok(dataTopic);
    }

    @GetMapping("/{id}/solution")
    @Operation(
            summary = "Obtiene la solución de un tema",
            description = "Lee la respuesta del tema marcada como su solución.",
            parameters = @Parameter(name = "id", description = "ID del tema", required = true),
            responses = {
                    @ApiResponse(responseCode = "200", description = "Solución obtenida con éxito", content = @Content(schema = @Schema(implementation = DetailsAnswerData.class))),
                    @ApiResponse(responseCode = "404", description = "Tema no encontrado"),
                    @ApiResponse(responseCode = "500", description = "Error interno del servidor")
            }
    )
    public ResponseEntity<List<DetailsAnswerData>> listSolutionTopic(@PathVariable Long id){
        List<Answer> answers = answerRepository.findByTopicId(id);
        List<DetailsAnswerData> dataAnswer = answers.stream().map(DetailsAnswerData::new).collect(Collectors.toList());
        return ResponseEntity.ok(dataAnswer);
    }

    @PutMapping("/{id}")
    @Transactional
    @Operation(
            summary = "Actualiza un tema",
            description = "Actualiza el título, mensaje, estado o ID del curso de un tema existente.",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Datos necesarios para actualizar un tema",
                    required = true,
                    content = @Content(
                            schema = @Schema(implementation = UpdateTopicData.class),
                            examples = @ExampleObject(
                                    name = "Ejemplo de actualización de tema",
                                    value = "{ \"title\": \"Título actualizado\", \"message\": \"Mensaje actualizado\", \"status\": \"ACTIVO\", \"course_id\": 1 }"
                            )
                    )
            ),
            parameters = @Parameter(name = "id", description = "ID del tema a actualizar", required = true),
            responses = {
                    @ApiResponse(responseCode = "200", description = "Tema actualizado con éxito", content = @Content(schema = @Schema(implementation = DetailsTopicData.class))),
                    @ApiResponse(responseCode = "404", description = "Tema no encontrado"),
                    @ApiResponse(responseCode = "500", description = "Error interno del servidor")
            }
    )
    public ResponseEntity<DetailsTopicData> actualizarTopico(@RequestBody @Valid UpdateTopicData updateTopicData, @PathVariable Long id){
        validateUpdate.forEach(v -> v.validate(updateTopicData));

        Topic topic = topicRepository.getReferenceById(id);

        if(updateTopicData.course_id() != null){
            Course course = courseRepository.getReferenceById(updateTopicData.course_id());
            topic.actualizarTopicoConCurso(updateTopicData, course);
        }else{
            topic.actualizarTopico(updateTopicData);
        }

        var topicData = new DetailsTopicData(topic);
        return ResponseEntity.ok(topicData);
    }

@DeleteMapping("/{id}")
@Transactional
@Operation(
        summary = "Elimina un tema",
        description = "Marca un tema como eliminado en la base de datos.",
        parameters = @Parameter(name = "id", description = "ID del tema a eliminar", required = true),
        responses = {
                @ApiResponse(responseCode = "204", description = "Tema eliminado con éxito"),
                @ApiResponse(responseCode = "404", description = "Tema no encontrado"),
                @ApiResponse(responseCode = "500", description = "Error interno del servidor")
        }
)
public ResponseEntity<?> DeleteTopic(@PathVariable Long id){
    Optional<Topic> topicOptional = topicRepository.findById(id);

    if (topicOptional.isEmpty()) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body("The ID of topic provide doesn't exist.");
    }

    Topic topic = topicOptional.get();
    topic.deleteTopic();
    return ResponseEntity.noContent().build();
}

}

