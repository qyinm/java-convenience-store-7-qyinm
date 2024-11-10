package store.view;

import static camp.nextstep.edu.missionutils.Console.readLine;
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

    public static List<ItemInputDto> getUserItemInput() {
        OutputView.printUserItemInputPrefix();
        String userInput = readLine();
        validateInputItemFrom(userInput);

        return USER_ITEM_INPUT_DELIMITER_PATTERN.splitAsStream(userInput)
                .map(getItemInputDto())
                .toList();
    }

    private static void validateInputItemFrom(String userInput) {
        if (VALID_INPUT_ITEM_PATTERN.isValid(userInput)) {
            throw new StoreException(INVALID_INPUT_FORM);
        }
    }

    private static Function<String, ItemInputDto> getItemInputDto() {
        return item -> {
            String[] split = ITEM_SPLIT_PATTERN.split(item);
            return new ItemInputDto(split[0], new BigDecimal(split[1]));
        };
    }
}
