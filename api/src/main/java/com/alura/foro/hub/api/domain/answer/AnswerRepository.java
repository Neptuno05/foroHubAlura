package com.alura.foro.hub.api.domain.answer;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AnswerRepository extends JpaRepository<Answer, Long> {

    Page<Answer> findAllByTopicId(Long topicoId, Pageable pageable);

    Page<Answer> findAllByUserId(Long usuarioId, Pageable pageable);

    List<Answer> findByTopicId(Long id);
}
