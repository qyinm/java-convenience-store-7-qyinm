package store.domain;

import static store.exception.store.StoreErrorCode.EXCEED_PRODUCT_QUANTITY;

import java.math.BigDecimal;
import java.util.Optional;
import store.exception.StoreException;

public class Product {
    private final String name;
    private final BigDecimal price;
    private BigDecimal quantity;
    private final Promotion promotion;

    public Product(String name, BigDecimal price, BigDecimal quantity, Promotion promotion) {
        this.name = name;
        this.price = price;
        this.quantity = quantity;
        this.promotion = promotion;
    }

    public String getName() {
        return name;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public BigDecimal getQuantity() {
        return quantity;
    }

    public Optional<Promotion> getPromotion() {
        return Optional.ofNullable(promotion);
    }

    public void updateQuantity(BigDecimal newQuantity) {
        validateQuantity(newQuantity);

        this.quantity = newQuantity;
    }

    private void validateQuantity(BigDecimal newQuantity) {
        if (newQuantity.compareTo(BigDecimal.ZERO) < 0) {
            throw new StoreException(EXCEED_PRODUCT_QUANTITY);
        }
    }
}
