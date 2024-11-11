package store.dtos;

import java.math.BigDecimal;
import java.util.List;
import store.domain.Item;

public record ReceiptDto(
        List<Item> promotionBonusItems,
        BigDecimal totalPay,
        BigDecimal promotionDiscountPay,
        BigDecimal membershipDiscountPay,
        BigDecimal willPayAmounts
) {
}