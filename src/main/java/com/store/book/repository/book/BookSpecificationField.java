package com.store.book.repository.book;

public enum BookSpecificationField {
    TITLE("title"),
    AUTHOR("author"),
    ISBN("isbn");

    private final String key;
    BookSpecificationField(String key) {
        this.key = key;
    }

    public String getKey() {
        return key;
    }

    public static BookSpecificationField fromKey(String key) {
        for (BookSpecificationField field : values()) {
            if (field.key.equalsIgnoreCase(key)) {
                return field;
            }
        }
        throw new IllegalArgumentException("Unknown specification key: " + key);
    }
}
