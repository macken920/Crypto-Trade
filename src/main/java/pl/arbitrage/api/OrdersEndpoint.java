package pl.arbitrage.api;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import pl.arbitrage.data.operations.LiquidityProvider;
import pl.arbitrage.model.Orders;
import pl.arbitrage.model.User;
import pl.arbitrage.repository.LiquidityProviderRepository;
import pl.arbitrage.repository.OrdersRepository;
import pl.arbitrage.repository.UserRepository;

@RestController
public class OrdersEndpoint {
	
	private LiquidityProvider liquidityProvider;
	private OrdersRepository ordersRepository;
	private UserRepository userRepository;
	private LiquidityProviderRepository liquidityProviderRepository;
	
	@Autowired
	public OrdersEndpoint(OrdersRepository ordersRepository) {
		this.ordersRepository = ordersRepository;
	}
	@Autowired
	public void setUserRepository(UserRepository userRepository) {
		this.userRepository = userRepository;
	}
	@Autowired
	public void setOrdersRepository(OrdersRepository ordersRepository) {
		this.ordersRepository = ordersRepository;
	}
	@Autowired
	public void setLiquidityProviderRepository(LiquidityProviderRepository liquidityProviderRepository) {
		this.liquidityProviderRepository = liquidityProviderRepository;
	}
	
	
    @GetMapping("/api/orders/user")
    public List<Orders> getAll() {

    	Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    	String currentPrincipalName = authentication.getName();
    	
    	User user = userRepository.findByEmail(currentPrincipalName);
    	
        return user.getOrders();
        
    }
	@GetMapping("/api/orders/pricing")
	public List<LiquidityProvider> getPrice(){
		liquidityProvider = new LiquidityProvider("ltcbtc");
		liquidityProvider.setId((long) 1);
		liquidityProviderRepository.save(liquidityProvider);
		liquidityProvider = new LiquidityProvider("ethbtc");
		liquidityProvider.setId((long) 2);
		liquidityProviderRepository.save(liquidityProvider);
		
		return liquidityProviderRepository.findAll();
	}
	
	 @PostMapping("/api/orders/open")
	 public ResponseEntity<?> save(@RequestBody Orders orders){
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
     	String currentPrincipalName = authentication.getName();
     	System.out.println(currentPrincipalName);
 		User user = userRepository.findByEmail(currentPrincipalName);
 		
	        if(orders.getId() == null) {
	        	orders.setOpenClose(1);
	        	user.getOrders().add(orders);//one to many
	        	ordersRepository.save(orders);
	        	
	        	return ResponseEntity.ok(null);
	        } else {
	        	Orders update = ordersRepository.getOne(orders.getId());
	        	update.setOpenClose(0);
	        	
	        	
	        	if(update.getPair().toLowerCase().equals("ethbtc")) {
	        		liquidityProvider = new LiquidityProvider("ethbtc");
	        		
	        		update.setClosePrice(liquidityProvider.getPrice());
	        		update.setCloseTime(liquidityProvider.getTimeStamp());
	        		
	        	}else if(update.getPair().toLowerCase().equals("ltcbtc")) {
	        		liquidityProvider = new LiquidityProvider("ltcbtc");
	        		
	        		update.setClosePrice(liquidityProvider.getPrice());
	        		update.setCloseTime(liquidityProvider.getTimeStamp());
	        		
	        	}
	        	
	        	if(update.getDirection().equals("Sell")) {
	        		update.setProfit(update.getOpenPrice() - update.getClosePrice());
	        	}else if(update.getDirection().equals("Buy")) {
	        		update.setProfit(update.getClosePrice() - update.getOpenPrice());
	        	}
	        	
	        	user.setBalance(user.getBalance() + update.getProfit());
	        	userRepository.save(user);
	        	ordersRepository.save(update);
	        	
	        	return ResponseEntity.ok(null);
	        }
		 
	 }

}
