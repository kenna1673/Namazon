package order;

import address.Address;
import enums.OrderStatus;
import product.Product;

public class Order {
    private static Long idCount = 1L;
    private Product product;
    private Address destination;
    private OrderStatus status;

    public Order(Product product, Address destination, OrderStatus status) {
        this.product = product;
        this.destination = destination;
        this.status = status;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public Address getDestination() {
        return destination;
    }

    public void setDestination(Address destination) {
        this.destination = destination;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public void setStatus(OrderStatus status) {
        this.status = status;
    }

    @Override
    public String toString() {
        String productString = product.toString();
        String addressString = destination.toString();
        String statusString = status.getName();
        return String.format("%s %s %s", productString, addressString, statusString);
    }
}
