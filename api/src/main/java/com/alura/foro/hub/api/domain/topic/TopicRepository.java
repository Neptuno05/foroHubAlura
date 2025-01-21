package com.alura.foro.hub.api.domain.topic;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TopicRepository extends JpaRepository<Topic, Long> {

    @SuppressWarnings("null")
    Page<Topic> findAll(Pageable pageable);

    Page<Topic> findAllByStatusIsNot(TopicStatus status, Pageable pageable);

    Boolean existsByTitleAndMessage(String title, String mensaje);

    Topic findByTitleAndMessage(String title, String message);
}
