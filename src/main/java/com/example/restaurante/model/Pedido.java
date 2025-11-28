package com.example.restaurante.model;

import java.time.LocalDateTime;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;



@Entity
@Table(name = "pedidos")
public class Pedido {
	
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idpedido;
    
    @Column(nullable = false)
    private LocalDateTime fechapedido = LocalDateTime.now();

    @ManyToOne
    @JoinColumn(name = "idtipoconsumo", nullable = false)
    private TipoConsumo tipoconsumo;

    @ManyToOne
    @JoinColumn(name = "idmesa", nullable = true) // si peude ser nul
    private Mesa idmesa;

    @Column(nullable = false)
    private String estadopedido = "EN_PROCESO"; // estado automático

    private String observaciones;

    @OneToMany(mappedBy = "pedido", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<DetallePedido> detalles;

	public Long getIdpedido() {
		return idpedido;
	}

	public void setIdpedido(Long idpedido) {
		this.idpedido = idpedido;
	}

	public LocalDateTime getFechapedido() {
		return fechapedido;
	}

	public void setFechapedido(LocalDateTime fechapedido) {
		this.fechapedido = fechapedido;
	}

	public TipoConsumo getTipoconsumo() {
		return tipoconsumo;
	}

	public void setTipoconsumo(TipoConsumo tipoconsumo) {
		this.tipoconsumo = tipoconsumo;
	}

	public Mesa getIdmesa() {
		return idmesa;
	}

	public void setIdmesa(Mesa idmesa) {
		this.idmesa = idmesa;
	}

	public String getEstadopedido() {
		return estadopedido;
	}

	public void setEstadopedido(String estadopedido) {
		this.estadopedido = estadopedido;
	}

	public String getObservaciones() {
		return observaciones;
	}

	public void setObservaciones(String observaciones) {
		this.observaciones = observaciones;
	}

	public List<DetallePedido> getDetalles() {
		return detalles;
	}

	public void setDetalles(List<DetallePedido> detalles) {
		this.detalles = detalles;
	}


    




}
