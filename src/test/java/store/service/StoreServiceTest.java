package store.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static store.exception.store.StoreErrorCode.NOT_FOUND_PRODUCT;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Set;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import store.domain.Item;
import store.domain.Product;
import store.domain.Promotion;
import store.dtos.ReceiptDto;
import store.exception.StoreException;
import store.repository.StoreRepository;

class StoreServiceTest {
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    private StoreRepository storeRepository;
    private StoreService storeService;

    @BeforeEach
    void setUp() {
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

    @Test
    @DisplayName("존재하지 않는 상품은 예외를 발생시킨다")
    void 존재하지_않는_상품은_예외_발생() {
        // When & Then
        assertThatThrownBy(() -> storeService.getGeneralProduct("오렌지"))
                .isInstanceOf(StoreException.class)
                .hasMessageContaining(NOT_FOUND_PRODUCT.getMessage());
    }

    @Test
    @DisplayName("프로모션, 일반 상품을 혼합하여 반환")
    void 프로모션_일반_상품_혼합_반환() {
        // Given
        Product promotionProduct = storeService.getPromotionProduct("콜라");

        // When
        List<Item> mixedItems = storeService.getMixedItems("콜라", BigDecimal.valueOf(15), promotionProduct);

        // Then
        assertThat(mixedItems).hasSize(2);
        Item expectedPromotionItem = mixedItems.get(0);
        Item expectedGeneralItem = mixedItems.get(1);
        assertThat(expectedPromotionItem.product().getName()).isEqualTo("콜라");
        assertThat(expectedPromotionItem.quantity()).isEqualTo(BigDecimal.valueOf(10)); // 총 15 구매 중 프로모션 10개
        assertThat(expectedGeneralItem.product().getName()).isEqualTo("콜라");
        assertThat(expectedGeneralItem.quantity()).isEqualTo(BigDecimal.valueOf(5)); // 남은 5개 일반 5개
    }

    @Test
    @DisplayName("사용자 구매 계산")
    void 사용자_구매_계산() {
        // Given
        List<Item> cart = List.of(new Item(storeService.getPromotionProduct("콜라"), BigDecimal.valueOf(5)),
                new Item(storeService.getGeneralProduct("사이다"), BigDecimal.valueOf(2)));

        // When
        ReceiptDto receipt = storeService.calculateUserPurchase(cart, true);

        // Then
        assertThat(receipt.totalPay()).isNotNull();
        // 콜라: 2+1로 4000, 사이다: 2000 - 600(멤버십 할인)
        assertThat(receipt.willPayAmounts()).isEqualTo(BigDecimal.valueOf(5400));
        assertThat(receipt.membershipDiscountPay()).isEqualTo(BigDecimal.valueOf(600));
        assertThat(receipt.promotionBonusItems().size()).isEqualTo(1);
    }

    @Test
    @DisplayName("사용자 구매 계산")
    void 사용자_구매_계산_멤버십_할인_최대_8000() {
        // Given
        Promotion promotion = new Promotion("탄산2+1", BigDecimal.valueOf(2), BigDecimal.valueOf(1),
                LocalDate.parse("2024-01-01", DATE_FORMATTER), LocalDate.parse("2024-12-31", DATE_FORMATTER));
        Set<Product> productSet = Set.of(
                new Product("환타", BigDecimal.valueOf(100), BigDecimal.valueOf(10), promotion),
                new Product("콜라", BigDecimal.valueOf(10000), BigDecimal.valueOf(1000), null),
                new Product("바나나", BigDecimal.valueOf(10000), BigDecimal.valueOf(1000), null));
        storeRepository = new StoreRepository(productSet);
        storeService = new StoreService(storeRepository);
        List<Item> cart = List.of(
                new Item(storeService.getGeneralProduct("콜라"), BigDecimal.valueOf(100)),
                new Item(storeService.getGeneralProduct("바나나"), BigDecimal.valueOf(100)),
                new Item(storeService.getPromotionProduct("환타"), BigDecimal.valueOf(3))
        );

        // When
        ReceiptDto receipt = storeService.calculateUserPurchase(cart, true);

        // Then
        assertThat(receipt.totalPay()).isNotNull();
        //
        assertThat(receipt.willPayAmounts()).isEqualTo(BigDecimal.valueOf(1_992_200));
        assertThat(receipt.membershipDiscountPay()).isEqualTo(BigDecimal.valueOf(8000));
        assertThat(receipt.promotionDiscountPay()).isEqualTo(BigDecimal.valueOf(100));
        assertThat(receipt.promotionBonusItems().size()).isEqualTo(1);
    }
}