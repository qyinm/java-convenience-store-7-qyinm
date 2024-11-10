package store.domain;

import java.math.BigDecimal;

public record Item(Product product, BigDecimal quantity) {
}
