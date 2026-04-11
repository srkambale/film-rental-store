package com.example.demo.rental.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.rental.entity.Inventory;

import java.util.List;

@Repository
public interface InventoryRepository extends JpaRepository<Inventory, Integer> {

    List<Inventory> findByFilmId(Integer filmId);

    List<Inventory> findByStoreId(Integer storeId);

    List<Inventory> findByFilmIdAndStoreId(Integer filmId, Integer storeId);
}
