package store.domain;

import java.math.BigDecimal;
import java.util.Optional;

public class Product {
    private final String name;
    private final BigDecimal price;
    private final BigDecimal quantity;
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
}
