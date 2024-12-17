package com.mosaeed.shoppingcart.service.cart;

import com.mosaeed.shoppingcart.exceptions.ResourceNotFoundException;
import com.mosaeed.shoppingcart.model.Cart;
import com.mosaeed.shoppingcart.model.CartItem;
import com.mosaeed.shoppingcart.repo.CartItemRepository;
import com.mosaeed.shoppingcart.repo.CartRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@RequiredArgsConstructor
@Service
public class CartService implements ICartService{

    // no need to @Autowired
    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;


    @Override
    public Cart getCart(Long id) {
        Cart cart = cartRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Cart Not Found!"));

        BigDecimal totalAmount = cart.getTotalAmount();
        cart.setTotalAmount(totalAmount);
        return cartRepository.save(cart);
    }


    @Override
    public void clearCart(Long id) {
        Cart cart = getCart(id);

        cartItemRepository.deleteAllByCartId(id);
        cart.getCartItems().clear();
        cartRepository.deleteById(id);

    }

    @Override
    public BigDecimal getTotalPrice(Long id) {
        Cart cart = getCart(id);

        return cart.getCartItems()
                .stream()
                .map(CartItem::getTotalPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

    }
}
