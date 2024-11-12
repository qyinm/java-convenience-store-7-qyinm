package store.domain;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;

public class Cart {
    private final List<Item> items;

    public Cart(List<Item> items) {
        this.items = items;
    }

    public Cart getPromotionBonusItems() {
        List<Item> bonusItems = items.stream().filter(item -> item.product().getPromotion().isPresent())
                .map(getBonusPromotionItem()).toList();
        return new Cart(bonusItems);
    }

    private Function<Item, Item> getBonusPromotionItem() {
        return item -> {
            Promotion promotion = item.product().getPromotion().get();
            BigDecimal bonusQuantity = item.quantity()
                    .divide(promotion.getBuyAmount().add(promotion.getGetAmount()), 0, RoundingMode.FLOOR);
            return new Item(item.product(), bonusQuantity);
        };
    }

    public BigDecimal getTotalItemsAmount() {
        return items.stream().map(Item::getTotalItemAmount).reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public BigDecimal calculateMembershipDiscountAmount(Cart promotionBonusItems) {
        return items.stream().filter(getItemUnIncludedBonusItemsPredicate(promotionBonusItems))
                .map(item -> item.getTotalItemAmount().multiply(BigDecimal.valueOf(0.3)))
                .reduce(BigDecimal.ZERO, BigDecimal::add).setScale(0, RoundingMode.FLOOR).min(BigDecimal.valueOf(8000));
    }

    private Predicate<Item> getItemUnIncludedBonusItemsPredicate(Cart promotionBonusItems) {
        return item -> promotionBonusItems.items.stream()
                .noneMatch(bonusItem -> bonusItem.product().getName().equals(item.product().getName()));
    }

    public String getFormattedAllItemsWithNameAndPriceAndQuantity(String format) {
        StringBuilder result = new StringBuilder();
        items.stream().map(item -> format.formatted(item.product().getName(), item.product().getPrice(),
                item.quantity().intValue())).forEach(result::append);
        return result.toString();
    }

    public int getSize() {
        return items.size();
    }

    public BigDecimal getTotalItemsQuantity() {
        return items.stream().map(Item::quantity).reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}
