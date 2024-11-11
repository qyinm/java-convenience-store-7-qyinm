package store.view;

import static camp.nextstep.edu.missionutils.Console.readLine;
import static store.constants.view.InputValidatePatternConstants.VALID_INPUT_ASK_ANSWER;
import static store.constants.view.InputValidatePatternConstants.VALID_INPUT_ITEM_PATTERN;
import static store.exception.input.InputErrorCode.INVALID_INPUT_FORM;

import java.math.BigDecimal;
import java.util.List;
import java.util.function.Function;
import java.util.regex.Pattern;
import store.dtos.ItemInputDto;
import store.exception.StoreException;

public class InputView {
    private static final Pattern USER_ITEM_INPUT_DELIMITER_PATTERN = Pattern.compile(",");
    private static final Pattern ITEM_SPLIT_PATTERN = Pattern.compile("-");
    private static final String YES = "Y";
    private static final String NO = "N";

    public static List<ItemInputDto> getUserItemInput() {
        OutputView.printUserItemInputPrefix();
        String userInput = readLine();
        validateInputItemFrom(userInput);

        return USER_ITEM_INPUT_DELIMITER_PATTERN.splitAsStream(userInput)
                .map(getItemInputDto())
                .toList();
    }

    private static void validateInputItemFrom(String userInput) {
        if (!VALID_INPUT_ITEM_PATTERN.isValid(userInput)) {
            throw new StoreException(INVALID_INPUT_FORM);
        }
    }

    // 아이템 문자열을 ItemInputDto로 변환하는 함수
    private static Function<String, ItemInputDto> getItemInputDto() {
        return item -> {
            item = item.replaceAll("[\\[|\\]]", "");
            String[] split = ITEM_SPLIT_PATTERN.split(item);
            return new ItemInputDto(split[0], new BigDecimal(split[1]));
        };
    }

    public static boolean askUserGetMoreBonusProduct(String itemName, BigDecimal bonusPrice) {
        OutputView.printAskGetMoreBonusProducts(itemName, bonusPrice);

        return askYesOrNo();
    }

    private static void validateYesOrNoInputForm(String userInput) {
        if (!VALID_INPUT_ASK_ANSWER.isValid(userInput)) {
            throw new StoreException(INVALID_INPUT_FORM);
        }
    }

    public static boolean askFullPricePurchase(String itemName, BigDecimal quantity) {
        OutputView.printAskFullPricePurchasePrefix(itemName, quantity);

        return askYesOrNo();
    }

    public static boolean askGetMembershipDiscount() {
        return askYesOrNo();
    }

    private static boolean askYesOrNo() {
        String userInput = readLine();
        validateYesOrNoInputForm(userInput);

        return YES.equals(userInput);
    }
}
