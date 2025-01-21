package com.alura.foro.hub.api.domain.answer;


import com.alura.foro.hub.api.domain.topic.Topic;
import com.alura.foro.hub.api.domain.user.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "answers")
@Entity(name = "Answer")
@EqualsAndHashCode(of = "id")
public class Answer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String message;

    @Column(name="created_date")
    private LocalDateTime createdDate;

    private Boolean solution;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="topic_id")
    private Topic topic;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "an_user_id")
    private User user;


    public Answer(RegisterAnswerData answerData, User user, Topic topic) {
        this.message = answerData.message();
        this.createdDate = LocalDateTime.now();
        this.solution = false;
        this.user = user;
        this.topic = topic;
    }

    public void actualizarRespuesta(UpdateAnswerData updateAnswer){
        if(updateAnswer.message() != null){
            this.message = updateAnswer.message();
        }
        if (updateAnswer.solution() != null){
            this.solution = updateAnswer.solution();
        }
    }
}
