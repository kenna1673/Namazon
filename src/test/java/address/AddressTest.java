package address;

import address.Address;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class AddressTest {

    private Address address;

    @BeforeEach
    public void setUp() {
        String street = "Maple";
        String unit = "South";
        String city = "Chicago";
        String state = "Indiana";
        address = new Address(street, unit, city, state);
    }

    @Test
    @DisplayName("parameterized constructor test")
    public void constructorTest01() {
        String expected = "Maple South Chicago Indiana";
        String actual = address.toString();
        Assertions.assertEquals(expected, actual);
    }
}
