package com.example.ElectronicStore.repository;



import com.example.ElectronicStore.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    // Custom query method to find users by their email addresses
    Optional<Product> findByName(String name);

    // Custom query method using JPQL
    // @Query("SELECT u FROM User u WHERE u.age >= :minAge")
    // List<User> findByMinAge(int minAge);

}
