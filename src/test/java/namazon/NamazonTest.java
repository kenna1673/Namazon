package namazon;

import account.Account;
import exceptions.AccountAuthenticationException;
import exceptions.AccountCreationException;
import exceptions.VendorNotFoundException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import users.Customer;
import users.Vendor;

import java.io.File;
import java.io.IOException;

public class NamazonTest {
    private Namazon namazon;
    private Customer customer;
    private Customer customer01;
    private Vendor vendor;
    private Vendor vendor01;

    @BeforeEach
    public void setUp() {
        String firstName = "Bob";
        String lastName = "Smith";
        String email = "bob@gmail.com";
        String password = "12345";
        customer = new Customer(firstName, lastName, email, password);

        String firstName01 = "Sarah";
        String lastName01 = "Smith";
        String email01 = "sarah@gmail.com";
        String password01 = "67890";
        customer01 = new Customer(firstName01, lastName01, email01, password01);

        String brandName = "Java";
        String brandName01 = "Python";
        vendor = new Vendor(brandName, firstName, lastName, email, password);
        vendor01 = new Vendor(brandName01, firstName01, lastName01, email01, password01);

        namazon = new Namazon();
    }

    @Test
    @DisplayName("add customer test")
    public void addCustomerTest01() {
        namazon.addCustomer(customer);
        int expected = 1;
        int actual = namazon.getCustomers().size();
        Assertions.assertEquals(expected, actual);
    }

    @Test
    @DisplayName("add vendor test")
    public void addVendorTest01() {
        namazon.addVendor(vendor);
        int expected = 1;
        int actual = namazon.getVendors().size();
        Assertions.assertEquals(expected, actual);
    }

    @Test
    @DisplayName("sign in successful test customer")
    public void signInTest01() throws AccountAuthenticationException {
        namazon.addCustomer(customer);
        String email = customer.getEmail();
        String password = customer.getPassword();
        Account expected = customer;
        Account actual = namazon.signIn(email, password);
        Assertions.assertEquals(expected, actual);
    }

    @Test
    @DisplayName("sign in successful test vendor")
    public void signInTest02() throws AccountAuthenticationException {
        namazon.addVendor(vendor);
        String email = vendor.getEmail();
        String password = vendor.getPassword();
        Account expected = vendor;
        Account actual = namazon.signIn(email, password);
        Assertions.assertEquals(expected, actual);
    }
    
    @Test
    @DisplayName("sign in not successful test")
    public void signInTest03() {
        String email = "notvalid";
        String password = "232343";
        Assertions.assertThrows(AccountAuthenticationException.class, ()->{
            namazon.signIn(email, password);
        });
    }

    @Test
    @DisplayName("verify email is available test is available test")
    public void verifyEmailIsAvailableTest01() {
        namazon.addVendor(vendor);
        String email = "available";
        boolean actual = namazon.verifyEmailIsAvailable(email);
        Assertions.assertTrue(actual);
    }

    @Test
    @DisplayName("verify email is available test is NOT available test")
    public void verifyEmailIsAvailableTest02() {
        namazon.addVendor(vendor);
        String email = "bob@gmail.com";
        boolean actual = namazon.verifyEmailIsAvailable(email);
        Assertions.assertFalse(actual);
    }

    @Test
    @DisplayName("sign up as vendor successful test")
    public void signUpAsVendorTest01() throws AccountCreationException {
        String brandName = "Java";
        String firstName = "Bob";
        String lastName = "Smith";
        String email = "bob@gmail.com";
        String password = "12345";
        namazon.signUpAsVendor(brandName, firstName, lastName, email, password);
        int expected = 1;
        int actual = namazon.getVendors().size();
        Assertions.assertEquals(expected, actual);
    }

    @Test
    @DisplayName("sign up as vendor test not successful test")
    public void signUpAsVendorTest02() {
        String brandName = "Java";
        String firstName = "Bob";
        String lastName = "Smith";
        String email = "bob@gmail.com";
        String password = "12345";
        namazon.addVendor(vendor);
        Assertions.assertThrows(AccountCreationException.class, ()->{
            namazon.signUpAsVendor(brandName, firstName, lastName, email, password);
        });
    }

    @Test
    @DisplayName("sign up as customer successful test")
    public void signUpAsCustomerTest01() throws AccountCreationException, IOException {
        String firstName = "Bob";
        String lastName = "Smith";
        String email = "bob@gmail.com";
        String password = "12345";
        namazon.signUpAsCustomer(firstName, lastName, email, password);
        int expected = 1;
        int actual = namazon.getCustomers().size();
        Assertions.assertEquals(expected, actual);
    }

    @Test
    @DisplayName("sign up as customer test not successful test")
    public void signUpAsCustomerTest02() {
        String firstName = "Bob";
        String lastName = "Smith";
        String email = "bob@gmail.com";
        String password = "12345";
        namazon.addCustomer(customer);
        Assertions.assertThrows(AccountCreationException.class, ()->{
            namazon.signUpAsCustomer(firstName, lastName, email, password);
        });
    }

    @Test
    @DisplayName("select vendor successful test")
    public void selectVendorTest01() throws VendorNotFoundException {
        namazon.addVendor(vendor);
        Vendor actual = namazon.selectVendor(vendor);
        Assertions.assertEquals(vendor, actual);
    }

    @Test
    @DisplayName("select vendor unsuccessful because empty test")
    public void selectVendorTest02() {
        Assertions.assertThrows(VendorNotFoundException.class, ()->{
            namazon.selectVendor(vendor);
        });
    }

    @Test
    @DisplayName("select vendor unsuccessful because no match test")
    public void selectVendorTest03() {
        namazon.addVendor(vendor01);
        Assertions.assertThrows(VendorNotFoundException.class, ()->{
            namazon.selectVendor(vendor);
        });
    }
}
