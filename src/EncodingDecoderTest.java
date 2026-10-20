
import encoding.LineEncoder;
import encoding.NRZL;
import encoding.NRZI;
import encoding.Manchester;
import encoding.DifferentialManchester;
import encoding.AMI;

import decoding.LineDecoder;
import decoding.NRZLDecoder;
import decoding.NRZIDecoder;
import decoding.ManchesterDecoder;
import decoding.DifferentialManchesterDecoder;
import decoding.AMIDecoder;

import java.util.Arrays;

public class EncodingDecoderTest {

    public static void main(String[] args) {

        String originalBits = "1011001010010110";

        LineEncoder[] encoders = {
                new NRZL(),
                new NRZI(),
                new Manchester(),
                new DifferentialManchester(),
                new AMI()
        };

        LineDecoder[] decoders = {
                new NRZLDecoder(),
                new NRZIDecoder(),
                new ManchesterDecoder(),
                new DifferentialManchesterDecoder(),
                new AMIDecoder()
        };

        String[] names = {
                "NRZ-L",
                "NRZ-I",
                "Manchester",
                "Differential Manchester",
                "AMI"
        };

        for (int i = 0; i < encoders.length; i++) {

            // Encode the original data.
            int[] signal = encoders[i].encode(originalBits);

            // Decode only the generated signal.
            String decodedBits = decoders[i].decode(signal);

            boolean correct = originalBits.equals(decodedBits);

            System.out.println("\nScheme: " + names[i]);
            System.out.println("Original: " + originalBits);
            System.out.println(
                    "Signal:   " + Arrays.toString(signal));
            System.out.println("Decoded:  " + decodedBits);
            System.out.println("Status:   " +
                    (correct ? "PASS" : "FAIL"));
        }
    }
}
