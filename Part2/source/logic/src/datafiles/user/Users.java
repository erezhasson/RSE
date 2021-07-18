package datafiles.user;

import exceptions.name.EmptyNameException;
import exceptions.name.NameExistsException;
import exceptions.stock.StockException;
import exceptions.symbol.SymbolException;
import exceptions.user.UserNotFoundException;

import java.util.Collection;
import java.util.HashMap;

public class Users {
    private final HashMap<String, User> Symbol2User;

    public Users(){
        Symbol2User = new HashMap<>();
    }

    public int getStockAmount() {
        return Symbol2User.size();
    }

    public boolean containsUser(String symbol) {
        return Symbol2User.containsKey(symbol);
    }

    public Collection<User> getAllUsers() {
        return Symbol2User.values();
    }

    public User getUser(String name) throws UserNotFoundException {
        User user = Symbol2User.get(name);

        if (user == null) throw new UserNotFoundException(name);
        return user;
    }

    public void addUser(String name, User newUser) throws NameExistsException, EmptyNameException {
        if (containsUser(name)) throw new NameExistsException(name);
        if (name == null || name.equals("")) throw new EmptyNameException(name);
        Symbol2User.put(name, newUser);
    }

    public void clear() {
        Symbol2User.clear();
    }
}
