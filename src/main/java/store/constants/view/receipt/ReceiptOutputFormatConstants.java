package store.constants.view.receipt;

public enum ReceiptOutputFormatConstants {
    RECEIPT_HEADER("============== W 편의점 ==============="),
    RECEIPT_TABLE_HEADER("%-12s%-6s%-12s%n"),
    RECEIPT_ITEM_FORMAT("%-12s%-6s%,12d%n"),
    RECEIPT_PROMOTION_HEADER("============= 증정 ================"),
    RECEIPT_PROMOTION_ITEM_FORMAT("%-12s%-6s%n"),
    RECEIPT_FOOTER("===================================="),
    RECEIPT_TOTAL_FORMAT("%-14s%-6s%,12d%n");

    private final String format;

    ReceiptOutputFormatConstants(String format) {
        this.format = format;
    }

    public String getFormat() {
        return format;
    }
}
