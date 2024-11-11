package store.dtos;

import java.math.BigDecimal;
import java.util.List;
import store.domain.Cart;
import store.domain.Item;

public record ReceiptDto(
        Cart promotionBonusItems,
        BigDecimal totalPay,
        BigDecimal promotionDiscountPay,
        BigDecimal membershipDiscountPay,
        BigDecimal willPayAmounts
) {
}