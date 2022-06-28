package users;

import account.Account;
import address.Address;
import enums.OrderStatus;
import enums.ProductCategory;
import exceptions.ProductNotAvailableException;
import order.Order;
import product.Product;

import java.util.*;

public class Vendor extends Account {

    private String brandName;
    private Map<Product, Integer> inventory;
    private Product[] showCase;
    private List<Order> orders;

    public Vendor(String brandName, String firstName, String lastName, String email, String password) {
        super(firstName, lastName, email, password);
        this.brandName = brandName;
        inventory = new HashMap<>();
        showCase = new Product[5];
        orders = new ArrayList<>();
    }

    public void addProduct(Product product) {
        int count = inventory.getOrDefault(product, 0);
        inventory.put(product, ++count);
    }

    public int getNumberInStock(Product product) {
        return inventory.get(product);
    }

    public Boolean removeProduct(Product product) {
        if (inventory.isEmpty()) {
            return false;
        }
        boolean contains = inventory.containsKey(product);
        if (!contains) {
            return false;
        }
        inventory.remove(product);
        return true;
    }

    public void addOrder(Order order) {
        orders.add(order);
    }

    public Boolean cancelOrder(Order order) {
        if (orders.isEmpty()) {
            return false;
        }
        boolean contains = orders.contains(order);
        if (!contains) {
            return false;
        }
        orders.remove(order);
        return true;
    }

    public void addProductToShowcase(Product product, int index) {
        showCase[index] = product;
    }

    public ArrayList<Product> searchByCategory(ProductCategory category) {
        ArrayList<Product> matches = new ArrayList<>();
        for (Product product : inventory.keySet()) {
            String categoryName = product.getCategory().getName();
            int numberInStock = getNumberInStock(product);
            if (categoryName.equals(category.getName()) && numberInStock != 0) {
                matches.add(product);
            }
        }
        return matches;
    }

    public ArrayList<Product> searchByName(String name) {
        ArrayList<Product> matches = new ArrayList<>();
        for (Product product : inventory.keySet()) {
            String productName = product.getName();
            int numberInStock = getNumberInStock(product);
            if (productName.equalsIgnoreCase(name) && numberInStock != 0) {
                matches.add(product);
            }
        }
        return matches;
    }

    public boolean purchase(Product product, Address address) throws ProductNotAvailableException {
        boolean contains = inventory.containsKey(product);
        boolean inStock = inventory.get(product) != null && inventory.get(product) >= 1;
        if (contains && inStock) {
            int currentStock = getNumberInStock(product);
            inventory.put(product, --currentStock);
            Order order = new Order(product, address, OrderStatus.PENDING);
            addOrder(order);
            return true;
        }
        throw new ProductNotAvailableException();
    }

    public String displayAllOrders() {
        StringBuilder display = new StringBuilder();
        for (Order order : orders) {
            display.append(order.toString()).append("\n");
        }
        return display.toString();
    }

    public String getBrandName() {
        return brandName;
    }

    public void setBrandName(String brandName) {
        this.brandName = brandName;
    }

    public Map<Product, Integer> getInventory() {
        return inventory;
    }

    public Product[] getShowCase() {
        return showCase;
    }

    public List<Order> getOrders() {
        return orders;
    }

    @Override
    public String toString() {
        return String.format("%s %s %s %s %s %d", brandName, getFirstName(), getLastName(), getEmail(), getPassword(), getId());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Vendor vendor = (Vendor) o;
        return Objects.equals(brandName, vendor.brandName) && Objects.equals(inventory, vendor.inventory) && Arrays.equals(showCase, vendor.showCase) && Objects.equals(orders, vendor.orders);
    }
}
