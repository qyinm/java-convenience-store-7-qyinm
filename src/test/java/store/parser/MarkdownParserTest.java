package store.parser;

import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import store.domain.Product;
import store.domain.Promotion;

class MarkdownParserTest {
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private static final String TEST_PROMOTIONS_PATH = "src/test/resources/promotions.md";
    private static final String TEST_PRODUCTS_PATH = "src/test/resources/products.md";

    private static Promotion promotion;
    private static Set<Product> productSet;

    @BeforeAll
    static void setUp() {
        promotion = new Promotion("탄산2+1", BigDecimal.valueOf(2), BigDecimal.valueOf(1),
                LocalDate.parse("2024-01-01", DATE_FORMATTER), LocalDate.parse("2024-12-31", DATE_FORMATTER));
        productSet = Set.of(new Product("콜라", BigDecimal.valueOf(1000), BigDecimal.valueOf(10), promotion),
                new Product("콜라", BigDecimal.valueOf(1000), BigDecimal.valueOf(10), null),
                new Product("사이다", BigDecimal.valueOf(1000), BigDecimal.valueOf(8), promotion),
                new Product("사이다", BigDecimal.valueOf(1000), BigDecimal.valueOf(7), null));
    }

    @Test
    @DisplayName("프로모션 파싱 테스트")
    void 프로모션_파싱_테스트() {
        // Given
        List<Promotion> promotionList = List.of(promotion);

        // When
        Map<String, Promotion> promotionMap = MarkdownParser.parsePromotions(TEST_PROMOTIONS_PATH);

        // Then
        assertThat(promotionMap.size()).isEqualTo(1);
        assertThat(promotionMap.values()).usingRecursiveFieldByFieldElementComparator().containsAll(promotionList);
    }

    @Test
    @DisplayName("프로모션 파싱 실패 테스트 - 파일에 없는 프로모션은 반환 하지 않습니다.")
    void 프로모션_파싱_실패_테스트() {
        // Given
        Promotion fakePromotion = new Promotion("가짜", BigDecimal.valueOf(1), BigDecimal.valueOf(1), LocalDate.MIN, LocalDate.MAX);

        // When
        Map<String, Promotion> promotionMap = MarkdownParser.parsePromotions(TEST_PROMOTIONS_PATH);

        // Then
        assertThat(promotionMap.size()).isEqualTo(1);
        assertThat(promotionMap.values()).usingRecursiveFieldByFieldElementComparator().doesNotContain(fakePromotion);
    }

    @Test
    @DisplayName("프로덕트 파싱 테스트")
    void 프로덕트_파싱_테스트() {
        // Given
        Map<String, Promotion> promotionMap = MarkdownParser.parsePromotions(TEST_PROMOTIONS_PATH);
        Set<Product> expectedProducts = productSet;

        // When
        Set<Product> parsedProducts = MarkdownParser.parseProducts(TEST_PRODUCTS_PATH, promotionMap);

        // Then
        assertThat(parsedProducts.size()).isEqualTo(expectedProducts.size());
        assertThat(parsedProducts).usingRecursiveFieldByFieldElementComparator().containsAll(expectedProducts);
    }

    @Test
    @DisplayName("프로덕트 파싱 실패 테스트 - 파일에 없는 프로덕트은 반환 하지 않습니다.")
    void 프로모션_파싱_실패_테스트_존재하지_않는_프로덕트() {
        // Give
        Map<String, Promotion> promotionMap = MarkdownParser.parsePromotions(TEST_PROMOTIONS_PATH);
        Product fakeProduct = new Product("가짜", BigDecimal.valueOf(1), BigDecimal.valueOf(1), null);

        // When
        Set<Product> parsedProducts = MarkdownParser.parseProducts(TEST_PRODUCTS_PATH, promotionMap);

        // Then
        assertThat(parsedProducts).usingRecursiveFieldByFieldElementComparator().doesNotContain(fakeProduct);
    }
}