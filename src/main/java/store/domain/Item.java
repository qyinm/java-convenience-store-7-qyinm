package store.domain;

import java.math.BigDecimal;

public record Item(Product product, BigDecimal quantity) {

    public BigDecimal getTotalItemAmount() {
        return quantity.multiply(product.getPrice());
    }
}
