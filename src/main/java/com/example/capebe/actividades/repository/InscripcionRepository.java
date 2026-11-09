package com.example.capebe.actividades.repository;



import com.example.capebe.actividades.entities.Inscripcion;
import org.springframework.data.jpa.repository.JpaRepository;


import java.util.List;


public interface InscripcionRepository extends JpaRepository<Inscripcion, Long> {

    // Para la app del invitado: "Mostrar Mis Actividades" (SELECT * FROM inscripciones WHERE rut = ?)
    List<Inscripcion> findByRut(String rut);

    // Para la app del operador de losa: Listar todos los inscritos en un evento particular
    List<Inscripcion> findByJornadaId(Long jornadaId);
    
    // (Opcional) Si quisieras buscar a los pasajeros de un evento en un horario específico
    List<Inscripcion> findByJornadaIdAndHorarioAsignado(Long jornadaId, String horarioAsignado);

    // ¡NUEVO!: Método que revisa si el usuario ya existe en ese vuelo
    boolean existsByJornadaIdAndRut(Long jornadaId, String rut);
}