package users;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class CustomerTest {

    private Customer customer;

    @BeforeEach
    public void setUp() {
        String firstName = "Bob";
        String lastName = "Smith";
        String email = "bob@gmail.com";
        String password = "12345";
        customer = new Customer(firstName, lastName, email, password);
    }

    @Test
    @DisplayName("parameterized constructor test")
    public void constructorTest01() {
        String expected = "Bob Smith bob@gmail.com 12345 1";
        String actual = customer.toString();
        Assertions.assertEquals(expected, actual);
    }
}
