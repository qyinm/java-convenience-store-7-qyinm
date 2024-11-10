package store.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;
import java.util.Set;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import store.domain.Product;
import store.domain.Promotion;

public class StoreRepositoryTest {

    StoreRepository storeRepository;

    @Test
    @DisplayName("상품명에 해당하는 일반상품 반환 테스트")
    void 상품명에_해당하는_일반상품_반환_테스트() {
        // Given
        Product product = new Product("땅콩", BigDecimal.valueOf(1000), BigDecimal.valueOf(100), null);
        storeRepository = new StoreRepository(Set.of(product));

        // When
        Optional<Product> generalProduct = storeRepository.findGeneralProductByName(product.getName());

        // Then
        assertThat(generalProduct).isPresent();
        assertThat(generalProduct.get().getPrice()).isEqualTo(product.getPrice());
    }

    @Test
    @DisplayName("상품명에 해당하는 프로모션상품 반환 테스트")
    void 상품명에_해당하는_프로모션상품_반환_테스트() {
        // Given
        Promotion promotion = new Promotion("1+2", BigDecimal.valueOf(1), BigDecimal.valueOf(2), LocalDate.MIN,
                LocalDate.MAX);
        Product product = new Product("땅콩", BigDecimal.valueOf(1000), BigDecimal.valueOf(100), promotion);
        storeRepository = new StoreRepository(Set.of(product));

        // When
        Optional<Product> generalProduct = storeRepository.findPromotionProductByName(product.getName());

        // Then
        assertThat(generalProduct).isPresent();
        assertThat(generalProduct.get().getPrice()).isEqualTo(product.getPrice());
    }
}
