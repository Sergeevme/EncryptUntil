import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Base64;

public class CryptoYou {

    public static String filename = "Go.txt";
    public static String filenameDec = "GoBlowEnc.txt";

    // Driver code
    public static void main(String[] args) throws IOException {
        // Init key
        String key = "thisIsTheKey";
        String filename = "C:\\Users\\NIKITA SERGEEV\\IdeaProjects\\EncryptUntil\\files\\Cryptome.txt";

        // Crypt
        encryptFile(filename, key);
        //decryptFile(filenameDec, key);

        //encryptFile("mp4.mp4", key);
        //decryptFile("mp4BlowEnc.mp4", key);
    }

    public static void encryptFile(String pathToFile, String key) throws IOException {
        String openText = ToolYou.filePathToStringBytes_BASE64(pathToFile);

        Blowfish blowfish = new Blowfish(key);
        Files.write(Paths.get(ToolYou.fileRename(pathToFile, "BlowEnc")),
                Base64.getDecoder().decode(blowfish.encryptString(openText)));
    }

    public static void decryptFile(String pathToFile, String key) throws IOException {
        String closeText = ToolYou.filePathToStringBytes_BASE64(pathToFile);

        Blowfish blowfish = new Blowfish(key);
        Files.write(Paths.get(ToolYou.fileRename(pathToFile, "BlowDec")),
                Base64.getDecoder().decode(blowfish.decryptString(closeText)));
    }
}
