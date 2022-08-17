package com.store.restapi.pincode;

import java.util.Random;

public class PinCodeGenerator {
    public static int getPinCode() {
        return 100_000 + new Random().nextInt(999_999);
    }
}
