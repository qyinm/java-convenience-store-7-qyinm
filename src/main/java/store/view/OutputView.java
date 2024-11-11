package store.view;

import static store.constants.view.OutputViewConstants.*;

import java.math.BigDecimal;
import java.util.Set;
import store.domain.Product;
import store.domain.Promotion;

public class OutputView {
    private static final String PRODUCT_INDEX = "-";
    private static final String PROMOTION_EMPTY = "";
    private static final String PRODUCT_PRICE_FORMAT = "%,d원";
    private static final String PRODUCT_QUANTITY_FORMAT = "%d개";
    private static final String PRODUCT_EMPTY_QUANTITY = "재고 없음";

    public static void printUserItemInputPrefix() {
        System.out.println(USER_ITEM_INPUT_PREFIX.getOutputViewPrefix());
    }

    public static void printAskGetMoreBonusProducts(String itemName, BigDecimal quantity) {
        System.out.println(ASK_GET_MORE_BONUS_PRODUCTS.getOutputViewPrefix().formatted(itemName, quantity.intValue()));
    }

    public static void printAskFullPricePurchasePrefix(String itemName, BigDecimal quantity) {
        System.out.println(ASK_FULL_PRICE_PURCHASE_PREFIX.getOutputViewPrefix().formatted(itemName, quantity.intValue()));
    }

    public static void printAskGetMembershipDiscountPrefix() {
        System.out.println(ASK_GET_MEMBERSHIP_DISCOUNT_PREFIX.getOutputViewPrefix());
    }

    public static void printAskMoreShoppingPrefix() {
        System.out.println(ASK_MORE_SHOPPING_PREFIX.getOutputViewPrefix());
    }

    public static void printCurrentStoreInventory(Set<Product> allProduct) {
        System.out.println(STORE_INVENTORY_PREFIX.getOutputViewPrefix());

        allProduct.forEach(OutputView::printProduct);
    }

    private static void printProduct(Product product) {
        String promotionInfo = getPromotionInfo(product);  // 프로모션이 없으면 공백 출력

        String productOutput = String.format("%s %s %s %s %s", PRODUCT_INDEX,
                product.getName(),
                String.format(PRODUCT_PRICE_FORMAT, product.getPrice().intValue()),
                getProductQuantityFormatted(product),
                promotionInfo);
        System.out.println(productOutput);
    }

    private static String getProductQuantityFormatted(Product product) {
        if (product.getQuantity().compareTo(BigDecimal.ZERO) == 0) {
            return PRODUCT_EMPTY_QUANTITY;
        }
        return String.format(PRODUCT_QUANTITY_FORMAT, product.getQuantity().intValue());
    }

    private static String getPromotionInfo(Product product) {
        return product.getPromotion()
                .map(Promotion::getName)
                .orElse(PROMOTION_EMPTY);
    }
}
