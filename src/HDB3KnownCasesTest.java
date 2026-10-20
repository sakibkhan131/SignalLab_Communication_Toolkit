
import java.util.Arrays;
import encoding.AMI;
import scrambling.HDB3;
import scrambling.HDB3Descrambler;

public class HDB3KnownCasesTest {
    public static void main(String[] args) {
        String[] cases = {
                "10000",
                "110000",
                "1100000001",
                "1010000100001",
                "11000001000011000001"
        };

        HDB3 encoder = new HDB3();
        HDB3Descrambler decoder = new HDB3Descrambler();
        AMI amiEncoder = new AMI();

        for (String bits : cases) {
            int[] original = amiEncoder.encode(bits);
            int[] scrambled = encoder.scramble(original);
            int[] restored = decoder.descramble(scrambled);

            System.out.println("\nInput: " + bits);
            System.out.println("Scrambled: "
                    + Arrays.toString(scrambled));
            System.out.println("Round trip: "
                    + (Arrays.equals(original, restored)
                            ? "PASS"
                            : "FAIL"));
        }
    }
}
