package enums;

public enum OrderStatus {
    PENDING("Pending"), SHIPPED("Shipped"), DELIVERED("Delivered");
    private String name;
    OrderStatus(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
