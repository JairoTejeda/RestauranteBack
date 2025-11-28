package com.example.restaurante.dao;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.restaurante.model.Pedido;


@Repository
public interface PedidoRepository extends JpaRepository<Pedido, Long>{


}