package com.example.restaurante.model;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "mesas")
public class Mesa {
	
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idmesa;

    @Column(name = "numeromesa", nullable = false, unique = true)
    private String numeromesa;

    @Enumerated(EnumType.STRING)
    @Column(name = "estadomesa", nullable = false)
    private EstadoMesa estadomesa = EstadoMesa.LIBRE;;

    public enum EstadoMesa {
        LIBRE, OCUPADA
    }

	public Long getIdmesa() {
		return idmesa;
	}

	public void setIdmesa(Long idmesa) {
		this.idmesa = idmesa;
	}

	public String getNumeromesa() {
		return numeromesa;
	}

	public void setNumeromesa(String numeromesa) {
		this.numeromesa = numeromesa;
	}

	public EstadoMesa getEstadomesa() {
		return estadomesa;
	}

	public void setEstadomesa(EstadoMesa estadomesa) {
		this.estadomesa = estadomesa;
	}

    
    
    
    

}
