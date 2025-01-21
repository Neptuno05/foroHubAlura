package com.alura.foro.hub.api.domain.course;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Table(name = "courses")
@Entity(name = "Course")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class Course {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    @Enumerated(EnumType.STRING)
    private Category category;

    public Course(RegisterCourseData registerCourseData){
        this.name = registerCourseData.name();
        this.category = registerCourseData.category();
    }

    public void updateCourse(UpdateCourseData updateCourseData){
        if (updateCourseData.name() != null){
            this.name = updateCourseData.name();
        }
        if (updateCourseData.category() != null){
            this.category = updateCourseData.category();
        }
    }

//    public void deleteCourse(){
//        /*Eleminar este metodo al finalizar el controller!!!!  */
//    }
}
