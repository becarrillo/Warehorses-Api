package com.web.warehorses_api_main.controller;

import com.web.warehorses_secure_persistence.domain.item.entity.Item;
import com.web.warehorses_secure_persistence.domain.item.service.ItemService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/v1/items")
public class ItemV1Controller {
    private final ItemService itemService;

    public ItemV1Controller(ItemService itemService) {
        this.itemService = itemService;
    }

    @GetMapping
    public ResponseEntity<List<Item>> listAllItems() {
        return ResponseEntity.ok(itemService.findAll());
    }

    @GetMapping(path = "/item", params = {"description"})
    public ResponseEntity<List<Item>> getByDescriptionContainingIgnoreCase(
            @RequestParam("description") String description) {
        List<Item> items;
        try {
            items = itemService.findByDescriptionContainingIgnoreCase(description);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return ResponseEntity.ok(items);
    }

    @GetMapping("/{itemId}")
    public ResponseEntity<Item> getItemById(@PathVariable("itemId") int id) {
        try {
            final Item item = itemService.findById(id);
            return ResponseEntity.ok(item);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


}
