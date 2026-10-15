package com.web.warehorses_secure_persistence.domain.item.repository;

import com.web.warehorses_secure_persistence.domain.item.entity.Item;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ItemRepository extends JpaRepository<Item, Integer> {
    List<Item> findByDescriptionContainingIgnoreCase(String description);
    List<Item> findBySupplier(String supplier);
}
