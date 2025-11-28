package com.example.restaurante.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.restaurante.dao.ListadoPlatosRepository;
import com.example.restaurante.model.PlatosPorCategoria;



@Service
public class PlatosService {
	
	@Autowired
	
	private ListadoPlatosRepository platoRepository;
	
	
	
    @Transactional // <- transacción normal, permite ejecutar SP que MySQL considere modificables
    public List<PlatosPorCategoria> buscarPlatosPorCategoria(String nombreCategoria) {
        return platoRepository.listarPlatosPorCategoria(nombreCategoria);
    }
}

