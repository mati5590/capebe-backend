package com.example.capebe.actividades.services;



import com.example.capebe.actividades.entities.Jornada;
import com.example.capebe.actividades.repository.JornadaRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class JornadaService {

    @Autowired
    private JornadaRepository jornadaRepository;

    public List<Jornada> obtenerTodasLasJornadas() {
        return jornadaRepository.findAll();
    }

    // Obtener solo las jornadas activas para mostrarlas en la App del invitado
    public List<Jornada> obtenerJornadasActivas() {
        return jornadaRepository.findByEstado("Activa");
    }

    // Crear una nueva jornada (Lo usaría un administrador web, por ejemplo)
    public Jornada crearJornada(Jornada jornada) {
        jornada.setEstado("Activa"); // Por defecto la creamos activa
        if (jornada.getCuposOcupados() == null) {
            jornada.setCuposOcupados(0); // Aseguramos que inicie en 0
        }
        return jornadaRepository.save(jornada);
    }

    public Jornada actualizarJornada(Long id, Jornada jornadaActualizada) {
        // Buscamos la jornada original en la base de datos
        Jornada jornadaExistente = jornadaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Jornada no encontrada"));

        // Sobreescribimos con los datos que llegan desde Postman (INCLUYENDO LOS NUEVOS)
        jornadaExistente.setNombre(jornadaActualizada.getNombre());
        jornadaExistente.setEstado(jornadaActualizada.getEstado());
        jornadaExistente.setFecha(jornadaActualizada.getFecha());
        jornadaExistente.setHoraInicio(jornadaActualizada.getHoraInicio());
        jornadaExistente.setHoraTermino(jornadaActualizada.getHoraTermino());
        jornadaExistente.setDescripcion(jornadaActualizada.getDescripcion());
        jornadaExistente.setUbicacion(jornadaActualizada.getUbicacion());
        jornadaExistente.setCuposTotales(jornadaActualizada.getCuposTotales());

        // Guardamos los cambios
        return jornadaRepository.save(jornadaExistente);
    }
}