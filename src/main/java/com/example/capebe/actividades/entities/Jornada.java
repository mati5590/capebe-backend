package com.example.capebe.actividades.entities;




import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Table(name = "jornadas")
@Data
public class Jornada {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nombre; 

    private String descripcion; 

    @Column(nullable = false)
    private LocalDate fecha;
    
    @Column(name = "hora_inicio", nullable = false)
    private LocalTime horaInicio; 
    
    @Column(name = "hora_termino", nullable = false)
    private LocalTime horaTermino; 
    
    // Ubicación para que el invitado sepa dónde es el evento
    private String ubicacion; 
    
    private String estado; 

    // --- NUEVOS CAMPOS: CONTROL DE CUPOS ---
    
    @Column(name = "cupos_totales", nullable = false)
    private Integer cuposTotales = 0; // Ej: 4 espacios en el avión

    @Column(name = "cupos_ocupados", nullable = false)
    private Integer cuposOcupados = 0; // Se inicializa en 0

    // Método de conveniencia para validar el negocio rápidamente
    public boolean tieneCuposDisponibles() {
        // Asegura que no lance NullPointerException
        int ocupados = (cuposOcupados != null) ? cuposOcupados : 0;
        int totales = (cuposTotales != null) ? cuposTotales : 0;
        return ocupados < totales;
    }
}