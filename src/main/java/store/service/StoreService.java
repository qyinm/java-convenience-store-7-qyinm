package store.service;

import static store.exception.store.StoreErrorCode.EXCEED_PRODUCT_QUANTITY;
import static store.exception.store.StoreErrorCode.NOT_FOUND_PRODUCT;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.function.Function;
import store.domain.Product;
import store.domain.Promotion;
import store.exception.StoreException;
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

    public Product getGeneralProduct(String productName) {
        validateHasProductWithName(productName);

        Product product = storeRepository.findGeneralProductByName(productName).get();
        return product;
    }

    public Product purchaseGeneralProduct(String productName, BigDecimal quantity) {
        Product generalProduct = getGeneralProduct(productName);

        storeRepository.updateGeneralProduct(productName, generalProduct.getQuantity().subtract(quantity));
        return generalProduct;
    }

    private void validateHasProductWithName(String productName) {
        storeRepository.findPromotionProductByName(productName)
                .or(() -> storeRepository.findGeneralProductByName(productName))
                .orElseThrow(() -> new StoreException(NOT_FOUND_PRODUCT));
    }

    public Product getPromotionProduct(String productName) {
        validateHasProductWithName(productName);

        Product product = storeRepository.findPromotionProductByName(productName).get();
        return product;
    }

    public Product purchasePromotionProduct(String productName, BigDecimal quantity) {
        Product promotionProduct = getPromotionProduct(productName);

        storeRepository.updatePromotionProduct(productName, promotionProduct.getQuantity().subtract(quantity));
        return promotionProduct;
    }

    public void validatePurchasableProduct(String productName, BigDecimal quantity) {
        BigDecimal promotionQuantity = storeRepository.findPromotionProductByName(productName).map(Product::getQuantity)
                .orElse(BigDecimal.ZERO);
        BigDecimal generalQuantity = storeRepository.findGeneralProductByName(productName).map(Product::getQuantity)
                .orElse(BigDecimal.ZERO);

        BigDecimal totalQuantity = promotionQuantity.add(generalQuantity);
        if (quantity.compareTo(totalQuantity) > 0) {
            throw new StoreException(EXCEED_PRODUCT_QUANTITY);
        }
    }
}