package pl.arbitrage.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import pl.arbitrage.data.operations.LiquidityProvider;

public interface LiquidityProviderRepository extends JpaRepository<LiquidityProvider, Long>  {

}
