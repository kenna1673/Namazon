package users;

import account.Account;

public class Customer extends Account {

    public Customer(String firstName, String lastName, String email, String password) {
        super(firstName, lastName, email, password);
    }

    @Override
    public String toString() {
        return String.format("%s %s %s %s %d", getFirstName(), getLastName(), getEmail(), getPassword(), getId());
    }

    @Override
    public boolean equals(Object obj) {
        return super.equals(obj);
    }
}
