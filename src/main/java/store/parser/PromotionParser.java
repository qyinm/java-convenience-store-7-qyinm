package store.parser;

import static store.constants.parser.ParserConstants.PARSER_DELIMITER;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import store.domain.Promotion;

public class PromotionParser {
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public static Promotion parse(String line) {
        String[] values = PARSER_DELIMITER.getPattern().split(line);
        String name = values[0];
        BigDecimal buyAmount = new BigDecimal(values[1]);
        BigDecimal getAmount = new BigDecimal(values[2]);
        LocalDate startDate = LocalDate.parse(values[3], DATE_FORMATTER);
        LocalDate endDate = LocalDate.parse(values[4], DATE_FORMATTER);

        return new Promotion(name, buyAmount, getAmount, startDate, endDate);
    }
}
