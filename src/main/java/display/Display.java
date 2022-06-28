package display;

import account.Account;
import address.Address;
import enums.ProductCategory;
import exceptions.AccountAuthenticationException;
import exceptions.AccountCreationException;
import exceptions.ProductNotAvailableException;
import exceptions.VendorNotFoundException;
import namazon.Namazon;
import order.Order;
import product.Product;
import users.Customer;
import users.Vendor;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Display {
    private static final String LINE = "----------------------------------------";
    private final String INVALID_CHOICE = "You did not enter a valid choice. Please choose again.";
    private final String ENTER_FIRSTNAME = "Please enter your first name";
    private final String ENTER_LASTNAME = "Please enter your last name";
    private final String ENTER_EMAIL = "Please enter your email";
    private final String ENTER_PASSWORD = "PLease enter your password";

    private final String NO_VENDORS_MESSAGE = "Sorry there are currently no vendors!";
    private final String NO_PRODUCTS_MESSAGE = "There are currently no vendors with available products!";
    private final String GOODBYE_MESSAGE = "Have a good day!";
    private final String WELCOME_MESSAGE = "Welcome to Namazon!";
    public final int NO_VENDORS = -1;
    public final int LOGOUT = -2;
    private Scanner scanner;
    private Namazon namazon;
    private Account currentAccount;
    private Vendor currentVendor;
    private Customer currentCustomer;

    public Display() {
        scanner = new Scanner(System.in);
        namazon = new Namazon();
    }

    public void start() throws VendorNotFoundException, ProductNotAvailableException, IOException {
        List<Vendor> vendors = namazon.getVendors();
        int choice;
        Vendor customersChosenVendor;
        Product toPurchase;
        boolean flag = true;
        while (flag) {
            boolean accountFlag = true;
            flag = startDisplay();
            while (accountFlag && flag) {
                boolean isVendor = currentAccount.getClass() == Vendor.class;
                boolean isCustomer = currentAccount.getClass() == Customer.class;
                if (isVendor) {
                    currentVendor = (Vendor) currentAccount;
                    accountFlag = vendorChoicesDisplay();
                } else if (isCustomer) {
                    currentCustomer = (Customer) currentAccount;
                    if (vendors.isEmpty()) {
                        print(NO_VENDORS_MESSAGE);
                        start(); // restart
                        break;
                    }
                    choice = customersChoiceOfVendorDisplay();
                    if (choice == NO_VENDORS) {
                        print(NO_PRODUCTS_MESSAGE);
                        break;
                    } else if (choice == LOGOUT) {
                        start(); // restart
                        break;
                    }
                    customersChosenVendor = vendors.get(choice);
                    toPurchase = customerOptionsDisplay(customersChosenVendor);
                    if (toPurchase == null) {
                        print(GOODBYE_MESSAGE);
                    } else {
                        Address address = promptForAddressAfterSaleDisplay();
                        try {
                            customersChosenVendor.purchase(toPurchase, address);
                        } catch (ProductNotAvailableException ex) {
                            print("There are none of these items currently in stock!");
                        }
                    }
                }
            }
        }
    }

    public static void main(String[] args) throws VendorNotFoundException, ProductNotAvailableException, IOException {
        Display display = new Display();
        display.start();
    }

    public static void print(String msg) {
        System.out.println(msg);
    }

    public boolean startDisplay() {
        boolean flag = true;
        print(WELCOME_MESSAGE);
        print(LINE);
        print("""
             Press 0 to exit
             Press 1 to sign in
             Press 2 to sign up as a vendor
             Press 3 to sign up as a customer""");
        print(LINE);
        int choice = scanner.nextInt();
        scanner.nextLine();
        switch (choice) {
            case 1:
                flag = signInDisplay();
                break;
            case 2:
                createAccountAsVendorDisplay();
                break;
            case 3:
                createAccountAsCustomer();
                break;
            case 0:
            default:
                print(GOODBYE_MESSAGE);
                flag = false;
        }
        return flag;
    }
    public Vendor createAccountAsVendorDisplay() {
        String brandName;
        String firstName;
        String lastName;
        String email;
        String password;
        Vendor vendor = null;
        print(LINE);
        print("You chose to create a new account as a vendor!");
        print(LINE);
        boolean continueFlag = true;
        while (continueFlag) {
            print("Please enter your brand name");
            brandName = scanner.nextLine();
            print(ENTER_FIRSTNAME);
            firstName = scanner.nextLine();
            print(ENTER_LASTNAME);
            lastName = scanner.nextLine();
            print(ENTER_EMAIL);
            email = scanner.nextLine();
            print(ENTER_PASSWORD);
            password = scanner.nextLine();
            try {
                vendor = namazon.signUpAsVendor(brandName, firstName, lastName, email, password);
                currentVendor = vendor;
                currentAccount = vendor;
                continueFlag = false;
            } catch (AccountCreationException ex) {
                print("You did not enter an available email. Please try again");
                print(LINE);
            }
        }
        return vendor;
    }

    public Customer createAccountAsCustomer() {
        String firstName;
        String lastName;
        String email;
        String password;
        Customer customer = null;
        print(LINE);
        print("You chose to create a new account as a customer!");
        print(LINE);
        boolean continueFlag = true;
        while (continueFlag) {
            print(ENTER_FIRSTNAME);
            firstName = scanner.nextLine();
            print(ENTER_LASTNAME);
            lastName = scanner.nextLine();
            print(ENTER_EMAIL);
            email = scanner.nextLine();
            print(ENTER_PASSWORD);
            password = scanner.nextLine();
            try {
                customer = namazon.signUpAsCustomer(firstName, lastName, email, password);
                currentCustomer = customer;
                currentAccount = customer;
                continueFlag = false;
            } catch (AccountCreationException | IOException ex) {
                print("You did not enter an available email. Please try again");
            }
        }
        return customer;
    }

    public boolean signInDisplay() {
        boolean success = true;
        int count = 0;
        String email;
        String password;
        Account account;
        print(LINE);
        print("You chose to sign in!");
        print(LINE);
        boolean continueFlag = true;
        while (continueFlag && count < 3) {
            print(ENTER_EMAIL);
            email = scanner.nextLine();
            print(ENTER_PASSWORD);
            password = scanner.nextLine();
            try {
                account = namazon.signIn(email, password);
                currentAccount = account;
                continueFlag = false;
                success = true;
            } catch (AccountAuthenticationException ex) {
                print("You did not enter an existing account. Please try again.");
                count++;
                print(LINE);
            }
        }
        if (count == 3) {
            print("Sorry! You exhausted your sign in attempts!");
            print(LINE);
            success = false;
        }
        return success;
    }

    public Product customerOptionsDisplay(Vendor vendor) throws ProductNotAvailableException {
        Product newProduct = null;
        int choice = 0;
        print(LINE);
        print("""
            Press 0 to exit
            Press 1 to search by category
            Press 2 to search by name""");
        print(LINE);
        choice = scanner.nextInt();
        scanner.nextLine();
        switch (choice) {
            case 1:
                newProduct = searchByCategoryDisplay(vendor);
                break;
            case 2:
                newProduct = searchByNameDisplay(vendor);
                break;
            case 0:
            default:
                print("Have a good day!");
        }
        return newProduct;
    }

    public int customersChoiceOfVendorDisplay() {
        boolean continueFlag = true;
        int choice = 0;
        int numberOfAvailableVendors = 0;
        while (continueFlag) {
            print(LINE);
            List<Vendor> vendors = namazon.getVendors();
            for (int i = 0; i < vendors.size(); i++) {
                Vendor current = vendors.get(i);
                String name = current.getBrandName();
                if (!current.getInventory().isEmpty()) {
                    print(String.format("Press %d to shop %s's products\n", i, name));
                    numberOfAvailableVendors++;
                }
            }
            print(String.format("Press %d to logout", vendors.size()));
            if (numberOfAvailableVendors == 0) {
                return NO_VENDORS;
            }
            print(LINE);
            choice = scanner.nextInt();
            scanner.nextLine();
            if (choice == vendors.size()) {
                return LOGOUT;
            }
            if (choice < 0 || choice >= vendors.size() || vendors.get(choice).getInventory().isEmpty()) {
                print(INVALID_CHOICE);
            } else {
                continueFlag = false;
            }
        }
        return choice;
    }

    public boolean vendorChoicesDisplay() throws VendorNotFoundException, IOException, ProductNotAvailableException {
        int choice = 0;
        boolean continueFlag = true;
        print(LINE);
        print("""
            Press 0 to exit
            Press 1 to add a product
            Press 2 to display all current orders""");
        print(LINE);
        choice = scanner.nextInt();
        scanner.nextLine();
        switch (choice) {
            case 1:
                addProductDisplay();
                break;
            case 2:
                displayAllOrdersDisplay(currentVendor);
                break;
            case 0:
            default:
                print(GOODBYE_MESSAGE);
                continueFlag = false;
        }
        return continueFlag;
    }

    public ProductCategory selectCategoryDisplay() {
        boolean continueFlag = true;
        int choice = 0;
        ProductCategory category = null;
        while (continueFlag) {
            print(LINE);
            print("""
                    Press 1 for ELECTRONICS
                    Press 2 for ATHLETICS
                    Press 3 for CLOTHING
                    Press 4 for HOME APPLIANCE""");
            print(LINE);
            choice = scanner.nextInt();
            if (choice < 1 || choice > 4) {
                print(INVALID_CHOICE);
            } else {
                continueFlag = false;
            }
        }
        switch (choice) {
            case 1 -> category = ProductCategory.ELECTRONICS;
            case 2 -> category = ProductCategory.ATHLETICS;
            case 3 -> category = ProductCategory.CLOTHING;
            case 4 -> category = ProductCategory.HOME_APPLIANCE;
        }
        return category;
    }

    public void addProductDisplay() {
        String name;
        ProductCategory category;
        double price;
        Product newProduct;
        print(LINE);
        print("Please enter a name for your product");
        name = scanner.nextLine();
        category = selectCategoryDisplay();
        print("Please enter a price for your product");
        price = scanner.nextDouble();
        newProduct = new Product(name, category, price);
        currentVendor.addProduct(newProduct);
    }

    public void displayAllOrdersDisplay(Vendor vendor) {
        List<Order> orders = vendor.getOrders();
        if (orders.isEmpty()) {
            print("You currently have no orders!");
        } else {
            for (Order order : orders) {
                print(order.toString());
            }
        }
    }

    public Product searchByCategoryDisplay(Vendor vendor) {
        ProductCategory category;
        Product toPurchase = null;
        category = selectCategoryDisplay();
        ArrayList<Product> matches = vendor.searchByCategory(category);
        boolean continueFlag = true;
        int choice = -1;
        int quitNumber = vendor.getInventory().size();
        while (continueFlag) {
            if (!matches.isEmpty()) {
                print("The following product(s) match your desired category:");
                for (int i = 0; i < matches.size(); i++) {
                    Product currentProduct = matches.get(i);
                    print(String.format("Press %d to purchase %s", i, currentProduct));
                }
                choice = scanner.nextInt();
                scanner.nextLine();
                if (choice == quitNumber) {
                    return null;
                } else if (choice < 0 || choice >= matches.size()) {
                    print(String.format("That is an invalid choice!\nPlease press %s if you would like the exit!", quitNumber));
                } else {
                    toPurchase = matches.get(choice);
                    continueFlag = false;
                }
            } else {
                print("Sorry! There are no products from " + vendor.getBrandName() + " that match that category!");
                continueFlag = false;
            }

        }
        return toPurchase;
    }

    public Product searchByNameDisplay(Vendor vendor) {
        print(LINE);
        print("Please type the name of the product you would like to search");
        String name = scanner.nextLine();
        ArrayList<Product> matches = vendor.searchByName(name);
        Product toPurchase = null;
        boolean continueFlag = true;
        int choice = -1;
        int quitNumber = vendor.getInventory().size();
        while (continueFlag) {
            if (!matches.isEmpty()) {
                print("The following product(s) match your desired product:");
                for (int i = 0; i < matches.size(); i++) {
                    Product currentProduct = matches.get(i);
                    print(String.format("Press %d to purchase %s", i, currentProduct));
                }
                choice = scanner.nextInt();
                scanner.nextLine();
                if (choice == quitNumber) {
                    return null;
                } else if (choice < 0 || choice >= matches.size()) {
                    print(String.format("That is an invalid choice!\nPlease press %s if you would like the exit!", quitNumber));
                } else {
                    toPurchase = matches.get(choice);
                    continueFlag = false;
                }
            } else {
                print("Sorry! There are no products from " + vendor.getBrandName() + " that match that category!");
                continueFlag = false;
            }

        }
        return toPurchase;
    }

    public Address promptForAddressAfterSaleDisplay() {
        Address address;
        String street;
        String unit;
        String city;
        String state;
        print(LINE);
        print("Thank you for wanting to make a purchase\nFirst we will need to get you're address");
        print(LINE);
        print("Please enter your street");
        street = scanner.nextLine();
        print("Please enter your unit");
        unit = scanner.nextLine();
        print("Please enter your city");
        city = scanner.nextLine();
        print("Please enter you state");
        state = scanner.nextLine();
        address = new Address(street, unit, city, state);
        return address;
    }
}
