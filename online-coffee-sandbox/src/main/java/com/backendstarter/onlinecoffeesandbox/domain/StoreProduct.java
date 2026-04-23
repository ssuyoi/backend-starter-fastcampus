package com.backendstarter.onlinecoffeesandbox.domain;

import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Getter
@Table(name = "store_products")
public class StoreProduct {

    @Id
    private int storeProductId;

    @Column
    private String storeId;

    @Column
    private String productId;

    @Column
    private int stockQuantity;

    /**
     * 상품 재고 조정 - 주문 수량이 재고보다 많으면 예외 발생
     */
    public void adjustStockQuantity(int buyQuantity) {
        if (stockQuantity < buyQuantity) {
            throw new RuntimeException("재고보다 많을 수 없습니다.");
        }
        this.stockQuantity -= buyQuantity;
    }
}
