package com.alura.foro.hub.api.domain.topic;

import jakarta.persistence.*;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import com.alura.foro.hub.api.domain.user.User;
import com.alura.foro.hub.api.domain.course.Course;


@Table(name = "topics")
@Entity(name = "topic")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class Topic {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    private String message;

    @Column(name="created_date")
    private LocalDateTime createdDate;

    @Enumerated(EnumType.STRING)
    private TopicStatus status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "course_id")
    private Course course;

    public Topic(RegisterTopicData registerTopicData, User user, Course course) {
        this.title = registerTopicData.title();
        this.message = registerTopicData.message();
        this.createdDate = LocalDateTime.now();
        this.status = TopicStatus.ACTIVE;
        this.user = user;
        this.course = course;
    }


    public void actualizarTopicoConCurso(@Valid UpdateTopicData actualizarTopicoDTO, Course course) {
        if (actualizarTopicoDTO.title() != null){
            this.title = actualizarTopicoDTO.title();
        }
        if (actualizarTopicoDTO.message() != null){
            this.message = actualizarTopicoDTO.message();
        }
        if (actualizarTopicoDTO.status() != null){
            this.status = actualizarTopicoDTO.status();
        }
        if (actualizarTopicoDTO.course_id() != null){
            this.course = course;
        }
    }

    public void actualizarTopico(@Valid UpdateTopicData actualizarTopicoDTO) {
        if (actualizarTopicoDTO.title() != null){
            this.title = actualizarTopicoDTO.title();
        }
        if (actualizarTopicoDTO.message() != null){
            this.message = actualizarTopicoDTO.message();
        }
        if (actualizarTopicoDTO.status() != null){
            this.status = actualizarTopicoDTO.status();
        }
    }

    public void setStatus(TopicStatus status){

        this.status = status;
    }

    public void deleteTopic() {
        this.status = TopicStatus.DELETED;
    }
}
