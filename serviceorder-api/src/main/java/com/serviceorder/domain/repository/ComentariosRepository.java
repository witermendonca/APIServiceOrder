package com.serviceorder.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.serviceorder.domain.model.Comentarios;

@Repository
public interface ComentariosRepository extends JpaRepository<Comentarios, Long>{

}
