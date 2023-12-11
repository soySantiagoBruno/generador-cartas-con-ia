package com.example.generadorcartas.Entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

@Entity @Data @NoArgsConstructor
@Table(name="cartas")
public class Carta {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(name="nombre")
    String nombre;

    @Column(name="descripcion")
    String descripcion;

    @Column(name="poder")
    int poder;

    @Column(name="defensa")
    int defensa;

    @Column(name="urlImagen")
    String urlImagen;


    //constructor sin id (para poder mostrar las cartas generadas con la api para las cuales no hace falta el id)
    public Carta(String nombre, String descripcion, int poder, int defensa, String urlImagen) {
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.poder = poder;
        this.defensa = defensa;
        this.urlImagen = urlImagen;
    }
}
