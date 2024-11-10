package store.service;

import static org.assertj.core.api.Assertions.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import store.domain.Product;
import store.domain.Promotion;
import store.repository.StoreRepository;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

class StoreServiceTest {
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    private StoreRepository storeRepository;
    private StoreService storeService;

    private Product productWithPromotion;
    private Product productWithoutPromotion;

    @BeforeEach
    void setUp() {
        // StoreRepository에 데이터를 넣어줌
        Set<Product> products = new HashSet<>();

        // 프로모션이 있는 상품
        Promotion promotion = new Promotion("탄산2+1", BigDecimal.valueOf(2), BigDecimal.valueOf(1),
                LocalDate.parse("2024-01-01", DATE_FORMATTER), LocalDate.parse("2024-12-31", DATE_FORMATTER));
        productWithPromotion = new Product("콜라", BigDecimal.valueOf(1000), BigDecimal.valueOf(10), promotion);

        // 프로모션이 없는 상품
        productWithoutPromotion = new Product("사이다", BigDecimal.valueOf(1000), BigDecimal.valueOf(10), null);

        products.add(productWithPromotion);
        products.add(productWithoutPromotion);

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
        boolean result = storeService.hasActivePromotion("사이다");

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
}