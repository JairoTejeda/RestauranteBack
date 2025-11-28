package com.example.restaurante.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.restaurante.model.Categoria;
@Repository
public interface CategoriaRepository extends JpaRepository<Categoria, Long>{

}
