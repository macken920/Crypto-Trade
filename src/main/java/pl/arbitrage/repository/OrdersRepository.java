package pl.arbitrage.repository;


import org.springframework.data.jpa.repository.JpaRepository;

import pl.arbitrage.model.Orders;

public interface OrdersRepository extends JpaRepository<Orders, Long>{


}
