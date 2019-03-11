package pl.arbitrage.controller;





import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import pl.arbitrage.model.User;
import pl.arbitrage.repository.UserRepository;
import pl.arbitrage.service.UserService;

@Controller
public class UserController {
	

	private UserService userService;
	private UserRepository userRepository;

	@Autowired
	public void setUserRepository(UserRepository userRepository) {
		this.userRepository = userRepository;
	}
	
	@Autowired
	public void setUserService(UserService userService) {
		this.userService = userService;
	}
	
	
	
	//testy na thymeleaf do usuniecia
	@GetMapping("/register")
	public String register(Model model) {
		model.addAttribute("user", new User());
		return "registerForm";
	}

	@PostMapping("/register")
	public ResponseEntity<?> save(@RequestBody User user){
		if(user.getId() == null) {
			
			userService.addWithDefaultRole(user);
			
			

			return ResponseEntity.ok(null);
		}else {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        } 
		
	}
	
	@ResponseBody
	@GetMapping("/userbalance")
	public String getBalance() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    	String currentPrincipalName = authentication.getName();
    	System.out.println(currentPrincipalName);
		User user = userRepository.findByEmail(currentPrincipalName);
		
		ObjectMapper mapper = new ObjectMapper();
		ArrayNode arrayNode = mapper.createArrayNode();
		
		ObjectNode objectNode1 = mapper.createObjectNode();
        objectNode1.put("balans", user.getBalance());
        
        arrayNode.add(objectNode1);
		

    	return arrayNode.toString();
	}

	
}
