package com.example.capebe.actividades.repository;



import com.example.capebe.actividades.entities.Jornada;
import org.springframework.data.jpa.repository.JpaRepository;


import java.util.List;


public interface JornadaRepository extends JpaRepository<Jornada, Long> {
    
    // Spring Boot crea el SQL automáticamente (SELECT * FROM jornadas WHERE estado = ?)
    List<Jornada> findByEstado(String estado);
}