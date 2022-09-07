package com.store.restapi.pincode;

import java.util.Random;

public class PinCodeGenerator {
    private PinCodeGenerator() {
        throw new IllegalStateException("PinCode Generator Utility class");
    }
    public static int getPinCode() {
        return 100_000 + new Random().nextInt(900_000);
    }
}
