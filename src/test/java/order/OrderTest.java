package order;

import address.Address;
import enums.OrderStatus;
import enums.ProductCategory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import product.Product;

public class OrderTest {
    private Order order;
    private Address address;
    private Product product;
    private OrderStatus status;

    @BeforeEach
    public void setUp() {
        String name = "Football";
        ProductCategory category = ProductCategory.ATHLETICS;
        Double price = 10.00;
        product = new Product(name, category, price);

        String street = "Maple";
        String unit = "South";
        String city = "Chicago";
        String state = "Indiana";
        address = new Address(street, unit, city, state);

        status = OrderStatus.PENDING;
        order = new Order(product, address, status);
    }

    @Test
    @DisplayName("parameterized constructor test")
    public void constructorTest01() {
        String productString = product.toString();
        String addressString = address.toString();
        String statusString = status.getName();
        String expected = String.format("%s %s %s", productString, addressString, statusString);
        String actual = order.toString();
        Assertions.assertEquals(expected, actual);
    }
}
