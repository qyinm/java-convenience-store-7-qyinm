package store.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class CartTest {

    private Cart cart;

    @BeforeEach
    void setUp() {
        // 프로모션
        Promotion promotion = new Promotion("1+100", BigDecimal.valueOf(1), BigDecimal.valueOf(100), LocalDate.MIN,
                LocalDate.MAX);
        Item item1 = new Item(new Product("콜라", BigDecimal.valueOf(1000), BigDecimal.valueOf(100), null),
                BigDecimal.valueOf(3));
        Item item2 = new Item(new Product("물", BigDecimal.valueOf(1000), BigDecimal.valueOf(100), promotion),
                BigDecimal.valueOf(5));

        List<Item> items = List.of(item1, item2);
        cart = new Cart(items);
    }

    @Test
    @DisplayName("프로모션 보너스 아이템이 잘 반환된다")
    void 프로모션_보너스_아이템_반환() {
        // When
        Cart bonusItems = cart.getPromotionBonusItems();

        // Then
        assertThat(bonusItems).isNotNull();
        assertThat(bonusItems.getSize()).isEqualTo(1);
    }

    @Test
    @DisplayName("상품 총 금액을 계산한다")
    void 상품_총_금액_계산() {
        // When
        BigDecimal totalAmount = cart.getTotalItemsAmount();

        // Then
        assertThat(totalAmount).isEqualTo(BigDecimal.valueOf(8000));
    }

    @Test
    @DisplayName("멤버십 할인 금액을 계산한다")
    void 멤버십_할인_금액_계산() {
        // Given
        Cart bonusItems = cart.getPromotionBonusItems();

        // When
        BigDecimal discountAmount = cart.calculateMembershipDiscountAmount(bonusItems);

        // Then
        assertThat(discountAmount).isEqualTo(BigDecimal.valueOf(900));
    }

    @Test
    @DisplayName("총 구매 상품 수량을 계산한다")
    void 총_구매_상품_수량_계산() {
        // When
        BigDecimal totalQuantity = cart.getTotalItemsQuantity();

        // Then
        assertThat(totalQuantity).isEqualTo(BigDecimal.valueOf(8));
    }
}