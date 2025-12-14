package com.example.restaurante.model;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "ventas")
public class Venta {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long idventa;
    @ManyToOne
    @JoinColumn(name = "idpedido", nullable = false)
    private Pedido pedido;
    @Column(nullable = false)
    private LocalDateTime fechaventa = LocalDateTime.now();
    @Column(nullable = false)
    private double  total;
	
    @Enumerated(EnumType.STRING)
    @Column(name = "metodopago", nullable = false)
	private MetodoPago metodopago;
	
	
    public enum MetodoPago {
        EFECTIVO, TARJETA, YAPE, PLIN
    }


	public Long getIdventa() {
		return idventa;
	}


	public void setIdventa(Long idventa) {
		this.idventa = idventa;
	}


	public Pedido getPedido() {
		return pedido;
	}


	public void setPedido(Pedido pedido) {
		this.pedido = pedido;
	}


	public LocalDateTime getFechaventa() {
		return fechaventa;
	}


	public void setFechaventa(LocalDateTime fechaventa) {
		this.fechaventa = fechaventa;
	}


	public double getTotal() {
		return total;
	}


	public void setTotal(double total) {
		this.total = total;
	}


	public MetodoPago getMetodopago() {
		return metodopago;
	}


	public void setMetodopago(MetodoPago metodopago) {
		this.metodopago = metodopago;
	}
    
    
    



    

}
