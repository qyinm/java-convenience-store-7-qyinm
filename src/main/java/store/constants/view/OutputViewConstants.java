package store.constants.view;

public enum OutputViewConstants {
    STORE_INVENTORY_PREFIX("""
            안녕하세요. W편의점입니다.
            현재 보유하고 있는 상품입니다.
            """),
    USER_ITEM_INPUT_PREFIX("구매하실 상품명과 수량을 입력해 주세요. (예: [사이다-2],[감자칩-1])"),
    ASK_FULL_PRICE_PURCHASE_PREFIX("현재 %s %d개는 프로모션 할인이 적용되지 않습니다. 그래도 구매하시겠습니까? (Y/N)"),
    ASK_GET_MEMBERSHIP_DISCOUNT_PREFIX("멤버십 할인을 받으시겠습니까? (Y/N)"),
    ASK_MORE_SHOPPING_PREFIX("감사합니다. 구매하고 싶은 다른 상품이 있나요? (Y/N)");

    private final String ouputViewPrefix;

    OutputViewConstants(String ouputViewPrefix) {
        this.ouputViewPrefix = ouputViewPrefix;
    }

    public String getOuputViewPrefix() {
        return ouputViewPrefix;
    }
}
