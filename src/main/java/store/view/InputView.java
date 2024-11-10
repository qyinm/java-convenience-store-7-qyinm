package store.view;

import static camp.nextstep.edu.missionutils.Console.readLine;

import java.math.BigDecimal;
import java.util.List;
import java.util.function.Function;
import java.util.regex.Pattern;
import store.dtos.ItemInputDto;

public class InputView {
    private static final Pattern USER_ITEM_INPUT_DELIMITER_PATTERN = Pattern.compile(",");
    private static final Pattern ITEM_SPLIT_PATTERN = Pattern.compile("-");
    
    public static List<ItemInputDto> getUserItemInput() {
        OutputView.printUserItemInputPrefix();

        String userInput = readLine();
        return USER_ITEM_INPUT_DELIMITER_PATTERN.splitAsStream(userInput)
                .map(getItemInputDto())
                .toList();
    }

    private static Function<String, ItemInputDto> getItemInputDto() {
        return item -> {
            String[] split = ITEM_SPLIT_PATTERN.split(item);
            return new ItemInputDto(split[0], new BigDecimal(split[1]));
        };
    }
}
