package store.view;

import static store.constants.view.OutputViewConstants.*;
import static store.constants.view.ReceiptOutputConstants.RECEIPT_FINAL_TOTAL;
import static store.constants.view.ReceiptOutputConstants.RECEIPT_MEMBERSHIP_DISCOUNT;
import static store.constants.view.ReceiptOutputConstants.RECEIPT_PROMOTION_DISCOUNT;
import static store.constants.view.ReceiptOutputConstants.RECEIPT_TOTAL;
import static store.constants.view.product.ProductOutputConstants.PRODUCT_EMPTY_QUANTITY;
import static store.constants.view.product.ProductOutputConstants.PRODUCT_INDEX;
import static store.constants.view.product.ProductOutputConstants.PROMOTION_EMPTY;
import static store.constants.view.product.ProductOutputFormatConstants.PRODUCT_PRICE_FORMAT;
import static store.constants.view.product.ProductOutputFormatConstants.PRODUCT_QUANTITY_FORMAT;
import static store.constants.view.receipt.ReceiptOutputFormatConstants.RECEIPT_FOOTER;
import static store.constants.view.receipt.ReceiptOutputFormatConstants.RECEIPT_HEADER;
import static store.constants.view.receipt.ReceiptOutputFormatConstants.RECEIPT_ITEM_FORMAT;
import static store.constants.view.receipt.ReceiptOutputFormatConstants.RECEIPT_PROMOTION_HEADER;
import static store.constants.view.receipt.ReceiptOutputFormatConstants.RECEIPT_PROMOTION_ITEM_FORMAT;
import static store.constants.view.receipt.ReceiptOutputFormatConstants.RECEIPT_TABLE_HEADER;
import static store.constants.view.receipt.ReceiptOutputFormatConstants.RECEIPT_TOTAL_FORMAT;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;
import store.domain.Item;
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

        String productOutput = String.format("%s %s %s %s %s", PRODUCT_INDEX.getLabel(),
                product.getName(),
                String.format(PRODUCT_PRICE_FORMAT.getFormat(), product.getPrice().intValue()),
                getProductQuantityFormatted(product),
                promotionInfo);
        System.out.println(productOutput);
    }

    private static String getProductQuantityFormatted(Product product) {
        if (product.getQuantity().compareTo(BigDecimal.ZERO) == 0) {
            return PRODUCT_EMPTY_QUANTITY.getLabel();
        }
        return String.format(PRODUCT_QUANTITY_FORMAT.getFormat(), product.getQuantity().intValue());
    }

    private static String getPromotionInfo(Product product) {
        return product.getPromotion()
                .map(Promotion::getName)
                .orElse(PROMOTION_EMPTY.getLabel());
    }

    public static void printReceipt(List<Item> cart, ReceiptDto receipt) {
        System.out.println(RECEIPT_HEADER.getFormat());

        // 상품 테이블 헤더 출력
        System.out.printf(RECEIPT_TABLE_HEADER.getFormat(), "상품명", "수량", "금액");

        // 상품별 정보 출력
        for (Item item : cart) {
            System.out.printf(RECEIPT_ITEM_FORMAT.getFormat(),
                    item.product().getName(),
                    item.quantity(),
                    item.product().getPrice().intValue());
        }

        // 증정 품목 출력
        System.out.println(RECEIPT_PROMOTION_HEADER.getFormat());
        for (Item bonusItem : receipt.promotionBonusItems()) {
            System.out.printf(RECEIPT_PROMOTION_ITEM_FORMAT.getFormat(),
                    bonusItem.product().getName(),
                    bonusItem.quantity());
        }

        // 영수증 풋터와 합계 출력
        System.out.println(RECEIPT_FOOTER.getFormat());

        System.out.printf(RECEIPT_TOTAL_FORMAT.getFormat(),
                RECEIPT_TOTAL.getLabel(), "",
                receipt.totalPay().intValue());

        System.out.printf(RECEIPT_TOTAL_FORMAT.getFormat(),
                RECEIPT_PROMOTION_DISCOUNT.getLabel(), "",
                -receipt.promotionDiscountPay().intValue());

        System.out.printf(RECEIPT_TOTAL_FORMAT.getFormat(),
                RECEIPT_MEMBERSHIP_DISCOUNT.getLabel(), "",
                -receipt.membershipDiscountPay().intValue());

        System.out.printf(RECEIPT_TOTAL_FORMAT.getFormat(),
                RECEIPT_FINAL_TOTAL.getLabel(), "",
                receipt.willPayAmounts().intValue());
    }
}
