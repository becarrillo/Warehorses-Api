package com.web.warehorses_secure_persistence.domain.item.service;

import com.web.warehorses_secure_persistence.domain.item.entity.Item;
import com.web.warehorses_secure_persistence.domain.item.repository.ItemRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ItemService {

    private final ItemRepository itemRepository;

    public ItemService(ItemRepository itemRepository) {
        this.itemRepository = itemRepository;
    }

    public Item create(Item item) {
        return itemRepository.save(item);
    }

    public Item findById(int id) {
        return itemRepository.findById(id)
                .orElseThrow(RuntimeException::new);
    }

    public List<Item> findAll() {
        return itemRepository.findAll();
    }

    public List<Item> findBySupplier(String supplier) {
        return itemRepository.findBySupplier(supplier);
    }

    public List<Item> findByDescriptionContainingIgnoreCase(String description) {
        return itemRepository.findByDescriptionContainingIgnoreCase(description);
    }

    public Item update(int id, Item item) {
        Item existing = itemRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Item not found"));
        existing.setDescription(item.getDescription());
        existing.setUnitPrice(item.getUnitPrice());
        existing.setQuantity(item.getQuantity());
        existing.setSalePrice(item.getSalePrice());
        existing.setSupplier(item.getSupplier());
        return itemRepository.save(existing);
    }

    public void delete(int id) {
        itemRepository.deleteById(id);
    }
}