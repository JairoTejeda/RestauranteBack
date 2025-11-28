package com.example.restaurante.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.restaurante.dao.MesaRepository;
import com.example.restaurante.dao.PedidoRepository;
import com.example.restaurante.dao.VentaRepository;
import com.example.restaurante.model.Mesa;
import com.example.restaurante.model.Pedido;
import com.example.restaurante.model.Venta;

import jakarta.transaction.Transactional;

@Service
public class RegistroEstadosService {
	
	
	@Autowired
	private VentaRepository ventaRepository;
	
    @Autowired
    private PedidoRepository pedidoRepository;
	
    @Autowired
    private MesaRepository mesaRepository;
	
	
    @Transactional
    public Pedido finalizarPedido(Long idPedido, Venta.MetodoPago metodoPago) {
        Pedido pedido = pedidoRepository.findById(idPedido)
                .orElseThrow(() -> new IllegalArgumentException("Pedido no encontrado con ID: " + idPedido));

        if (!pedido.getEstadopedido().equals("EN_PROCESO")) {
            throw new IllegalStateException("Solo se pueden finalizar pedidos en proceso");
        }

        // Calcular total del pedido
        double total = pedido.getDetalles().stream()
                .mapToDouble(d -> d.getCantidad() * d.getPreciounitario())
                .sum();

        // Crear venta
        Venta venta = new Venta();
        venta.setIdpedido(pedido);
        venta.setTotal(total);
        venta.setMetodopago(metodoPago);
        ventaRepository.save(venta);

        // Cambiar estado pedido
        pedido.setEstadopedido("FINALIZADO");
        pedidoRepository.save(pedido);

        // Liberar mesa si existe
        if (pedido.getIdmesa() != null) {
            Mesa mesa = pedido.getIdmesa();
            mesa.setEstadomesa(Mesa.EstadoMesa.LIBRE);
            mesaRepository.save(mesa);
        }

        return pedido;
    }
    
    
    @Transactional
    public Pedido cancelarPedido(Long idPedido) {
        Pedido pedido = pedidoRepository.findById(idPedido)
                .orElseThrow(() -> new IllegalArgumentException("Pedido no encontrado con ID: " + idPedido));

        if (!pedido.getEstadopedido().equals("EN_PROCESO")) {
            throw new IllegalStateException("Solo se pueden cancelar pedidos en proceso");
        }

        pedido.setEstadopedido("CANCELADO");
        pedidoRepository.save(pedido);

        if (pedido.getIdmesa() != null) {
            Mesa mesa = pedido.getIdmesa();
            mesa.setEstadomesa(Mesa.EstadoMesa.LIBRE);
            mesaRepository.save(mesa);
        }

        return pedido;
    }

}
