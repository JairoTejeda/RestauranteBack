package com.example.restaurante.controller;



import com.example.restaurante.model.Mesa;
import com.example.restaurante.model.Pedido;
import com.example.restaurante.model.PlatosPorCategoria;
import com.example.restaurante.model.Venta;
import com.example.restaurante.service.MesaService;
import com.example.restaurante.service.PedidoService;
import com.example.restaurante.service.PlatosService;
import com.example.restaurante.service.RegistroEstadosService;
import com.example.restaurante.service.VentaService;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;






@RestController
@RequestMapping("/Restaurante")
public class MesaController {
	
	

    @Autowired
    private PedidoService pedidoService;
    
    @Autowired
    private RegistroEstadosService registroestadospedidoService;
    
    @Autowired
    private MesaService MesaService;
    
    @Autowired
    private PlatosService platoservice;
    
    @Autowired
    private VentaService ventaService;
    
    private final Path rootLocation = Paths.get("src/main/resources/imag");
    
    
    
    
    @GetMapping("/platos")
    public List<PlatosPorCategoria> listarPlatos(@RequestParam String nombreCategoria) {
        return platoservice.buscarPlatosPorCategoria(nombreCategoria);
    }

    
    @PostMapping("/registrarpedido")
    public ResponseEntity<Map<String, Object>> registrarPedido(@RequestBody Pedido pedido) {
        try {

            Pedido p = pedidoService.registrarPedido(pedido);

            // 🔥 Calcular subtotal total
            double subtotalTotal = p.getDetalles()
                    .stream()
                    .mapToDouble(det -> det.getSubtotal())
                    .sum();

            Map<String, Object> respuesta = new HashMap<>();
            respuesta.put("idPedido", p.getIdpedido());
            respuesta.put("estado", p.getEstadopedido());
            respuesta.put("fecha", p.getFechapedido().toString());
            respuesta.put("subtotal", subtotalTotal); // ⬅️ agregado

            return ResponseEntity.status(HttpStatus.CREATED).body(respuesta);

        } catch (Exception e) {

            Map<String, Object> error = new HashMap<>();
            error.put("mensaje", "Error: " + e.getMessage());

            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
        }
    }


    
    
    @PutMapping("/{id}/finalizar")
    public Pedido finalizarPedido(@PathVariable Long id, @RequestParam Venta.MetodoPago metodoPago) {
        return registroestadospedidoService.finalizarPedido(id, metodoPago);
    }
    
    @PutMapping("/{id}/cancelar")
    public Pedido cancelarPedido(@PathVariable Long id) {
        return registroestadospedidoService.cancelarPedido(id);
    }
    
    
    @GetMapping("/listadomesas")
    public List<Mesa> listarmesas(){
    	return MesaService.listarmesas();
    }
    
    
    @GetMapping("/imag/{nombreImagen}")
    public ResponseEntity<Resource> obtenerImagen(@PathVariable String nombreImagen) {
        try {
            // Resolvemos la ruta de la imagen
            Path file = rootLocation.resolve(nombreImagen).normalize();

            // Validamos que la ruta no permita acceso fuera del directorio permitido
            if (!file.startsWith(rootLocation)) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
            }

            Resource resource = new UrlResource(file.toUri());

            // Si el recurso existe y es legible, lo retornamos
            if (resource.exists() && resource.isReadable()) {
                // Detectamos dinámicamente el tipo MIME del archivo
                String mimeType = Files.probeContentType(file);
                if (mimeType == null) {
                    mimeType = "application/octet-stream"; // Tipo por defecto si no se detecta
                }

                return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType(mimeType))
                    .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + resource.getFilename() + "\"")
                    .body(resource);
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(null);
            }
        } catch (IOException e) {
            // Manejo de errores específicos relacionados con I/O
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(null);
        } catch (Exception e) {
            // Otros errores generales
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(null);
        }
    }
    
    
    
    @PostMapping("/pagarcuenta")
    public ResponseEntity<Map<String, Object>> registrarVenta(
            @RequestParam Long idPedido,
            @RequestParam Venta.MetodoPago metodoPago) {

        try {

            byte[] pdfBytes = ventaService.registrarVenta(idPedido, metodoPago);

            if (pdfBytes == null || pdfBytes.length == 0) {
                return ResponseEntity.badRequest().body(null);
            }

            // Convertir PDF a Base64
            String pdfBase64 = Base64.getEncoder().encodeToString(pdfBytes);

            // Crear respuesta JSON
            Map<String, Object> response = new HashMap<>();
            response.put("mensaje", "Venta registrada correctamente");
            response.put("pdfBase64", pdfBase64);
            response.put("idPedido", idPedido);

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            Map<String, Object> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }
    
    @GetMapping("/pedido/{idPedido}")
    public ResponseEntity<Map<String, Object>> obtenerPedido(@PathVariable Long idPedido) {
        Pedido pedido = ventaService.obtenerPedidoPorId(idPedido);

        double total = pedido.getDetalles().stream()
                .mapToDouble(d -> d.getPreciounitario() * d.getCantidad())
                .sum();

        Map<String, Object> response = new HashMap<>();
        response.put("idPedido", pedido.getIdpedido());
        response.put("total", total);

        return ResponseEntity.ok(response);
    }

    
    
    
    
}