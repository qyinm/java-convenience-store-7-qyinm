package store.service;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.function.Function;
import store.domain.Product;
import store.domain.Promotion;
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

        return generalProduct.map(isGreaterQuantityThan(quantity)).orElse(false);
    }

    private Function<Product, Boolean> isGreaterQuantityThan(BigDecimal quantity) {
        return product -> product.getQuantity().compareTo(quantity) >= 0;
    }

    public boolean isSufficientQuantityPromotionProduct(String productName, BigDecimal quantity) {
        Optional<Product> generalProduct = storeRepository.findPromotionProductByName(productName);

        return generalProduct.map(isGreaterQuantityThan(quantity)).orElse(false);
    }

    public boolean isSufficientQuantityToGetBonusProduct(String productName, BigDecimal quantity) {
        Optional<Product> promotionProduct = storeRepository.findPromotionProductByName(productName);
        Promotion promotion = promotionProduct.get().getPromotion().get();

        Boolean isSufficientQuantity = promotionProduct.map(isGreaterQuantityThan(quantity)).orElse(false);
        return promotion.isAvailableToGetBonus(quantity) && isSufficientQuantity;
    }
}
