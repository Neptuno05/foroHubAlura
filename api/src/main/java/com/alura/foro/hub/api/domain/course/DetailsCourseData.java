package com.alura.foro.hub.api.domain.course;

public record DetailsCourseData(
        Long id,
        String name,
        Category category
) {

    public DetailsCourseData(Course course){
        this(
                course.getId(),
                course.getName(),
                course.getCategory()
        );
    }
}
