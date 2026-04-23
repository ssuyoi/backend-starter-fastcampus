package com.backendstarter.onlinecoffeesandbox.repository;

import com.backendstarter.onlinecoffeesandbox.domain.StoreProduct;
import java.util.Optional;
import org.springframework.data.repository.CrudRepository;

public interface StoreProductRepository extends CrudRepository<StoreProduct, Integer> {
    Optional<StoreProduct> findByStoreIdAndProductId(int storeId, int productId);

}
