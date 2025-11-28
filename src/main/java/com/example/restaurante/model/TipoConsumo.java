package com.example.restaurante.model;


import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "tipoconsumo")
public class TipoConsumo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idtipoconsumo;

    @Enumerated(EnumType.STRING)  // Se guardará como una cadena (MESA o PARA_LLEVAR)
    private Tipo nombretipoconsumo;

    public enum Tipo {
        MESA, PARA_LLEVAR
    }

	public Long getIdtipoconsumo() {
		return idtipoconsumo;
	}

	public void setIdtipoconsumo(Long idtipoconsumo) {
		this.idtipoconsumo = idtipoconsumo;
	}

	public Tipo getNombretipoconsumo() {
		return nombretipoconsumo;
	}

	public void setNombretipoconsumo(Tipo nombretipoconsumo) {
		this.nombretipoconsumo = nombretipoconsumo;
	}


    
    
    

}
