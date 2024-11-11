package store.controller;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import store.domain.Item;
import store.domain.Product;
import store.domain.Promotion;
import store.dtos.ItemInputDto;
import store.dtos.ReceiptDto;
import store.service.StoreService;
import store.view.InputView;
import store.view.OutputView;

public class StoreController {

    private final StoreService storeService;

    public StoreController(StoreService storeService) {
        this.storeService = storeService;
    }

    public void runStore() {
        Set<Product> allProducts = storeService.getAllProducts();
        OutputView.printCurrentStoreInventory(allProducts);

        List<ItemInputDto> userItemInput = InputView.getUserItemInput();
        List<Item> cart = createCart(userItemInput);
        ReceiptDto receiptDto = calculateUserPurchase(cart);
        OutputView.printReceipt(cart, receiptDto);
    }

    private ReceiptDto calculateUserPurchase(List<Item> cart) {
        BigDecimal totalPay = cart.stream().map(Item::getTotalQuantity).reduce(BigDecimal.ZERO, BigDecimal::add);
        List<Item> promotionBonusItems = cart.stream()
                .filter(item -> item.product().getPromotion().isPresent()) // promotion이 있는 아이템만 필터링
                .map(item -> {
                    Promotion promotion = item.product().getPromotion().get();
                    BigDecimal bonusQuantity = item.quantity()
                            .divide(promotion.getBuyAmount().add(promotion.getGetAmount()));
                    return new Item(item.product(), bonusQuantity);
                })
                .toList(); // 변환된 아이템들을 리스트로 수집
        BigDecimal promotionDiscountPay = promotionBonusItems.stream().map(Item::getTotalQuantity)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        BigDecimal membershipDiscountPay = cart.stream()
                .filter(item -> promotionBonusItems.stream()
                        .anyMatch(bonusItem -> bonusItem.product().getName().equals(item.product().getName())))
                .map(item -> item.getTotalQuantity().multiply(BigDecimal.valueOf(0.3)))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        BigDecimal willPayAmounts = totalPay.subtract(promotionDiscountPay).subtract(membershipDiscountPay);
        return new ReceiptDto(promotionBonusItems, totalPay, promotionDiscountPay, membershipDiscountPay, willPayAmounts);
    }

    public List<Item> createCart(List<ItemInputDto> userItemInput) {
        List<Item> items = new ArrayList<>();
        userItemInput.forEach(itemDto -> items.addAll(processItem(itemDto)));
        return items;
    }

    private List<Item> processItem(ItemInputDto itemDto) {
        String itemName = itemDto.itemName();
        BigDecimal quantity = itemDto.quantity();
        storeService.validatePurchasableProduct(itemName, quantity);

        if (!storeService.hasActivePromotion(itemName)) {
            return List.of(createGeneralItem(itemName, quantity));
        }
        if (!storeService.isSufficientQuantityPromotionProduct(itemName, quantity)) {
            return createMixedItems(itemName, quantity);
        }

        return createPromotionItems(itemName, quantity);
    }

    private Item createGeneralItem(String itemName, BigDecimal quantity) {
        return new Item(storeService.purchaseGeneralProduct(itemName, quantity), quantity);
    }

    private List<Item> createPromotionItems(String itemName, BigDecimal quantity) {
        Product promotionProduct = storeService.getPromotionProduct(itemName);

        if (storeService.isSufficientQuantityToGetBonusProduct(itemName, quantity) && askForMoreBonus(itemName,
                promotionProduct)) {
            quantity = quantity.add(promotionProduct.getPromotion().get().getGetAmount());
        }

        return List.of(new Item(storeService.purchasePromotionProduct(itemName, quantity), quantity));
    }

    private boolean askForMoreBonus(String itemName, Product promotionProduct) {
        return InputView.askUserGetMoreBonusProduct(itemName, promotionProduct.getPromotion().get().getGetAmount());
    }

    private List<Item> createMixedItems(String itemName, BigDecimal quantity) {
        Product promotionProduct = storeService.getPromotionProduct(itemName);
        BigDecimal fullPriceCount = calculateFullPriceItemCount(quantity, promotionProduct);

        if (InputView.askFullPricePurchase(itemName, fullPriceCount)) {
            return storeService.getMixedItems(itemName, quantity, promotionProduct);
        }

        return List.of(new Item(storeService.purchasePromotionProduct(itemName, quantity.subtract(fullPriceCount)),
                quantity.subtract(fullPriceCount)));
    }

    private BigDecimal calculateFullPriceItemCount(BigDecimal itemQuantity, Product promotionProduct) {
        BigDecimal generalProductQuantity = itemQuantity.subtract(promotionProduct.getQuantity());
        return generalProductQuantity.add(itemQuantity.remainder(promotionProduct.getPromotion().get().getGetAmount()
                .add(promotionProduct.getPromotion().get().getBuyAmount())));
    }
}