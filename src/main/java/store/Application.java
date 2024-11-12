package store;

import camp.nextstep.edu.missionutils.Console;
import java.util.Map;
import java.util.Set;
import store.constants.parser.FileConstants;
import store.controller.StoreController;
import store.domain.Product;
import store.domain.Promotion;
import store.parser.MarkdownParser;
import store.repository.StoreRepository;
import store.service.StoreService;

public class Application {
    public static void main(String[] args) {
        StoreRepository storeRepository = new StoreRepository(getProductSet());
        StoreService storeService = new StoreService(storeRepository);
        StoreController storeController = new StoreController(storeService);

        storeController.runStore();

        Console.close();
    }

    private static Set<Product> getProductSet() {
        Map<String, Promotion> promotionMap = MarkdownParser.parsePromotions(
                FileConstants.PROMOTIONS_FILE_PATH.getFilePath());
        return MarkdownParser.parseProducts(FileConstants.PRODUCTIONS_FILE_PATH.getFilePath(),
                promotionMap);
    }
}
