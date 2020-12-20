package com.example.cruddemo.repository;

import com.example.cruddemo.model.User;
import java.util.ArrayList;
import java.util.Iterator;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends CrudRepository<User, Long> {

    List<User> findByName(String name);
    
    public default boolean checkOldUser(User anUser) {
        Iterable<User> iterUser = this.findAll();
        for (User user : iterUser) {
            if (user.equals(anUser)) {
                return true;
            }
        }

        return false;
    }
}
