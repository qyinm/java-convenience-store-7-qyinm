package store.parser;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import store.domain.Product;
import store.domain.Promotion;

public class MarkdownParser {

    public static Map<String, Promotion> parsePromotions(String filePath) {
        return parse(filePath, PromotionParser::parse).stream()
                .collect(Collectors.toMap(
                        Promotion::getName,
                        promotion -> promotion
                ));
    }

    public static Set<Product> parseProducts(String filePath, Map<String, Promotion> promotionMap) {
        Set<Product> productSetWithOutSoldOutGeneralProducts = new HashSet<>(
                parse(filePath, (line) -> ProductParser.parse(line, promotionMap)));
        return getAllProductsWithOutOfGeneralProducts(productSetWithOutSoldOutGeneralProducts);
    }

    private static Set<Product> getAllProductsWithOutOfGeneralProducts(Set<Product> productSet) {
        Set<Product> products = new HashSet<>();
        for (Product product : productSet) {
            if (doesNotHasGeneralProducts(productSet, product)) {
                products.add(new Product(product.getName(), product.getPrice(), BigDecimal.ZERO, null));
            }
            products.add(product);
        }

        return products;
    }

    private static boolean doesNotHasGeneralProducts(Set<Product> productSet, Product product) {
        return !(productSet.stream()
                .filter(find -> find.getName().equals(product.getName()) && find.getPromotion().isEmpty())
                .count() == 1);
    }

    public static <T> List<T> parse(String filePath, LineParser<T> parser) {
        Path path = Paths.get(filePath);

        try (Stream<String> lines = Files.lines(path)) {
            return lines.skip(1)
                    .map(parser::parse)
                    .toList();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }

        return List.of();
    }
}
