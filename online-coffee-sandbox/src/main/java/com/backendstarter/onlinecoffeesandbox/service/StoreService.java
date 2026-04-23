package com.backendstarter.onlinecoffeesandbox.service;

import com.backendstarter.onlinecoffeesandbox.domain.StoreProduct;
import com.backendstarter.onlinecoffeesandbox.repository.StoreProductRepository;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;

@Service
public class StoreService {

    private StoreProductRepository storeProductRepository;

    public StoreService(StoreProductRepository storeProductRepository) {
        this.storeProductRepository = storeProductRepository;
    }

    public StoreProduct getStoreProduct(int storeId, int productId) {
        Optional<StoreProduct> storeProductOptional = storeProductRepository.findByStoreIdAndProductId(
            storeId, productId);

        if (storeProductOptional.isEmpty()) {
            throw new RuntimeException("존재하지 않습니다");
        }

        return storeProductOptional.get();
    }

    public void saveAll(List<StoreProduct> storeProducts) {
        storeProductRepository.saveAll(storeProducts);
    }
}
