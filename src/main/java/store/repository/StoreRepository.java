package store.repository;

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
}
