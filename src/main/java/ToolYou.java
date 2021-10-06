import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Base64;
import java.util.Random;

public class ToolYou {

    public static String filePathToStringBytes_BASE64(String filename) throws IOException {
        return Base64.getEncoder().encodeToString(Files.readAllBytes(Paths.get(filename)));
    }

    public static String fileRename(String fileName, String textToAdd) {
        int dot = fileName.lastIndexOf('.');
        return fileName.substring(0, dot) + textToAdd + fileName.substring(dot);
    }

    private static char[] generatePassword(int length) {
        String capitalCaseLetters = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        String lowerCaseLetters = "abcdefghijklmnopqrstuvwxyz";
        String lowerCaseCyrillic = "àáâãäåæçèêëìíîïğñòóôõö÷øùúûüışÿ";
        String capitalCaseCyrillic = "ÀÁÂÃÄÅÆÇÈÊËÌÍÎÏĞÑÒÓÔÕÖ×ØÙÚÛÜİŞß";
        String specialCharacters = "!@#$%^&*()-+=";
        String numbers = "1234567890";
        String combinedChars = capitalCaseLetters + lowerCaseLetters + specialCharacters + numbers;

        Random random = new Random();
        char[] password = new char[length];

        password[0] = lowerCaseLetters.charAt(random.nextInt(lowerCaseLetters.length()));
        password[1] = capitalCaseLetters.charAt(random.nextInt(capitalCaseLetters.length()));
        password[2] = specialCharacters.charAt(random.nextInt(specialCharacters.length()));
        password[3] = lowerCaseCyrillic.charAt(random.nextInt(specialCharacters.length()));
        password[4] = capitalCaseCyrillic.charAt(random.nextInt(specialCharacters.length()));
        password[5] = numbers.charAt(random.nextInt(numbers.length()));

        for(int i = 6; i < length ; i++)
            password[i] = combinedChars.charAt(random.nextInt(combinedChars.length()));

        return password;
    }
}
