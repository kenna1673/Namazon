package enums;

public enum ProductCategory {
    ELECTRONICS("electronics"), ATHLETICS("Athletics"), CLOTHING("Clothing"), HOME_APPLIANCE("Home Appliance");
    private String name;
    ProductCategory(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
