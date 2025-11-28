package com.example.restaurante.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;


@Entity
@Table(name = "detallepedido")
public class DetallePedido {
	
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long iddetalle;
    
    @ManyToOne
    @JoinColumn(name = "idpedido", nullable = false)
    private Pedido pedido;

    @ManyToOne
    @JoinColumn(name = "idplato", nullable = false)
    private Plato plato;
    
    
    @Column(nullable = false)
    private int cantidad;
    
    @Column(nullable = false)
    private double preciounitario;

    // subtotal se calcula en base al precio * cantidad
    @Transient
    public double getSubtotal() {
        return preciounitario * cantidad;
    }

	public Long getIddetalle() {
		return iddetalle;
	}

	public void setIddetalle(Long iddetalle) {
		this.iddetalle = iddetalle;
	}

	public Pedido getPedido() {
		return pedido;
	}

	public void setPedido(Pedido pedido) {
		this.pedido = pedido;
	}

	public Plato getPlato() {
		return plato;
	}

	public void setPlato(Plato plato) {
		this.plato = plato;
	}

	public int getCantidad() {
		return cantidad;
	}

	public void setCantidad(int cantidad) {
		this.cantidad = cantidad;
	}

	public double getPreciounitario() {
		return preciounitario;
	}

	public void setPreciounitario(double preciounitario) {
		this.preciounitario = preciounitario;
	}

	

}
