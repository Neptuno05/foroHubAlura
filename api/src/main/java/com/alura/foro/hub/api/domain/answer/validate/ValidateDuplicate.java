package com.alura.foro.hub.api.domain.answer.validate;

import com.alura.foro.hub.api.domain.answer.Answer;
import com.alura.foro.hub.api.domain.answer.AnswerRepository;
import com.alura.foro.hub.api.domain.answer.UpdateAnswerData;
import com.alura.foro.hub.api.domain.topic.TopicRepository;
import com.alura.foro.hub.api.domain.topic.TopicStatus;
import com.alura.foro.hub.api.infra.errores.ValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ValidateDuplicate implements ValidateUpdateAnswer{
    @Autowired
    private AnswerRepository answerRepository;

    @Autowired
    private TopicRepository topicRepository;

    @Override
    public void validate(UpdateAnswerData data, Long respuestaId) {
        if (data.solution()){
            Answer respuesta = answerRepository.getReferenceById(respuestaId);
            var topicoResuelto = topicRepository.getReferenceById(respuesta.getTopic().getId());
            if (topicoResuelto.getStatus() == TopicStatus.SOLVED){
                throw new ValidationException("This topic is now solved.");
            }
        }
    }

}
