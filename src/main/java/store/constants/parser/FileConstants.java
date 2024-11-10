package store.constants.parser;

public enum FileConstants {
    PROMOTIONS_FILE_PATH("src/main/resources/promotions.md"),
    PRODUCTIONS_FILE_PATH("src/main/resources/products.md");

    private final String filePath;

    FileConstants(String filePath) {
        this.filePath = filePath;
    }

    public String getFilePath() {
        return filePath;
    }
}
