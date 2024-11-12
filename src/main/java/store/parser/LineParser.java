package store.parser;

@FunctionalInterface
public interface LineParser<T> {
    T parse(String line);
}
