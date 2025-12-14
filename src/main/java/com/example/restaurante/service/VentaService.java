package com.example.restaurante.service;

import java.io.ByteArrayOutputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.restaurante.dao.MesaRepository;
import com.example.restaurante.dao.PedidoRepository;
import com.example.restaurante.dao.VentaRepository;
import com.example.restaurante.model.DetallePedido;
import com.example.restaurante.model.Mesa;
import com.example.restaurante.model.Pedido;
import com.example.restaurante.model.TipoConsumo.Tipo;
import com.example.restaurante.model.Venta;

import jakarta.transaction.Transactional;

// PDFBOX
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDType1Font;

@Service
public class VentaService {

    @Autowired
    private VentaRepository ventaRepository;

    @Autowired
    private PedidoRepository pedidoRepository;

    @Autowired
    private MesaRepository mesaRepository;
    

    @Autowired
    private WebSocketNotificacionService webSocketNotificacionService;

    

    // REGISTRAR VENTA + CERRAR PEDIDO + GENERAR PDF
    // ======================================================

    @Transactional
    public byte[] registrarVenta(Long idPedido, Venta.MetodoPago metodoPago) throws Exception {

        // 1. Obtener pedido
        Pedido pedido = pedidoRepository.findById(idPedido)
                .orElseThrow(() -> new RuntimeException("Pedido no encontrado."));

        // 2. Verificar si ya está finalizado
        if (pedido.getEstadopedido().equals("FINALIZADO")) {
            throw new RuntimeException("El pedido ya está finalizado.");
        }

        // 3. Calcular total correctamente
        double total = pedido.getDetalles().stream()
                .mapToDouble(det -> det.getPreciounitario() * det.getCantidad())
                .sum();

        // 4. Registrar venta
        Venta venta = new Venta();
        venta.setPedido(pedido);
        venta.setMetodopago(metodoPago);
        venta.setTotal(total);
        venta.setFechaventa(LocalDateTime.now());

        ventaRepository.save(venta);

        // 5. Cambiar estado del pedido
        pedido.setEstadopedido("FINALIZADO");
        pedidoRepository.save(pedido);

        // 6. Si es consumo en mesa → liberar mesa
        if (pedido.getTipoconsumo().getNombretipoconsumo() == Tipo.MESA) {

            Mesa mesa = pedido.getIdmesa();
            if (mesa != null) {
                mesa.setEstadomesa(Mesa.EstadoMesa.LIBRE); // CORRECTO
                mesaRepository.save(mesa);
                
                // 🚀 Notificar a todos los clientes conectados vía WebSocket
                webSocketNotificacionService.enviarCambioEstadoMesa(
                        mesa.getIdmesa(),
                        mesa.getEstadomesa().name()
                    );


            }
        }

        // 7. Generar PDF
        return generarPDFBoleta(venta);
    }
    
    
    



    // =============================================
    // GENERAR PDF
    // =============================================
    private byte[] generarPDFBoleta(Venta venta) throws Exception {

        ByteArrayOutputStream out = new ByteArrayOutputStream();

        try (PDDocument doc = new PDDocument()) {

            PDPage page = new PDPage(PDRectangle.A4);
            doc.addPage(page);

            PDPageContentStream stream = new PDPageContentStream(doc, page);

            float y = 750;

            stream.setFont(PDType1Font.HELVETICA_BOLD, 18);
            stream.beginText();
            stream.newLineAtOffset(200, y);
            stream.showText("BOLETA ELECTRÓNICA");
            stream.endText();
            y -= 40;

            DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

            stream.setFont(PDType1Font.HELVETICA, 12);

            stream.beginText();
            stream.newLineAtOffset(50, y);
            stream.showText("Fecha: " + venta.getFechaventa().format(format));
            stream.endText();
            y -= 20;

            stream.beginText();
            stream.newLineAtOffset(50, y);
            stream.showText("Método de Pago: " + venta.getMetodopago());
            stream.endText();
            y -= 20;

            stream.beginText();
            stream.newLineAtOffset(50, y);
            stream.showText("Pedido N°: " + venta.getPedido().getIdpedido());
            stream.endText();
            y -= 40;

            // Detalle
            stream.setFont(PDType1Font.HELVETICA_BOLD, 14);
            stream.beginText();
            stream.newLineAtOffset(50, y);
            stream.showText("Detalle del Pedido:");
            stream.endText();
            y -= 25;

            stream.setFont(PDType1Font.HELVETICA, 12);

            for (DetallePedido d : venta.getPedido().getDetalles()) {
                stream.beginText();
                stream.newLineAtOffset(50, y);
                stream.showText(
                    d.getPlato().getNombreplato() +
                    " x" + d.getCantidad() +
                    " - S/ " + d.getSubtotal()
                );
                stream.endText();
                y -= 20;
            }

            y -= 20;
            stream.setFont(PDType1Font.HELVETICA_BOLD, 14);

            stream.beginText();
            stream.newLineAtOffset(50, y);
            stream.showText("TOTAL: S/ " + venta.getTotal());
            stream.endText();

            stream.close();
            doc.save(out);
        }

        return out.toByteArray();
    }
    
    
    
    
    public Pedido obtenerPedidoPorId(Long idPedido) {
        return pedidoRepository.findById(idPedido)
                .orElseThrow(() -> new RuntimeException("Pedido no encontrado."));
    }
}