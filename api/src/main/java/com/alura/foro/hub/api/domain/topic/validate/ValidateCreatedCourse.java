package com.alura.foro.hub.api.domain.topic.validate;

import com.alura.foro.hub.api.domain.course.CourseRepository;
import com.alura.foro.hub.api.domain.topic.RegisterTopicData;
import com.alura.foro.hub.api.infra.errores.ValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ValidateCreatedCourse implements ValidateCreateTopic{

    @Autowired
    private CourseRepository repository;

    @Override
    public void validate(RegisterTopicData data) {
        var ExisteCurso = repository.existsById(data.courseid());
        if(!ExisteCurso){
            throw new ValidationException("This course doesn't exist");
        }
    }

}
