package com.example.restaurante.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.restaurante.dao.MesaRepository;
import com.example.restaurante.model.Mesa;

@Service
public class MesaService {

	
	
	@Autowired 
	private MesaRepository mesaRepository;
	
	public List<Mesa>listarmesas(){
		return mesaRepository.findAll();
	}
}
