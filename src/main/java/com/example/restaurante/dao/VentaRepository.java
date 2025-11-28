package com.example.restaurante.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.restaurante.model.Venta;

@Repository
public interface VentaRepository extends JpaRepository<Venta, Long>{

}
