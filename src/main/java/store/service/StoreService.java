package store.service;

import java.math.BigDecimal;
import java.util.Optional;
import store.domain.Product;
import store.repository.StoreRepository;

public class StoreService {
    private final StoreRepository storeRepository;

    public StoreService(StoreRepository storeRepository) {
        this.storeRepository = storeRepository;
    }

    public boolean hasActivePromotion(String productName) {
        Optional<Product> promotionProduct = storeRepository.findPromotionProductByName(productName);

        return promotionProduct.map(product -> product.getPromotion().get().isActive()).orElse(false);
    }

    public boolean isSufficientQuantityGeneralProduct(String productName, BigDecimal quantity) {
        Optional<Product> generalProduct = storeRepository.findGeneralProductByName(productName);

        return generalProduct.map(product -> product.getQuantity().compareTo(quantity) >= 0).orElse(false);
    }

    public boolean isSufficientQuantityPromotionProduct(String productName, BigDecimal quantity) {
        Optional<Product> generalProduct = storeRepository.findPromotionProductByName(productName);

        return generalProduct.map(product -> product.getQuantity().compareTo(quantity) >= 0).orElse(false);
    }
}
