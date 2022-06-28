package namazon;

import account.Account;
import exceptions.AccountAuthenticationException;
import exceptions.AccountCreationException;
import exceptions.VendorNotFoundException;
import users.Customer;
import users.Vendor;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class Namazon {
    private List<Customer> customers;
    private List<Vendor> vendors;

    public Namazon() {
        customers = new ArrayList<>();
        vendors = new ArrayList<>();
    }
    public Account signIn(String email, String password) throws AccountAuthenticationException {
        Account account = null;
        for (Customer customer : customers) {
            String currentEmail = customer.getEmail();
            String currentPassword = customer.getPassword();
            boolean emailMatch = currentEmail.equals(email);
            boolean passwordMatch = currentPassword.equals(password);
            if (emailMatch && passwordMatch) {
                account = customer;
                return account;
            }
        }
        for (Vendor vendor : vendors) {
            String currentEmail = vendor.getEmail();
            String currentPassword = vendor.getPassword();
            boolean emailMatch = currentEmail.equals(email);
            boolean passwordMatch = currentPassword.equals(password);
            if (emailMatch && passwordMatch) {
                account = vendor;
                return account;
            }
        }
        throw new AccountAuthenticationException();
    }

    public Vendor signUpAsVendor(String brandName, String firstName, String lastName, String email, String password) throws AccountCreationException {
        boolean emailIsAvailable = verifyEmailIsAvailable(email);
        if (emailIsAvailable) {
            Vendor vendor = new Vendor(brandName, firstName, lastName, email, password);
            addVendor(vendor);
            return vendor;
        }
        throw new AccountCreationException();
    }

    public Customer signUpAsCustomer(String firstName, String lastName, String email, String password) throws AccountCreationException, IOException {
        boolean emailIsAvailable = verifyEmailIsAvailable(email);
        if (emailIsAvailable) {
            Customer customer = new Customer(firstName, lastName, email, password);
            addCustomer(customer);
            return customer;
        }
        throw new AccountCreationException();
    }

    public Vendor selectVendor(Vendor vendor) throws VendorNotFoundException {
        for (Vendor currentVendor : vendors) {
            boolean vendorsMatch = currentVendor.equals(vendor);
            if (vendorsMatch) {
                return currentVendor;
            }
        }
        throw new VendorNotFoundException();
    }

    public boolean verifyEmailIsAvailable(String email) {
        for (Customer customer : customers) {
            String currentEmail = customer.getEmail();
            boolean emailMatches = currentEmail.equals(email);
            if (emailMatches) {
                return false;
            }
        }
        for (Vendor vendor : vendors) {
            String currentEmail = vendor.getEmail();
            boolean emailMatches = currentEmail.equals(email);
            if (emailMatches) {
                return false;
            }
        }
        return true;
    }

    public void addCustomer(Customer customer) {
        customers.add(customer);
    }

    public void addVendor(Vendor vendor) {
        vendors.add(vendor);
    }

    public List<Customer> getCustomers() {
        return customers;
    }

    public List<Vendor> getVendors() {
        return vendors;
    }

    public void setCustomers(List<Customer> customers) {
        this.customers = customers;
    }

    public void setVendors(List<Vendor> vendors) {
        this.vendors = vendors;
    }
}
