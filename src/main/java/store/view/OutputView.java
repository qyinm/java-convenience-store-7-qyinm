package store.view;

import static store.constants.view.OutputViewConstants.ASK_FULL_PRICE_PURCHASE_PREFIX;
import static store.constants.view.OutputViewConstants.ASK_GET_MEMBERSHIP_DISCOUNT_PREFIX;
import static store.constants.view.OutputViewConstants.ASK_GET_MORE_BONUS_PRODUCTS;
import static store.constants.view.OutputViewConstants.ASK_MORE_SHOPPING_PREFIX;
import static store.constants.view.OutputViewConstants.STORE_INVENTORY_PREFIX;
import static store.constants.view.OutputViewConstants.USER_ITEM_INPUT_PREFIX;
import static store.constants.view.product.ProductOutputConstants.PRODUCT_EMPTY_QUANTITY;
import static store.constants.view.product.ProductOutputConstants.PRODUCT_INDEX;
import static store.constants.view.product.ProductOutputConstants.PROMOTION_EMPTY;
import static store.constants.view.product.ProductOutputFormatConstants.PRODUCT_PRICE_FORMAT;
import static store.constants.view.product.ProductOutputFormatConstants.PRODUCT_QUANTITY_FORMAT;
import static store.constants.view.receipt.ReceiptOutputConstants.RECEIPT_FINAL_TOTAL;
import static store.constants.view.receipt.ReceiptOutputConstants.RECEIPT_MEMBERSHIP_DISCOUNT;
import static store.constants.view.receipt.ReceiptOutputConstants.RECEIPT_PROMOTION_DISCOUNT;
import static store.constants.view.receipt.ReceiptOutputConstants.RECEIPT_TOTAL;
import static store.constants.view.receipt.ReceiptOutputFormatConstants.RECEIPT_FOOTER;
import static store.constants.view.receipt.ReceiptOutputFormatConstants.RECEIPT_HEADER;
import static store.constants.view.receipt.ReceiptOutputFormatConstants.RECEIPT_ITEM_FORMAT;
import static store.constants.view.receipt.ReceiptOutputFormatConstants.RECEIPT_PROMOTION_HEADER;
import static store.constants.view.receipt.ReceiptOutputFormatConstants.RECEIPT_TABLE_HEADER;
import static store.constants.view.receipt.ReceiptOutputFormatConstants.RECEIPT_TOTAL_FORMAT;

import java.math.BigDecimal;
import java.util.Set;
import store.domain.Cart;
import store.domain.Product;
import store.domain.Promotion;
import store.dtos.ReceiptDto;

public class OutputView {
    public static void printUserItemInputPrefix() {
        System.out.println(USER_ITEM_INPUT_PREFIX.getOutputViewPrefix());
    }

    public static void printAskGetMoreBonusProducts(String itemName, BigDecimal quantity) {
        System.out.println(ASK_GET_MORE_BONUS_PRODUCTS.getOutputViewPrefix().formatted(itemName, quantity.intValue()));
    }

    public static void printAskFullPricePurchasePrefix(String itemName, BigDecimal quantity) {
        System.out.println(
                ASK_FULL_PRICE_PURCHASE_PREFIX.getOutputViewPrefix().formatted(itemName, quantity.intValue()));
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

        String productOutput = String.format("%s %s %s %s %s", PRODUCT_INDEX.getLabel(), product.getName(),
                String.format(PRODUCT_PRICE_FORMAT.getFormat(), product.getPrice().intValue()),
                getProductQuantityFormatted(product), promotionInfo);
        System.out.println(productOutput);
    }

    private static String getProductQuantityFormatted(Product product) {
        if (product.getQuantity().compareTo(BigDecimal.ZERO) == 0) {
            return PRODUCT_EMPTY_QUANTITY.getLabel();
        }
        return String.format(PRODUCT_QUANTITY_FORMAT.getFormat(), product.getQuantity().intValue());
    }

    private static String getPromotionInfo(Product product) {
        return product.getPromotion().map(Promotion::getName).orElse(PROMOTION_EMPTY.getLabel());
    }

    public static void printReceipt(Cart cart, ReceiptDto receipt) {
        System.out.println(RECEIPT_HEADER.getFormat());
        System.out.printf(RECEIPT_TABLE_HEADER.getFormat(), "상품명", "수량", "금액");
        printItemsInformation(cart);
        printBonusItems(receipt);
        System.out.println(RECEIPT_FOOTER.getFormat());
        printTotalAmountStatistics(cart, receipt);
    }

    private static void printItemsInformation(Cart cart) {
        String formattedAllItemsWithNameAndQuantity = cart.getFormattedAllItemsWithNameAndPriceAndQuantity(
                RECEIPT_ITEM_FORMAT.getFormat());
        System.out.print(formattedAllItemsWithNameAndQuantity);
    }

    private static void printTotalAmountStatistics(Cart cart, ReceiptDto receipt) {
        printTotalAmount(cart.getTotalItemsQuantity(), receipt);
        printPromotionDiscountAmount(receipt);
        printMembershipDiscountAmount(receipt);
        printWillPayAmount(receipt);
    }

    private static void printWillPayAmount(ReceiptDto receipt) {
        System.out.printf(RECEIPT_TOTAL_FORMAT.getFormat(), RECEIPT_FINAL_TOTAL.getLabel(), "",
                receipt.willPayAmounts().intValue());
    }

    private static void printMembershipDiscountAmount(ReceiptDto receipt) {
        System.out.printf(RECEIPT_TOTAL_FORMAT.getFormat(), RECEIPT_MEMBERSHIP_DISCOUNT.getLabel(), "",
                -receipt.membershipDiscountPay().intValue());
    }

    private static void printPromotionDiscountAmount(ReceiptDto receipt) {
        System.out.printf(RECEIPT_TOTAL_FORMAT.getFormat(), RECEIPT_PROMOTION_DISCOUNT.getLabel(), "",
                -receipt.promotionDiscountPay().intValue());
    }

    private static void printTotalAmount(BigDecimal totalQuantity, ReceiptDto receipt) {
        System.out.printf(RECEIPT_TOTAL_FORMAT.getFormat(), RECEIPT_TOTAL.getLabel(), totalQuantity,
                receipt.totalPay().intValue());
    }

    private static void printBonusItems(ReceiptDto receipt) {
        System.out.println(RECEIPT_PROMOTION_HEADER.getFormat());
        printItemsInformation(receipt.promotionBonusItems());
    }
}
