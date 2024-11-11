package store.constants.view.receipt;

public enum ReceiptOutputFormatConstants {
    RECEIPT_HEADER("==============W 편의점================"),
    RECEIPT_TABLE_HEADER("%-18s%-10s%s%n"),
    RECEIPT_ITEM_FORMAT("%-18s%-10s%,2d%n"),
    RECEIPT_PROMOTION_HEADER("=============증     정==============="),
    RECEIPT_PROMOTION_ITEM_FORMAT("%-18s%-10s%n"),
    RECEIPT_FOOTER("===================================="),
    RECEIPT_TOTAL_FORMAT("%-18s%-10s%,2d%n");

    private final String format;

    ReceiptOutputFormatConstants(String format) {
        this.format = format;
    }

    public String getFormat() {
        return format;
    }
}
