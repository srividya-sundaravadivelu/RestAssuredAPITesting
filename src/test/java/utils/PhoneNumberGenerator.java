package utils;

import java.util.Random;

public class PhoneNumberGenerator {
    public static String generatePhoneNumber() {
        Random random = new Random();
        long number = 1000000000L + random.nextLong(9000000000L); // Ensures 10-digit number
        return String.valueOf(number);
    }

   
}

