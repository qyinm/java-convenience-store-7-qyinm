package store.domain;

import static camp.nextstep.edu.missionutils.DateTimes.now;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

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

    public boolean isActive() {
        LocalDate nowDate = now().toLocalDate();
        return (promotionStartDate.isEqual(nowDate) || promotionStartDate.isBefore(nowDate)) && (
                promotionEndDate.isEqual(nowDate) || promotionEndDate.isAfter(nowDate));
    }

    public boolean isAvailableToGetBonus(BigDecimal bonus) {
        return bonus.remainder(buyAmount.add(getAmount)).compareTo(buyAmount) == 0;
    }
}
