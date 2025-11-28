package com.example.restaurante.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.restaurante.model.PlatosPorCategoria;

@Repository
public interface ListadoPlatosRepository extends JpaRepository<PlatosPorCategoria, Integer>{
	
	  @Procedure(procedureName = "ListarPlatos")
	    List<PlatosPorCategoria> listarPlatosPorCategoria(@Param("nombre_categoria") String nombreCategoria);


}
