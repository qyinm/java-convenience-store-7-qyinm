package store.utils.exception;

import java.util.function.Supplier;
import store.exception.StoreException;

public class RetryExceptionHandler {
    public static <T> T runUntilNoneLottoException(Supplier<T> task) {
        while (true) {
            try {
                return task.get();
            } catch (StoreException exception) {
                System.out.println(exception.getMessage());
            }
        }
    }
}
