package store.repository;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.Set;
import store.domain.Product;

public class StoreRepository {

    private final Set<Product> products;

    public StoreRepository(Set<Product> products) {
        this.products = products;
    }

    public Optional<Product> findGeneralProductByName(String productName) {
        return products.stream()
                .filter(product -> product.getPromotion().isEmpty())
                .filter(product -> product.getName().equals(productName))
                .findFirst();
    }

    public Optional<Product> findPromotionProductByName(String productName) {
        return products.stream()
                .filter(product -> product.getPromotion().isPresent())
                .filter(product -> product.getName().equals(productName))
                .findFirst();
    }

    public Product updateGeneralProduct(String productName, BigDecimal updatedQuantity) {
        Product findGenralProduct = products.stream()
                .filter(product -> product.getPromotion().isEmpty())
                .filter(product -> product.getName().equals(productName))
                .findFirst()
                .get();

        findGenralProduct.updateQuantity(updatedQuantity);
        return findGenralProduct;
    }

    public Product updatePromotionProduct(String productName, BigDecimal updatedQuantity) {
        Product findPromotionProduct = products.stream()
                .filter(product -> product.getPromotion().isPresent())
                .filter(product -> product.getName().equals(productName))
                .findFirst()
                .get();

        findPromotionProduct.updateQuantity(updatedQuantity);
        return findPromotionProduct;
    }
}
