package store.constants.view.product;

public enum ProductOutputConstants {
    PRODUCT_INDEX("-"),
    PROMOTION_EMPTY(""),
    PRODUCT_EMPTY_QUANTITY("재고 없음");

    private final String label;

    ProductOutputConstants(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}
