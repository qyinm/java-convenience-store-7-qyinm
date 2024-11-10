package store;

import java.util.Map;
import java.util.Set;
import store.constants.parser.FileConstants;
import store.domain.Product;
import store.domain.Promotion;
import store.parser.MarkdownParser;
import store.repository.StoreRepository;

public class Application {
    public static void main(String[] args) {
        Map<String, Promotion> promotionMap = MarkdownParser.parsePromotions(
                FileConstants.PROMOTIONS_FILE_PATH.getFilePath());
        Set<Product> products = MarkdownParser.parseProducts(FileConstants.PRODUCTIONS_FILE_PATH.getFilePath(),
                promotionMap);
        StoreRepository storeRepository = new StoreRepository(products);

    }
}
