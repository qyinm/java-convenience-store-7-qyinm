package store.controller;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import store.domain.Cart;
import store.domain.Item;
import store.domain.Product;
import store.dtos.ItemInputDto;
import store.dtos.ReceiptDto;
import store.service.StoreService;
import store.utils.exception.RetryExceptionHandler;
import store.view.InputView;
import store.view.OutputView;

public class StoreController {

    private final StoreService storeService;

    public StoreController(StoreService storeService) {
        this.storeService = storeService;
    }

    public void runStore() {
        do {
            noticeCurrentInventory();
            Cart cart = RetryExceptionHandler.runUntilNoneLottoException(this::getUserWantToBuyItems);
            boolean applyMembershipDiscount = InputView.askGetMembershipDiscount();

            ReceiptDto receiptDto = storeService.calculateUserPurchase(cart, applyMembershipDiscount);
            OutputView.printReceipt(cart, receiptDto);
        } while (InputView.askMoreGetShopping());
    }

    private Cart getUserWantToBuyItems() {
        List<ItemInputDto> userItemInput = InputView.getUserItemInput();
        return new Cart(createCart(userItemInput));
    }

    private void noticeCurrentInventory() {
        Set<Product> allProducts = storeService.getAllProducts();
        OutputView.printCurrentStoreInventory(allProducts);
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