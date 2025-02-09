package utils;

import java.util.Random;

public class EmailGenerator {
    public static String generateEmail() {        
        String randomString = getRandomString(6); // Generates 3 random letters
        int randomNum = new Random().nextInt(90) + 10; // Generates a 2-digit number
        return randomString + randomNum + "@gmail.com";
    }

    private static String getRandomString(int length) {
        String chars = "abcdefghijklmnopqrstuvwxyz";
        StringBuilder sb = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < length; i++) {
            sb.append(chars.charAt(random.nextInt(chars.length())));
        }
        return sb.toString();
    }

   
}
