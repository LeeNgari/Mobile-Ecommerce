package utilities;

import java.util.ArrayList;
import java.util.List;

import adapter.CartAdapter;
import model.CartItem;

public class CartManager {
    private static CartManager instance;
    private List<CartItem> cartItems;
    private CartAdapter.OnCartUpdatedListener onCartUpdatedListener;

    private CartManager() {
        cartItems = new ArrayList<>();
    }

    public static synchronized CartManager getInstance() {
        if (instance == null) {
            instance = new CartManager();
        }
        return instance;
    }

    public List<CartItem> getCartItems() {
        return cartItems;
    }

    public void addItemToCart(CartItem item) {
        if (item.getQuantity() <= 0) return;
        for (CartItem cartItem : cartItems) {
            if (cartItem.getTitle().equals(item.getTitle())) {
                cartItem.setQuantity(cartItem.getQuantity() + item.getQuantity());
                notifyCartUpdated();
                return;
            }
        }
        cartItems.add(item);
        notifyCartUpdated();
    }

    public void removeItemFromCart(CartItem item) {
        cartItems.remove(item);
        notifyCartUpdated();
    }

    public void updateItemQuantity(CartItem item, int quantity) {
        CartItem targetItem = null;
        for (CartItem cartItem : cartItems) {
            if (cartItem.getTitle().equals(item.getTitle())) {
                if (quantity <= 0) {
                    targetItem = cartItem; // Mark for removal
                } else {
                    cartItem.setQuantity(quantity);
                }
                break;
            }
        }
        if (targetItem != null) {
            cartItems.remove(targetItem);
        }
        notifyCartUpdated();
    }

    public double calculateTotalPrice() {
        double total = 0;
        for (CartItem item : cartItems) {
            total += item.getTotalPrice();
        }
        return total;
    }

    public void clearCart() {
        cartItems.clear();
        notifyCartUpdated();
    }

    public void setOnCartUpdatedListener(CartAdapter.OnCartUpdatedListener listener) {
        this.onCartUpdatedListener = listener;
    }

    private void notifyCartUpdated() {
        if (onCartUpdatedListener != null) {
            onCartUpdatedListener.onCartUpdated();
        }
    }
}