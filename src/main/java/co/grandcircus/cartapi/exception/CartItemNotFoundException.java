package co.grandcircus.cartapi.exception;

public class CartItemNotFoundException extends RuntimeException{
	
	private static final long serialVersionUID = 1L;
	
	
	public CartItemNotFoundException(String id) {
		super("Could not find cart item with id " + id);
	}

}
