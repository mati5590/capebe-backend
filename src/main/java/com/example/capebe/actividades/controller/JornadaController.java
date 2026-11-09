package com.example.capebe.actividades.controller;



import com.example.capebe.actividades.entities.Jornada;
import com.example.capebe.actividades.services.JornadaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/jornadas")
@CrossOrigin("*") // Importante para que tu app móvil no tenga problemas de CORS
public class JornadaController {

    @Autowired
    private JornadaService jornadaService;

    // GET: http://localhost:8082/api/jornadas (Trae TODAS las jornadas)
    @GetMapping
    public ResponseEntity<List<Jornada>> obtenerTodas() {
        return ResponseEntity.ok(jornadaService.obtenerTodasLasJornadas());
    }

    // GET: http://localhost:8082/api/jornadas/activas
    @GetMapping("/activas")
    public ResponseEntity<List<Jornada>> obtenerActivas() {
        return ResponseEntity.ok(jornadaService.obtenerJornadasActivas());
    }

    // POST: http://localhost:8082/api/jornadas
    @PostMapping
    public ResponseEntity<Jornada> crear(@RequestBody Jornada jornada) {
        return ResponseEntity.ok(jornadaService.crearJornada(jornada));
    }

    // PUT: http://localhost:8082/api/jornadas/{id}
    @PutMapping("/{id}")
    public ResponseEntity<Jornada> actualizar(@PathVariable Long id, @RequestBody Jornada jornada) {
        try {
            return ResponseEntity.ok(jornadaService.actualizarJornada(id, jornada));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build(); // Devuelve 404 si el ID no existe
        }
    }
}