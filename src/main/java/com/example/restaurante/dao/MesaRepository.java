package com.example.restaurante.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.restaurante.model.Mesa;
@Repository
public interface MesaRepository extends JpaRepository<Mesa, Long> {

}
