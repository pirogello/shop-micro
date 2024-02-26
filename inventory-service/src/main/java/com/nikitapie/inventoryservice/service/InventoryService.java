package com.nikitapie.inventoryservice.service;

import com.nikitapie.inventoryservice.dto.InventoryResponse;
import com.nikitapie.inventoryservice.model.Inventory;
import com.nikitapie.inventoryservice.repository.InventoryRepository;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class InventoryService {

    private final InventoryRepository inventoryRepository;

    @SneakyThrows
    @Transactional(readOnly = true)
    public List<InventoryResponse> isInStock(List<String> skuCode) {
        /** Для проверки resilience4j в Order service
        log.info("start wait");
        Thread.sleep(10_000);
        log.info("end wait");
         **/
        return inventoryRepository.findBySkuCodeIn(skuCode).stream()
                .map(this::mapToDto).toList();
    }


    private InventoryResponse mapToDto(Inventory inventory) {
        return InventoryResponse.builder()
                .skuCode(inventory.getSkuCode())
                .isInStock(inventory.getQuantity() > 0)
                .build();
    }
}
