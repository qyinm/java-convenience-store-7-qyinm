package store.domain;

import java.math.BigDecimal;
import java.time.LocalDate;

public class Promotion {
    private final String name;
    private final BigDecimal buyAmount;
    private final BigDecimal getAmount;
    private final LocalDate promotionStartDate;
    private final LocalDate promotionEndDate;

    public Promotion(String name, BigDecimal buyAmount, BigDecimal getAmount,
                     LocalDate promotionStartDate, LocalDate promotionEndDate) {
        this.name = name;
        this.buyAmount = buyAmount;
        this.getAmount = getAmount;
        this.promotionStartDate = promotionStartDate;
        this.promotionEndDate = promotionEndDate;
    }


    public String getName() {
        return name;
    }

    public BigDecimal getBuyAmount() {
        return buyAmount;
    }

    public BigDecimal getGetAmount() {
        return getAmount;
    }

    public LocalDate getPromotionStartDate() {
        return promotionStartDate;
    }

    public LocalDate getPromotionEndDate() {
        return promotionEndDate;
    }
}
