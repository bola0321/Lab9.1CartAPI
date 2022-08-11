package co.grandcircus.cartapi.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import co.grandcircus.cartapi.exception.CartItemNotFoundException;
import co.grandcircus.cartapi.model.CartItem;
import co.grandcircus.cartapi.repository.CartItemRepository;
import org.springframework.http.HttpStatus;

@CrossOrigin(origins = "https://gc-express-tester.surge.sh/")


@RestController
public class CartItemController {
	
	@Autowired
	private CartItemRepository cartRepo;
	
	@GetMapping("/reset")	
	public String reset() {
		cartRepo.deleteAll();
		
		CartItem item = new CartItem("egusi", 7.99, 8);
		cartRepo.insert(item);
		
		item = new CartItem("ata rodo", .99 , 15);
		cartRepo.insert(item);
		
		item = new CartItem("garri", 4.99 , 3);
		cartRepo.insert(item);
		
		item = new CartItem("akara", 1.99 , 11);
		cartRepo.insert(item);
		
		item = new CartItem("plaintain", .50 , 6);
		cartRepo.insert(item);
		
		return "Cart data has been reset";
		
	}
		
	
	
	
	
	
	
	//GET 
	@GetMapping("cart-items")
	public List<CartItem> readAllItems (@RequestParam(required= false) String product,
								  		@RequestParam(required= false) Double maxPrice,
								  		@RequestParam(required= false) String prefix,
								  		@RequestParam(required= false) Integer pageSize){
		
		List<CartItem> queryResult;
		
		if (product != null && maxPrice != null) {
			queryResult = cartRepo.findByProductAndMaxPrice(maxPrice, product);
		}else if (prefix != null && maxPrice != null) {
			
			queryResult = cartRepo.findByPrefixAndMaxPrice(maxPrice,"^"+prefix);
			
		}else if (product!= null) {
			queryResult = cartRepo.findByProduct(product);
			
		}else if (prefix != null) {
			queryResult = cartRepo.findByProductStartingWith("^"+prefix);
		}else {
			queryResult = cartRepo.findAll();
		}
		
		if(pageSize != null) {
			queryResult = queryResult.subList(0, pageSize);
				
		}
		
		return queryResult;
				
	}
	
	//GET
	@GetMapping("/cart-items/{id}")
	public CartItem readOneItem(@PathVariable("id") String id) {
		
		return cartRepo.findById(id).orElseThrow(() -> new CartItemNotFoundException(id));
	}
	
	
	//POST
	@PostMapping("/cart-items")
	@ResponseStatus(HttpStatus.CREATED)
	public CartItem create(@RequestBody CartItem item) {
		cartRepo.insert(item);
		return item;
	}
	
	
	//PUT 
	@PutMapping("/cart-items/{id}")
	public CartItem updateById(@PathVariable("id")String id, @RequestBody CartItem item) {
		item.setId(id);
		return cartRepo.save(item);
	}
	
	//DELETE
	@DeleteMapping("/cart-items/{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void delete(@PathVariable("id")String id) {
		cartRepo.deleteById(id);
	}
	
	//GET
	@GetMapping("/cart-item/total-cost")
	public double cartMath() {
		double cartAddition = 0;
		for(CartItem item: cartRepo.findAll()) {
			cartAddition+= (item.getPrice() * item.getQuantity());
		}
		cartAddition = cartAddition * 1.06;
		return cartAddition;
		
	}
	
	//PATCH
	@PatchMapping("/cart-items/{id}/add")
	public CartItem updateItemQuantity(@PathVariable("id") String id, @RequestParam int count) {
		CartItem item = cartRepo.findById(id).orElseThrow(() -> new CartItemNotFoundException(id));
		item.setQuantity(item.getQuantity() + count);
		return cartRepo.save(item);
		
	}
	
	
	
	@ResponseBody
	@ExceptionHandler(CartItemNotFoundException.class)
	@ResponseStatus(HttpStatus.NOT_FOUND)
	String characterNotFoundHandler(CartItemNotFoundException ex) {
		return ex.getMessage();
	}
}
