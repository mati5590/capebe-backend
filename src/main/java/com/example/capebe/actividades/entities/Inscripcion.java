package com.example.capebe.actividades.entities;



import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Entity
@Table(name = "inscripciones")
@Data
public class Inscripcion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "jornada_id", nullable = false)
    private Jornada jornada;

    // --- REQUERIMIENTOS DEL PARTICIPANTE ---

    // a) RUT
    @Column(nullable = false)
    private String rut;

    // b) Nombres y Apellidos
    private String nombreCompleto;

    // c) Oficina Banco a la que pertenecen
    private String oficinaBanco; // <-- ¡NUEVO CAMPO!

    // d) Calidad de socio (Socio, Beneficiario, Invitado...)
    private String categoria; 

    // e) Número de teléfono móvil
    private String telefono; // <-- ¡NUEVO CAMPO!

    // --- CAMPOS OPERATIVOS DEL VUELO ---
    
    private String estadoConsentimiento; 
    
    @Column(columnDefinition = "TEXT")
    private String firmaBase64; 

    private String horarioAsignado; 
    private Double pesoKilos; 
    private String estadoEmbarque; 
    private LocalDateTime fechaHoraEscaneo; 
}