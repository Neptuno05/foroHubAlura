package com.alura.foro.hub.api.domain.topic;

import com.fasterxml.jackson.annotation.JsonValue;

public enum TopicStatus {
    SOLVED("SOLUCIONADO"),
    DISCUSSED("DISCUTIDO"),
    DELETED("ELIMINADO"),
    ACTIVE("ACTIVO");

    private String caregoriaEspanol;

    TopicStatus(String caregoriaEspanol){
        this.caregoriaEspanol = caregoriaEspanol;
    }

    @JsonValue
    public String getCategoriaEspanol() { return caregoriaEspanol; }

    public static TopicStatus fromEspanol(String text) {
        for (TopicStatus topicStatus : TopicStatus.values()) {
            if (topicStatus.caregoriaEspanol.equalsIgnoreCase(text)
                    || topicStatus.name().equalsIgnoreCase(text)) {
                return topicStatus;
            }
        }
        throw new IllegalArgumentException("No status found: " + text);
    }


}
