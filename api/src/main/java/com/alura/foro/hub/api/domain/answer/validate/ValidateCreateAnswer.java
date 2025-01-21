package com.alura.foro.hub.api.domain.answer.validate;

import com.alura.foro.hub.api.domain.answer.RegisterAnswerData;

public interface ValidateCreateAnswer {
    void validate(RegisterAnswerData data);
}
