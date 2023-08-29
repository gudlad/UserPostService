package com.gudlad.rest.webservices.restfulwebservices.user;


import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

@Component
public class UserDaoService {

    // jpa/hibernate > database
    // static list of users
    private static List<User> users = new ArrayList<>();
    private static int userCount = 0;

    static {
        users.add(new User(++userCount, "Adam", LocalDate.now().minusYears(30)));
        users.add(new User(++userCount, "Eve", LocalDate.now().minusYears(25)));
        users.add(new User(++userCount, "Jim", LocalDate.now().minusYears(20)));
    }

    public static User save(User user) {
        user.setId(++userCount);
        users.add(user);
        return user;
    }

    public List<User> getUsers() {
        return users;
    }

    public User getUser(int id) {
        Predicate<? super User> userPredicate = user -> user.getId().equals(id);
        return users.stream().filter(userPredicate).findFirst().orElse(null);
    }

    public void deleteUser(int id) {
        Predicate<? super User> predicate = user -> user.getId().equals(id);
        users.removeIf(predicate);
    }
}