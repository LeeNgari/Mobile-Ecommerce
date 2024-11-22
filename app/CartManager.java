import java.util.ArrayList;
import com.exam.model.Cart.CartItem

public class CartManager{
    private static CartManager instance;
    private List<CartItem> cartItems;

    private CartManager(){
        cartItems = new ArrayList<>();

    }

    public static synchronized  CartManager getInstance(){
        if(instance == null){
            instance = new CartManager();
        }
        return instance;
    }
    public List<CartItem> getCartItems() {
        return cartItems;
    }
    public void addItemToCart(CartItem item){
        for (CartItem cartItem : cartItems){
            if(cartItem.getTitle().equals(item.getTitle())){
                cartItem.setQuantity(cartItem.getQuantity + item.getQuantity());
                return;
            }
        }
        cartItems.add(item);
    }
    public void removeItemFromCart(CartItem item){
        cartItems.remove(item);
    }
    public void updateItemQuantity(CartItem item, int quantity)
    { for (CartItem cartItem : cartItems) {
        if (cartItem.getTitle().equals(item.getTitle()))
            { cartItem.setQuantity(quantity);
                if (quantity <= 0) {
                    cartItems.remove(cartItem);
                }
                return;
            }
    }
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
    }
}