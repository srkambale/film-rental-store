package com.example.demo.customer.repository;

import com.example.demo.customer.model.CustomerInventory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface CustomerInventoryRepository extends JpaRepository<CustomerInventory, Long> {
    List<CustomerInventory> findByFilmId(Long filmId);
}
