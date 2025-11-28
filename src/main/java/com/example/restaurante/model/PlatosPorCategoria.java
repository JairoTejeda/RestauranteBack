package com.example.restaurante.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;


@Entity
@Table(name = "platos")
public class PlatosPorCategoria {
	
    @Id
    
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idplato;
    private String nombreplato;
    private String nombrecategoria;
    private Double precio;
    private String urlimagen;
	public Long getIdplato() {
		return idplato;
	}
	public void setIdplato(Long idplato) {
		this.idplato = idplato;
	}
	public String getNombreplato() {
		return nombreplato;
	}
	public void setNombreplato(String nombreplato) {
		this.nombreplato = nombreplato;
	}
	public String getNombrecategoria() {
		return nombrecategoria;
	}
	public void setNombrecategoria(String nombrecategoria) {
		this.nombrecategoria = nombrecategoria;
	}
	public Double getPrecio() {
		return precio;
	}
	public void setPrecio(Double precio) {
		this.precio = precio;
	}
	public String getUrlimagen() {
		return urlimagen;
	}
	public void setUrlimagen(String urlimagen) {
		this.urlimagen = urlimagen;
	}
    
    


}
