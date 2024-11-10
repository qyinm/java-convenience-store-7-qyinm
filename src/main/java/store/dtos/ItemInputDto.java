package store.dtos;

import java.math.BigDecimal;

public record ItemInputDto(String itemName, BigDecimal price) {
}
