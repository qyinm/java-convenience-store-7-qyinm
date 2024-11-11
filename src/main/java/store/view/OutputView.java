package store.view;

import static store.constants.view.OutputViewConstants.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;
import store.domain.Item;
import store.domain.Product;
import store.domain.Promotion;
import store.dtos.ReceiptDto;

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

    public static void printReceipt(List<Item> cart, ReceiptDto receipt) {
        // 출력
        System.out.println("============== W 편의점 ===============");
        System.out.printf("%-12s%-6s%-12s%n", "상품명", "수량", "금액");  // 헤더 출력

        for (Item item : cart) {
            System.out.printf("%-12s%-6s%,12d%n", item.product().getName(), item.quantity(), item.product().getPrice().intValue());
        }

        System.out.println("============= 증정 ================");
        for (Item bonusItem : receipt.promotionBonusItems()) {
            System.out.printf("%-12s%-6s%n", bonusItem.product().getName(), bonusItem.quantity());
        }

        System.out.println("====================================");
        System.out.printf("%-14s%-6s%,12d%n", "총구매액", cart.stream().map(Item::quantity).reduce(BigDecimal.ZERO, BigDecimal::add), receipt.totalPay().intValue());
        System.out.printf("%-14s%-6s%,12d%n", "행사할인", "", -receipt.promotionDiscountPay().intValue());
        System.out.printf("%-14s%-6s%,12d%n", "멤버십할인", "", -receipt.membershipDiscountPay().intValue());
        System.out.printf("%-14s%-6s%,12d%n", "내실돈", "", receipt.willPayAmounts().intValue());

    }
}
