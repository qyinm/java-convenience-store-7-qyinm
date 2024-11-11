package store.service;

import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Set;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import store.domain.Product;
import store.domain.Promotion;
import store.repository.StoreRepository;

class StoreServiceTest {
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    private StoreRepository storeRepository;
    private StoreService storeService;

    @BeforeEach
    void setUp() {
        ;

        // 프로모션
        Promotion promotion = new Promotion("탄산2+1", BigDecimal.valueOf(2), BigDecimal.valueOf(1),
                LocalDate.parse("2024-01-01", DATE_FORMATTER), LocalDate.parse("2024-12-31", DATE_FORMATTER));
        Promotion unactivePromotion = new Promotion("탄산1+1", BigDecimal.valueOf(2), BigDecimal.valueOf(1),
                LocalDate.parse("2023-01-01", DATE_FORMATTER), LocalDate.parse("2023-12-31", DATE_FORMATTER));


        Set<Product> products = Set.of(new Product("콜라", BigDecimal.valueOf(1000), BigDecimal.valueOf(10), promotion),
                new Product("콜라", BigDecimal.valueOf(1000), BigDecimal.valueOf(10), promotion),
                new Product("사이다", BigDecimal.valueOf(1000), BigDecimal.valueOf(5), promotion),
                new Product("환타", BigDecimal.valueOf(1000), BigDecimal.valueOf(5), unactivePromotion),
                new Product("콜라", BigDecimal.valueOf(1000), BigDecimal.valueOf(10), null),
                new Product("사이다", BigDecimal.valueOf(1000), BigDecimal.valueOf(5), null));

        // StoreRepository 직접 구현
        storeRepository = new StoreRepository(products);
        storeService = new StoreService(storeRepository);
    }

    @Test
    @DisplayName("프로모션이 적용된 상품은 true 반환")
    void 프로모션_적용된_상품_true() {
        // When
        boolean result = storeService.hasActivePromotion("콜라");

        // Then
        assertThat(result).isTrue();
    }

    @Test
    @DisplayName("프로모션이 적용되지 않은 상품은 false 반환")
    void 프로모션_적용되지_않은_상품_false() {
        // When
        boolean result = storeService.hasActivePromotion("환타");

        // Then
        assertThat(result).isFalse();
    }

    @Test
    @DisplayName("존재하지 않는 프로모션 상품은 false 반환")
    void 존재하지_않는_프로모션_상품은_false() {
        // When
        boolean result = storeService.hasActivePromotion("오렌지");

        // Then
        assertThat(result).isFalse();
    }

    @Test
    @DisplayName("일반 상품이 충분한 수량일 때, true 반환")
    void 일반_상품_충분한_수량일때_true() {
        // When
        boolean result = storeService.isSufficientQuantityGeneralProduct("콜라", BigDecimal.valueOf(5));

        // Then
        assertThat(result).isTrue();
    }

    @Test
    @DisplayName("일반 상품이 부족한 수량일 때, false 반환")
    void 일반_상품_부족한_수량일때_false() {
        // When
        boolean result = storeService.isSufficientQuantityGeneralProduct("사이다", BigDecimal.valueOf(10));

        // Then
        assertThat(result).isFalse();
    }

    @Test
    @DisplayName("프로모션 상품이 충분한 수량일 때, true 반환")
    void 프로모션_상품_충분한_수량일때_true() {
        // When
        boolean result = storeService.isSufficientQuantityPromotionProduct("콜라", BigDecimal.valueOf(5));

        // Then
        assertThat(result).isTrue();
    }

    @Test
    @DisplayName("프로모션 상품이 부족한 수량일 때, false 반환")
    void 프로모션_상품_부족한_수량일때_false() {
        // When
        boolean result = storeService.isSufficientQuantityPromotionProduct("사이다", BigDecimal.valueOf(10));

        // Then
        assertThat(result).isFalse();
    }

    @Test
    @DisplayName("프로모션 상품이 보너스 받을 수량을 만족할 때 true 반환")
    void 프로모션_상품_보너스_충분_수량일때_true() {
        // When
        boolean result = storeService.isSufficientQuantityToGetBonusProduct("콜라", BigDecimal.valueOf(2));

        // Then
        assertThat(result).isTrue();
    }

    @Test
    @DisplayName("프로모션 상품이 보너스 받을 수량을 만족하지 않을 때 false 반환")
    void 프로모션_상품_보너스_충분하지않은_수량일때_false() {
        // When
        boolean result = storeService.isSufficientQuantityToGetBonusProduct("사이다", BigDecimal.valueOf(1));

        // Then
        assertThat(result).isFalse();
    }

    @Test
    @DisplayName("프로모션 상품이 보너스 받을 수량을 줄 재고가 없을 때 false 반환")
    void 프로모션_상품_보너스_충분하지않은_수량을_줄_재고가_없을때_false() {
        // When
        boolean result = storeService.isSufficientQuantityToGetBonusProduct("사이다", BigDecimal.valueOf(1001));

        // Then
        assertThat(result).isFalse();
    }

    @Test
    @DisplayName("일반 상품이 존재하면 해당 상품을 반환")
    void 일반_상품_존재_시_반환() {
        // When
        Product product = storeService.getGeneralProduct("콜라");

        // Then
        assertThat(product).isNotNull();
        assertThat(product.getName()).isEqualTo("콜라");
    }

    @Test
    @DisplayName("일반 상품을 구매하면 수량이 차감된다")
    void 일반_상품_구매_시_수량_차감() {
        // Given
        Product product = storeService.getGeneralProduct("콜라");

        // When
        Product updatedProduct = storeService.purchaseGeneralProduct("콜라", BigDecimal.valueOf(5));

        // Then
        assertThat(updatedProduct.getQuantity()).isEqualTo(BigDecimal.valueOf(5));  // 10 - 5 = 5
    }

    @Test
    @DisplayName("프로모션 상품이 존재하면 해당 상품을 반환")
    void 프로모션_상품_존재_시_반환() {
        // When
        Product product = storeService.getPromotionProduct("콜라");

        // Then
        assertThat(product).isNotNull();
        assertThat(product.getName()).isEqualTo("콜라");
    }

    @Test
    @DisplayName("프로모션 상품을 구매하면 수량이 차감된다")
    void 프로모션_상품_구매_시_수량_차감() {
        // Given
        Product product = storeService.getPromotionProduct("콜라");

        // When
        Product updatedProduct = storeService.purchasePromotionProduct("콜라", BigDecimal.valueOf(3));

        // Then
        assertThat(updatedProduct.getQuantity()).isEqualTo(BigDecimal.valueOf(7));  // 10 - 3 = 7
    }
}