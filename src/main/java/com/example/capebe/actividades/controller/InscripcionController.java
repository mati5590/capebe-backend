package com.example.capebe.actividades.controller;




import com.example.capebe.actividades.entities.Inscripcion;
import com.example.capebe.actividades.services.InscripcionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/inscripciones")
@CrossOrigin("*")
public class InscripcionController {

    @Autowired
    private InscripcionService inscripcionService;

    // 1. INVITADO: Se inscribe a una jornada
    // POST: http://localhost:8082/api/inscripciones
    @PostMapping
    public ResponseEntity<?> inscribir(@RequestBody Inscripcion inscripcion) {
        try {
            // Intentamos guardar la inscripción
            Inscripcion nuevaInscripcion = inscripcionService.crearInscripcion(inscripcion);
            return ResponseEntity.ok(nuevaInscripcion);
        } catch (RuntimeException e) {
            // ¡NUEVO!: Si no hay cupos, capturamos el error y respondemos con HTTP 400
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // 2. INVITADO: Ver "Mis Actividades" en la app móvil
    // GET: http://localhost:8082/api/inscripciones/pasajero/{rut}
    @GetMapping("/pasajero/{rut}")
    public ResponseEntity<List<Inscripcion>> obtenerPorRut(@PathVariable String rut) {
        return ResponseEntity.ok(inscripcionService.obtenerPorRut(rut));
    }

    // 3. INVITADO: Firma con el dedo en la app (Habilita el QR)
    // PUT: http://localhost:8082/api/inscripciones/{id}/firmar
    @PutMapping("/{id}/firmar")
    public ResponseEntity<Inscripcion> firmar(@PathVariable Long id, @RequestBody Map<String, String> body) {
        // Recibimos un JSON pequeñito solo con la firma: { "firmaBase64": "iVBORw0KGgo..." }
        String firma = body.get("firmaBase64");
        return ResponseEntity.ok(inscripcionService.firmarInscripcion(id, firma));
    }

    // 4. OPERADOR (LOSA): Escanea QR, pesa y autoriza
    // PUT: http://localhost:8082/api/inscripciones/{id}/embarcar
    @PutMapping("/{id}/embarcar")
    public ResponseEntity<?> embarcar(@PathVariable Long id, @RequestBody Map<String, Double> body) {
        try {
            // Recibimos un JSON con el peso: { "pesoKilos": 75.5 }
            Double peso = body.get("pesoKilos");
            return ResponseEntity.ok(inscripcionService.registrarEmbarque(id, peso));
        } catch (RuntimeException e) {
            // Si el servicio detecta que NO ha firmado, mandamos un error 400 (Bad Request)
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}