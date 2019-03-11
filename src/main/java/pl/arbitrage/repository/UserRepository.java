package pl.arbitrage.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import pl.arbitrage.model.User;

public interface UserRepository extends JpaRepository<User, Long> {

	
	public User findByEmail(String username);
}