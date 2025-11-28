package com.example.restaurante.service;




import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.restaurante.dao.MesaRepository;
import com.example.restaurante.dao.PedidoRepository;
import com.example.restaurante.dao.PlatoRepository;
import com.example.restaurante.dao.TipoConsumoRepository;
import com.example.restaurante.model.DetallePedido;
import com.example.restaurante.model.Mesa;
import com.example.restaurante.model.Pedido;
import com.example.restaurante.model.Plato;
import com.example.restaurante.model.TipoConsumo;

import jakarta.transaction.Transactional;

@Service

public class PedidoService {
	
	

    @Autowired
    private PedidoRepository pedidoRepository;
    
    @Autowired
    private MesaRepository mesaRepository;
    
    @Autowired
    private PlatoRepository platoRepository;
    

    @Autowired
    private TipoConsumoRepository tipoConsumoRepository;
    
    
    @Autowired
    private WebSocketNotificacionService webSocketNotificacionService;
    

    @Transactional
    public Pedido registrarPedido(Pedido pedido) {

        // 1️⃣ Verificar tipo de consumo
        TipoConsumo tipoConsumo = tipoConsumoRepository.findById(pedido.getTipoconsumo().getIdtipoconsumo())
                .orElseThrow(() -> new RuntimeException("Tipo de consumo no encontrado"));
        pedido.setTipoconsumo(tipoConsumo);

        // 2️⃣ Si es MESA, verificar que la mesa exista y marcarla como OCUPADA
        if (tipoConsumo.getNombretipoconsumo() == TipoConsumo.Tipo.MESA) {
            if (pedido.getIdmesa() == null) {
                throw new RuntimeException("Debe indicar una mesa para consumo en MESA");
            }
            Mesa mesa = mesaRepository.findById(pedido.getIdmesa().getIdmesa())
                    .orElseThrow(() -> new RuntimeException("Mesa no encontrada"));

            mesa.setEstadomesa(Mesa.EstadoMesa.OCUPADA);
            mesaRepository.save(mesa);
            pedido.setIdmesa(mesa);
            
            // 🔴 Notificación en tiempo real
            webSocketNotificacionService.enviarCambioEstadoMesa(
                    mesa.getIdmesa(),
                    mesa.getEstadomesa().name()
            );
            
        } else {
            // Si es para llevar, no debe tener mesa
            pedido.setIdmesa(null);
        }

        // 3️⃣ Registrar detalles
        for (DetallePedido detalle : pedido.getDetalles()) {
            Plato plato = platoRepository.findById(detalle.getPlato().getIdplato())
                    .orElseThrow(() -> new RuntimeException("Plato no encontrado"));
            detalle.setPlato(plato);
            detalle.setPreciounitario(plato.getPrecio()); // se obtiene el precio del plato automáticamente
            detalle.setPedido(pedido); // importante para evitar el error de valor nulo
        }

        // 4️⃣ Guardar el pedido (gracias a Cascade.ALL guarda los detalles también)
        return pedidoRepository.save(pedido);
    }
    
    
    


}
