package product;

import enums.ProductCategory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import product.Product;

public class ProductTest {

    private Product product;

    @BeforeEach
    public void setUp() {
        String name = "Football";
        ProductCategory category = ProductCategory.ATHLETICS;
        Double price = 10.00;
        product = new Product(name, category, price);
    }

    @Test
    @DisplayName("parameterized constructor test")
    public void constructorTest01() {
        String expected = "Football Athletics 10.00 1";
        String actual = product.toString();
        Assertions.assertEquals(expected, actual);
    }
}
