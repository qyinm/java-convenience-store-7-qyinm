package store.constants.view.product;

public enum ProductOutputFormatConstants {
    PRODUCT_PRICE_FORMAT("%,d원"),
    PRODUCT_QUANTITY_FORMAT("%d개");

    private final String format;

    ProductOutputFormatConstants(String format) {
        this.format = format;
    }

    public String getFormat() {
        return format;
    }
}
