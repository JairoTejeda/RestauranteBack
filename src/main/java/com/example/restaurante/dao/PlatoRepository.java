package com.example.restaurante.dao;



import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.stereotype.Repository;

import com.example.restaurante.model.Plato;
@Repository
public interface PlatoRepository  extends JpaRepository<Plato, Long>{
	
}
