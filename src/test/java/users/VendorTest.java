package users;

import address.Address;
import enums.OrderStatus;
import enums.ProductCategory;
import exceptions.ProductNotAvailableException;
import org.junit.jupiter.api.*;
import product.Product;

import java.util.ArrayList;

public class VendorTest {
    private Vendor vendor;
    private Product product;
    private Product product01;
    private order.Order order;
    private order.Order order01;
    private Address address;

    @BeforeEach
    public void setUp() {
        String brandName = "Java";
        String firstName = "Bob";
        String lastName = "Smith";
        String email = "bob@gmail.com";
        String password = "12345";
        vendor = new Vendor(brandName, firstName, lastName, email, password);

        String name = "Football";
        ProductCategory category = ProductCategory.ATHLETICS;
        Double price = 10.00;
        product = new Product(name, category, price);

        String name01 = "Fridge";
        ProductCategory category01 = ProductCategory.HOME_APPLIANCE;
        Double price01 = 1000.00;
        product01 = new Product(name01, category01, price01);

        String street = "Maple";
        String unit = "South";
        String city = "Chicago";
        String state = "Indiana";
        address = new Address(street, unit, city, state);

        OrderStatus status = OrderStatus.PENDING;
        order = new order.Order(product, address, status);
        order01 = new order.Order(product01, address, status);
    }

    @Test
    @DisplayName("parameterized constructor test")
    public void constructorTest01() {
        String expected = "Java Bob Smith bob@gmail.com 12345 1";
        String actual = vendor.toString();
        Assertions.assertEquals(expected, actual);
    }

    @Test
    @DisplayName("add product when product not in inventory test")
    public void addProductTest01() {
        int expected = 1;
        vendor.addProduct(product);
        int actual = vendor.getInventory().size();
        Assertions.assertEquals(expected, actual);
    }

    @Test
    @DisplayName("add product when product in inventory test")
    public void addProductTest02() {
        int expected = 2;
        vendor.addProduct(product);
        vendor.addProduct(product);
        int actual = vendor.getInventory().get(product);
        Assertions.assertEquals(expected, actual);
    }

    @Test
    @DisplayName("get number in stock test")
    public void getNumberInStockTest01() {
        int expected = 1;
        vendor.addProduct(product);
        int actual = vendor.getNumberInStock(product);
        Assertions.assertEquals(expected, actual);
    }

    @Test
    @DisplayName("remove product when empty test")
    public void removeProductTest01() {
        boolean actual = vendor.removeProduct(product);
        Assertions.assertFalse(actual);
    }

    @Test
    @DisplayName("remove product when not in non-empty inventory test")
    public void removeProductTest02() {
        vendor.addProduct(product01);
        boolean actual = vendor.removeProduct(product);
        Assertions.assertFalse(actual);
    }

    @Test
    @DisplayName("remove product when is in non-empty inventory test")
    public void removeProductTest03() {
        vendor.addProduct(product);
        boolean actual = vendor.removeProduct(product);
        Assertions.assertTrue(actual);
    }

    @Test
    @DisplayName("add order test")
    public void addOrderTest01() {
        int expected = 1;
        vendor.addOrder(order);
        int actual = vendor.getOrders().size();
        Assertions.assertEquals(expected, actual);
    }

    @Test
    @DisplayName("cancel order when orders is empty test")
    public void cancelOrderTest01() {
        boolean actual = vendor.cancelOrder(order);
        Assertions.assertFalse(actual);
    }

    @Test
    @DisplayName("cancel order when orders is non-empty but order is not in orders")
    public void cancelOrderTest02() {
        vendor.addOrder(order);
        boolean actual = vendor.cancelOrder(order01);
        Assertions.assertFalse(actual);
    }

    @Test
    @DisplayName("cancel order when orders is non-empty and order is in orders")
    public void cancelOrderTest03() {
        vendor.addOrder(order);
        boolean actual = vendor.cancelOrder(order);
        Assertions.assertTrue(actual);
    }

    @Test
    @DisplayName("add product to showcase test")
    public void addProductToShowcaseTest01() {
        vendor.addProductToShowcase(product, 0);
        boolean actual = vendor.getShowCase()[0].equals(product);
        Assertions.assertTrue(actual);
    }

    @Test
    @DisplayName("search by category when there is a match test")
    public void searchByCategoryTest01() {
        vendor.addProduct(product);
        vendor.addProduct(product01);
        ArrayList<Product> matches = vendor.searchByCategory(ProductCategory.ATHLETICS);
        int expected = 1;
        int actual = matches.size();
        Assertions.assertEquals(expected, actual);
    }

    @Test
    @DisplayName("search by category when there is NOT a match test")
    public void searchByCategoryTest02() {
        vendor.addProduct(product);
        vendor.addProduct(product01);
        ArrayList<Product> matches = vendor.searchByCategory(ProductCategory.CLOTHING);
        int expected = 0;
        int actual = matches.size();
        Assertions.assertEquals(expected, actual);
    }

    @Test
    @DisplayName("search by category test - product that matches category is in inventory, but not in stock")
    public void searchByCategoryTest03() {
        vendor.addProduct(product);
        vendor.getInventory().put(product, 0); // simulates a purchase of product
        int expected = 0;
        ArrayList<Product> matches = vendor.searchByCategory(ProductCategory.ATHLETICS);
        int actual = matches.size();
        Assertions.assertEquals(expected, actual);
    }

    @Test
    @DisplayName("search by name when there is a match test")
    public void searchByNameTest01() {
        vendor.addProduct(product);
        vendor.addProduct(product01);
        ArrayList<Product> matches = vendor.searchByName("Football");
        int expected = 1;
        int actual = matches.size();
        Assertions.assertEquals(expected, actual);
    }

    @Test
    @DisplayName("search by name when there is NOT a match test")
    public void searchByNameTest02() {
        vendor.addProduct(product);
        vendor.addProduct(product01);
        ArrayList<Product> matches = vendor.searchByName("Baseball");
        int expected = 0;
        int actual = matches.size();
        Assertions.assertEquals(expected, actual);
    }

    @Test
    @DisplayName("search by name test - product that matches name is in inventory, but not in stock")
    public void searchByNameTest03() {
        vendor.addProduct(product);
        vendor.getInventory().put(product, 0); // simulates a purchase of product
        int expected = 0;
        ArrayList<Product> matches = vendor.searchByName("Football");
        int actual = matches.size();
        Assertions.assertEquals(expected, actual);
    }

    @Test
    @DisplayName("purchase when not in stock test")
    public void purchaseTest01() {
        vendor.addProduct(product);
        Assertions.assertThrows(ProductNotAvailableException.class, ()->{
            vendor.purchase(product01, address);
        });
    }

    @Test
    @DisplayName("purchase when in stock test")
    public void purchaseTest02() throws ProductNotAvailableException {
        vendor.addProduct(product);
        boolean actual = vendor.purchase(product, address);
        Assertions.assertTrue(actual);
    }

    @Test
    @DisplayName("purchase stock updated test")
    public void purchaseTest03() throws ProductNotAvailableException {
        vendor.addProduct(product);
        vendor.purchase(product, address);
        int expected = 0;
        int actual = vendor.getNumberInStock(product);
        Assertions.assertEquals(expected, actual);
    }

    @Test
    @DisplayName("purchase orders updated test")
    public void purchaseTest04() throws ProductNotAvailableException {
        vendor.addProduct(product);
        vendor.purchase(product, address);
        int expected = 1;
        int actual = vendor.getOrders().size();
        Assertions.assertEquals(expected, actual);
    }

    @Test
    @DisplayName("display all orders test")
    public void displayAllOrdersTest01() {
        vendor.addOrder(order);
        vendor.addOrder(order01);
        String expected = order.toString() + "\n" + order01.toString() + "\n";
        String actual = vendor.displayAllOrders();
        Assertions.assertEquals(expected, actual);
    }
}
