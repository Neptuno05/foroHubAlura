package com.alura.foro.hub.api.controller;

import com.alura.foro.hub.api.domain.course.Course;
import com.alura.foro.hub.api.domain.course.CourseRepository;
import com.alura.foro.hub.api.domain.course.DetailsCourseData;
import com.alura.foro.hub.api.domain.course.RegisterCourseData;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/courses")
@SecurityRequirement(name = "bearer-key")
@Tag(name = "Curso", description = "Puede pertenecer a una de las muchas categorías definidas")
public class CourseController {

    @Autowired
    private CourseRepository courseRepository;

    @PostMapping
//    @Transactional
    @Operation(summary = "Registrar un nuevo curso en la BD.")
    public ResponseEntity<DetailsCourseData> createTopics(@RequestBody @Valid RegisterCourseData registerCourseData,
                                                          UriComponentsBuilder uriComponentsBuilder){

        Course course = courseRepository.save(new Course(registerCourseData));

        URI url = uriComponentsBuilder.path("/course/{id}").buildAndExpand(course.getId()).toUri();
        return ResponseEntity.created(url).body(new DetailsCourseData(course));
    }

    @GetMapping("/all")
    @Operation(summary = "Lee todos los cursos independientemente de su estado")
    public ResponseEntity<Page<DetailsCourseData>> listCourses(@PageableDefault(size = 5, page = 0) Pageable pagination){
        var page = courseRepository.findAll(pagination).map(DetailsCourseData::new);
        return ResponseEntity.ok(page);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Lee un solo curso por su ID")
    public ResponseEntity<DetailsCourseData> ListarUnCurso(@PathVariable Long id){
        Course course = courseRepository.getReferenceById(id);
        return ResponseEntity.ok(new DetailsCourseData(course));
    }

}
