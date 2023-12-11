package com.example.generadorcartas.Repository;

import com.example.generadorcartas.Entity.Carta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MisCartasRepository extends JpaRepository<Carta,Long> {
}
