package store.parser;

import java.io.IOException;
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
        return new HashSet<>(parse(filePath, (line) -> ProductParser.parse(filePath, promotionMap)));
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
