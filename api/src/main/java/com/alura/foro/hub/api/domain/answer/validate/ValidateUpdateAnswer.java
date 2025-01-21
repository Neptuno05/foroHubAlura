package com.alura.foro.hub.api.domain.answer.validate;

import com.alura.foro.hub.api.domain.answer.UpdateAnswerData;

public interface ValidateUpdateAnswer {
    void validate(UpdateAnswerData data, Long answerid);
}
