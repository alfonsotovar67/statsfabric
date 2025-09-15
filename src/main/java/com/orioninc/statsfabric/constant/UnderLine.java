package com.orioninc.statsfabric.constant;

public enum UnderLine {
    NONE((byte) 0),
    SINGLE((byte) 1),
    DOUBLE((byte) 2),
    SINGLE_ACCOUNTING((byte) 33),
    DOUBLE_ACCOUNTING((byte) 34);

    private final byte value;

    UnderLine(byte value) {
        this.value = value;
    }

    public static UnderLine fromValue(byte value) {
        for (UnderLine underline : values()) {
            if (underline.value == value) {
                return underline;
            }
        }
        throw new IllegalArgumentException("Invalid underline value: " + value);
    }

    public static int fromString(String value) {
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid underline string value: " + value, e);
        }
    }

    public byte getValue() {
        return value;
    }
}
