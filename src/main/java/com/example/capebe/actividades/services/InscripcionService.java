package com.example.capebe.actividades.services;



import com.example.capebe.actividades.entities.Inscripcion;
import com.example.capebe.actividades.entities.Jornada;
import com.example.capebe.actividades.repository.InscripcionRepository;
import com.example.capebe.actividades.repository.JornadaRepository;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class InscripcionService {

    @Autowired
    private InscripcionRepository inscripcionRepository;

    @Autowired
    private JornadaRepository jornadaRepository; // <-- ¡NUEVO! Necesitamos acceder a la jornada

    // 1. INVITADO: Se inscribe a una jornada
    @Transactional // <-- ¡IMPORTANTE! Asegura que ambos guardados ocurran juntos
    public Inscripcion crearInscripcion(Inscripcion inscripcion) {

        if (inscripcionRepository.existsByJornadaIdAndRut(inscripcion.getJornada().getId(), inscripcion.getRut())) {
            throw new RuntimeException("Ya te encuentras inscrito en esta jornada.");
        }
        
        // 1. Buscar la jornada real en la base de datos
        Jornada jornada = jornadaRepository.findById(inscripcion.getJornada().getId())
                .orElseThrow(() -> new RuntimeException("Jornada no encontrada"));

        // 2. REGLA DE NEGOCIO: ¿Quedan cupos?
        if (!jornada.tieneCuposDisponibles()) {
            throw new RuntimeException("Lo sentimos, esta jornada ya no tiene cupos disponibles.");
        }

        // 3. Reglas de negocio iniciales para el pasajero:
        inscripcion.setJornada(jornada);
        inscripcion.setEstadoConsentimiento("Pendiente");
        inscripcion.setEstadoEmbarque("No Presentado");
        
        // 4. Guardamos la inscripción
        Inscripcion nuevaInscripcion = inscripcionRepository.save(inscripcion);

        // 5. Restamos el cupo sumando +1 a los ocupados y actualizamos la jornada
        jornada.setCuposOcupados(jornada.getCuposOcupados() + 1);
        jornadaRepository.save(jornada);

        return nuevaInscripcion;
    }

    // 2. INVITADO: Ver "Mis Actividades" en la app
    public List<Inscripcion> obtenerPorRut(String rut) {
        return inscripcionRepository.findByRut(rut);
    }

    // 3. INVITADO: Dibuja la firma y acepta los términos (Esto habilita su QR)
    public Inscripcion firmarInscripcion(Long id, String firmaBase64) {
        Inscripcion inscripcion = inscripcionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Inscripción no encontrada"));
        
        inscripcion.setFirmaBase64(firmaBase64);
        inscripcion.setEstadoConsentimiento("Firmado");
        
        return inscripcionRepository.save(inscripcion);
    }

    // 4. OPERADOR (LOSA): Escanea QR, ingresa peso y autoriza embarque
    public Inscripcion registrarEmbarque(Long id, Double peso) {
        Inscripcion inscripcion = inscripcionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Inscripción no encontrada"));
        
        // Regla de seguridad: No puede embarcar si no ha firmado
        if (!"Firmado".equals(inscripcion.getEstadoConsentimiento())) {
            throw new RuntimeException("El pasajero no ha firmado el consentimiento legal.");
        }

        inscripcion.setPesoKilos(peso);
        inscripcion.setEstadoEmbarque("En Pista");
        inscripcion.setFechaHoraEscaneo(LocalDateTime.now()); // Registramos la hora exacta
        
        return inscripcionRepository.save(inscripcion);
    }
}