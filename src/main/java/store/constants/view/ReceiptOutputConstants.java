package store.constants.view;

public enum ReceiptOutputConstants {
    RECEIPT_PROMOTION_DISCOUNT("행사할인"),
    RECEIPT_MEMBERSHIP_DISCOUNT("멤버십할인"),
    RECEIPT_FINAL_TOTAL("내실돈"),
    RECEIPT_TOTAL("총구매액");

    private final String label;

    ReceiptOutputConstants(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}