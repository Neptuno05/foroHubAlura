package com.alura.foro.hub.api.domain.topic.validate;

import com.alura.foro.hub.api.domain.course.CourseRepository;
import com.alura.foro.hub.api.domain.topic.UpdateTopicData;
import com.alura.foro.hub.api.infra.errores.ValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ValidateUpdatedCourse implements ValidateUpdateTopic{
    @Autowired
    private CourseRepository repository;

    @Override
    public void validate(UpdateTopicData data) {
        if(data.course_id() != null){
            var ExisteCurso = repository.existsById(data.course_id());
            if (!ExisteCurso){
                throw new ValidationException("This course doesn't exist");
            }
        }

    }

}
