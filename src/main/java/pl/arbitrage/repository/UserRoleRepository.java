package pl.arbitrage.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import pl.arbitrage.model.UserRole;

public interface UserRoleRepository extends JpaRepository<UserRole, Long> {
	//UserRole findByRole(String role);
}