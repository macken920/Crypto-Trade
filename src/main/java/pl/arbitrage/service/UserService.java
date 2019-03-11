package pl.arbitrage.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import pl.arbitrage.model.User;
import pl.arbitrage.model.UserRole;
import pl.arbitrage.repository.UserRepository;
import pl.arbitrage.repository.UserRoleRepository;

@Service
public class UserService {

	private static final String DEFAULT_ROLE = "ROLE_USER";
	private UserRepository userRepository;
	private UserRoleRepository roleRepository;
	private PasswordEncoder passwordEncoder;
	
	@Autowired
	public UserService(PasswordEncoder passwordEncoder) {
		this.passwordEncoder = passwordEncoder;
	}

	@Autowired
	public void setUserRepository(UserRepository userRepository) {
		this.userRepository = userRepository;
	}
	
	@Autowired
	public void setRoleRepository(UserRoleRepository roleRepository) {
		this.roleRepository = roleRepository;
	}
	

	public void addWithDefaultRole(User user) {
		
		
		
		
		
		UserRole userRole = new UserRole();
		userRole.setRole(DEFAULT_ROLE);
		userRole.setUsername(user.getEmail());
		
		user.setBalance(100);
		user.setEnabled(1);
		user.getRoles().add(userRole);//Many to many
		String passwordHash = passwordEncoder.encode(user.getPassword());
		user.setPassword(passwordHash);
		
		roleRepository.save(userRole);
		userRepository.save(user);
	}

	
	
}
