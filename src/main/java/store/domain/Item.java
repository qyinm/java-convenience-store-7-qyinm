package store.domain;

import java.math.BigDecimal;

public record Item(Product product, BigDecimal quantity) {

    public BigDecimal getTotalQuantity() {
        return quantity.multiply(product.getPrice());
    }
}
