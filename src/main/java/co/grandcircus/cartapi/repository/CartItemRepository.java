package co.grandcircus.cartapi.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import co.grandcircus.cartapi.model.CartItem;

public interface CartItemRepository extends MongoRepository<CartItem, String>{

	List<CartItem>findByProduct(String product);
	
	List <CartItem> findByProductStartingWith(String regexp);
	
	@Query("{'price': {$lte: ?0}}")
	List<CartItem> findByMaxPrice(double maxPrice);
	
	@Query("$query:{}, $limit: 2")
	List<CartItem>findAll(int limit);
	
	@Query("{'price': {$lte: ?0}, 'product':?1}")
	List<CartItem> findByProductAndMaxPrice(double maxPrice, String product);
	
	@Query("{'price': {$lte: ?0}, 'prefix': {$regex: ?1}")
	List<CartItem> findByPrefixAndMaxPrice(double maxPrice, String prefix);
	
	
}
