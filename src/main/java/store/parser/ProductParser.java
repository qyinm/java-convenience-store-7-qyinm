package store.parser;

import static store.constants.parser.ParserConstants.PARSER_DELIMITER;

import java.math.BigDecimal;
import java.util.Map;
import store.domain.Product;
import store.domain.Promotion;

public class ProductParser {
    public static Product parse(String line, Map<String, Promotion> promotionMap) {
        String[] values = PARSER_DELIMITER.getPattern().split(line);
        String name = values[0];
        BigDecimal price = new BigDecimal(values[1]);
        BigDecimal quantity = new BigDecimal(values[2]);
        Promotion promotion = promotionMap.getOrDefault(values[3], null);

        return new Product(name, price, quantity, promotion);
    }
}
