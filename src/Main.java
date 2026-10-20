
import java.util.Arrays;
import java.util.Scanner;

import data.DataType;
import data.DigitalDataGenerator;
import data.PalindromeFinder;

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

import modulation.PCMEncoder;
import modulation.PCMDecoder;
import modulation.DeltaModulator;
import modulation.DeltaDemodulator;

import scrambling.Scrambler;
import scrambling.B8ZS;
import scrambling.B8ZSDescrambler;
import scrambling.HDB3;
import scrambling.HDB3Descrambler;

import visualization.SignalPlotter;

public class Main {

    public static void main(String[] args) {
        try (Scanner scanner = new Scanner(System.in)) {
            boolean running = true;

            while (running) {
                System.out.println("\n==============================================");
                System.out.println("   DIGITAL SIGNAL GENERATOR & ANALYZER");
                System.out.println("==============================================");

                // 1. Select input type
                System.out.println("\nSelect input type:");
                System.out.println("1. Digital bitstream");
                System.out.println("2. Analog samples (PCM / Delta Modulation)");
                int inputChoice = readInt(scanner, "Choice (1-2): ", 1, 2);

                String bits = "";
                double[] reconstructedSamples = null;

                // 2. Generate or enter digital data, or convert analog samples
                if (inputChoice == 1) {
                    System.out.println("\nSelect digital data source:");
                    System.out.println("1. Completely random");
                    System.out.println("2. Random with four consecutive zeros");
                    System.out.println("3. Random with eight consecutive zeros");
                    System.out.println("4. Enter custom binary bitstream");
                    int dataChoice = readInt(scanner, "Choice (1-4): ", 1, 4);

                    if (dataChoice == 4) {
                        bits = readBitstream(scanner, "Enter binary bitstream (e.g. 1011000000001): ");
                    } else {
                        DataType dataType = (dataChoice == 1) ? DataType.RANDOM
                                : (dataChoice == 2) ? DataType.FOUR_ZEROS
                                : DataType.EIGHT_ZEROS;

                        int length = readInt(scanner, "Enter bit sequence length (minimum 8): ", 8, 10000);
                        DigitalDataGenerator generator = new DigitalDataGenerator();
                        bits = generator.generate(length, dataType);
                    }

                } else {
                    System.out.println("\nSelect analog modulation scheme:");
                    System.out.println("1. Pulse Code Modulation (PCM)");
                    System.out.println("2. Delta Modulation (DM)");
                    int modulationChoice = readInt(scanner, "Choice (1-2): ", 1, 2);

                    int sampleCount = readInt(scanner, "Enter number of analog samples (>= 1): ", 1, 1000);
                    double[] analogSamples = new double[sampleCount];

                    System.out.println("Enter sample values in range [-1.0, 1.0]:");
                    for (int i = 0; i < sampleCount; i++) {
                        analogSamples[i] = readDouble(scanner, String.format("  Sample [%d]: ", i), -1.0, 1.0);
                    }

                    if (modulationChoice == 1) {
                        int bitsPerSample = readInt(scanner, "Enter PCM bits per sample (1-16): ", 1, 16);
                        PCMEncoder pcmEncoder = new PCMEncoder(bitsPerSample);
                        PCMDecoder pcmDecoder = new PCMDecoder(bitsPerSample);

                        bits = pcmEncoder.encode(analogSamples);
                        reconstructedSamples = pcmDecoder.decode(bits);

                        System.out.println("\n========== PCM MODULATION ==========");
                        System.out.println("Bits per sample:      " + pcmEncoder.getBitsPerSample());
                        System.out.println("Quantization levels:  " + pcmEncoder.getLevels());
                        System.out.println("Digital bitstream:    " + bits);
                        System.out.println("Reconstructed samples:" + Arrays.toString(reconstructedSamples));

                        SignalPlotter.plotPCM("PCM Reconstructed Signal", reconstructedSamples);

                    } else {
                        double stepSize = readDouble(scanner, "Enter DM step size delta (0.01 - 1.0, e.g. 0.2): ", 0.0001, 1.0);
                        DeltaModulator dmEncoder = new DeltaModulator(stepSize);
                        DeltaDemodulator dmDecoder = new DeltaDemodulator(stepSize);

                        bits = dmEncoder.encode(analogSamples);
                        reconstructedSamples = dmDecoder.decode(bits);

                        System.out.println("\n===== DELTA MODULATION =====");
                        System.out.println("Step size (delta):    " + stepSize);
                        System.out.println("Digital bitstream:    " + bits);
                        System.out.println("Reconstructed samples:" + Arrays.toString(reconstructedSamples));

                        SignalPlotter.plotPCM("Delta Modulation Reconstructed Signal", reconstructedSamples);
                    }
                }

                // 3. Display digital bitstream and longest palindrome
                System.out.println("\n==============================================");
                System.out.println("   DIGITAL BITSTREAM & PALINDROME ANALYSIS");
                System.out.println("==============================================");
                System.out.println("Bitstream:       " + bits);
                System.out.println("Bit count (N):   " + bits.length());

                String palindrome = PalindromeFinder.findLongestPalindrome(bits);
                System.out.println("Longest palindrome: " + palindrome);
                System.out.println("Palindrome length:  " + palindrome.length());

                // 4. Select line-coding scheme
                System.out.println("\nSelect line-coding scheme:");
                System.out.println("1. NRZ-L (Non-Return to Zero Level)");
                System.out.println("2. NRZ-I (Non-Return to Zero Invert on 1)");
                System.out.println("3. Manchester (Biphase)");
                System.out.println("4. Differential Manchester");
                System.out.println("5. AMI (Alternate Mark Inversion)");
                int schemeChoice = readInt(scanner, "Choice (1-5): ", 1, 5);

                LineEncoder encoder;
                LineDecoder decoder;
                String schemeName;

                switch (schemeChoice) {
                    case 1:
                        encoder = new NRZL();
                        decoder = new NRZLDecoder();
                        schemeName = "NRZ-L";
                        break;
                    case 2:
                        encoder = new NRZI();
                        decoder = new NRZIDecoder();
                        schemeName = "NRZ-I";
                        break;
                    case 3:
                        encoder = new Manchester();
                        decoder = new ManchesterDecoder();
                        schemeName = "Manchester";
                        break;
                    case 4:
                        encoder = new DifferentialManchester();
                        decoder = new DifferentialManchesterDecoder();
                        schemeName = "Differential Manchester";
                        break;
                    case 5:
                    default:
                        encoder = new AMI();
                        decoder = new AMIDecoder();
                        schemeName = "AMI";
                        break;
                }

                // 5. Encode the bitstream
                int[] encodedSignal = encoder.encode(bits);
                int[] transmittedSignal = encodedSignal.clone();

                boolean scramblingApplied = false;
                String selectedScheme = schemeName;

                // Scrambling is available with AMI
                if (schemeChoice == 5) {
                    System.out.println("\nApply scrambling (to eliminate long zero runs)?");
                    System.out.println("1. No scrambling");
                    System.out.println("2. B8ZS (Bipolar with 8-Zero Substitution)");
                    System.out.println("3. HDB3 (High-Density Bipolar 3-Zero)");
                    int scrambleChoice = readInt(scanner, "Choice (1-3): ", 1, 3);

                    if (scrambleChoice == 2) {
                        Scrambler scrambler = new B8ZS();
                        transmittedSignal = scrambler.scramble(encodedSignal);
                        selectedScheme = "AMI + B8ZS";
                        scramblingApplied = true;
                    } else if (scrambleChoice == 3) {
                        Scrambler scrambler = new HDB3();
                        transmittedSignal = scrambler.scramble(encodedSignal);
                        selectedScheme = "AMI + HDB3";
                        scramblingApplied = true;
                    }
                }

                // 6. Display transmitted signal and metrics
                System.out.println("\n==============================================");
                System.out.println("   TRANSMISSION & SIGNAL METRICS");
                System.out.println("==============================================");
                System.out.println("Scheme:              " + selectedScheme);
                System.out.println("Transmitted signal:  " + Arrays.toString(transmittedSignal));
                System.out.println("Signal element count:" + transmittedSignal.length);

                // Telecommunication metrics calculation
                displaySignalMetrics(bits, transmittedSignal, selectedScheme);

                // 7. Descramble if scrambling was applied
                int[] signalForDecoding = transmittedSignal.clone();
                if (scramblingApplied) {
                    if (selectedScheme.equals("AMI + B8ZS")) {
                        B8ZSDescrambler descrambler = new B8ZSDescrambler();
                        signalForDecoding = descrambler.descramble(signalForDecoding);
                    } else {
                        HDB3Descrambler descrambler = new HDB3Descrambler();
                        signalForDecoding = descrambler.descramble(signalForDecoding);
                    }
                }

                // 8. Recover bitstream and verify round-trip
                String decodedBits = decoder.decode(signalForDecoding);
                System.out.println("\n==============================================");
                System.out.println("   RECEIVER DECODING & VERIFICATION");
                System.out.println("==============================================");
                System.out.println("Decoded bitstream:   " + decodedBits);
                boolean passed = bits.equals(decodedBits);
                System.out.println("Round-trip status:   " + (passed ? "PASS (Exact match)" : "FAIL (Mismatch)"));

                // 9. Plot the transmitted signal
                SignalPlotter.plotDigital(selectedScheme + " Signal", transmittedSignal);

                // 10. Re-run prompt
                System.out.println("\n----------------------------------------------");
                System.out.println("Select next action:");
                System.out.println("1. Analyze another signal");
                System.out.println("2. Exit");
                int nextAction = readInt(scanner, "Choice (1-2): ", 1, 2);
                if (nextAction == 2) {
                    running = false;
                    System.out.println("\nExiting Digital Signal Generator. Thank you!");
                }
            }

        } catch (Exception e) {
            System.err.println("\nUnexpected application error: " + e.getMessage());
        }
    }

    private static void displaySignalMetrics(String bits, int[] signal, String scheme) {
        int n = bits.length();
        int m = signal.length;

        // Baud rate / Signal rate factor
        double r = (double) n / m; // data elements per signal element
        System.out.printf("Signal elements per bit: %.2f (r = %.2f)%n", (double) m / n, r);
        System.out.printf("Baud Rate:               %.2f * BitRate (N)%n", (double) m / n);

        // DC Component calculation
        double sum = 0;
        for (int level : signal) {
            sum += level;
        }
        double dcComponent = sum / m;
        System.out.printf("DC Component (avg level): %.4f V %s%n",
                dcComponent, Math.abs(dcComponent) < 1e-6 ? "(DC Balanced / Zero DC)" : "(Non-zero DC)");

        // Transition count (clock synchronization metric)
        int transitions = 0;
        for (int i = 0; i < m - 1; i++) {
            if (signal[i] != signal[i + 1]) {
                transitions++;
            }
        }
        double transitionDensity = (double) transitions / m;
        System.out.printf("Signal Transitions:      %d / %d intervals (density: %.2f)%n",
                transitions, m, transitionDensity);

        // Max consecutive zeros run in bits vs transmitted
        int maxZeroData = maxZeroRun(bits);
        System.out.println("Max zero run in data:    " + maxZeroData + " zeros");
    }

    private static int maxZeroRun(String s) {
        int max = 0;
        int curr = 0;
        for (char c : s.toCharArray()) {
            if (c == '0') {
                curr++;
                max = Math.max(max, curr);
            } else {
                curr = 0;
            }
        }
        return max;
    }

    private static int readInt(Scanner scanner, String prompt, int min, int max) {
        while (true) {
            System.out.print(prompt);
            String line = scanner.nextLine().trim();
            if (line.isEmpty()) {
                continue;
            }
            try {
                int val = Integer.parseInt(line);
                if (val >= min && val <= max) {
                    return val;
                }
                System.out.printf("Please enter an integer between %d and %d.%n", min, max);
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a valid number.");
            }
        }
    }

    private static double readDouble(Scanner scanner, String prompt, double min, double max) {
        while (true) {
            System.out.print(prompt);
            String line = scanner.nextLine().trim();
            if (line.isEmpty()) {
                continue;
            }
            try {
                double val = Double.parseDouble(line);
                if (Double.isFinite(val) && val >= min && val <= max) {
                    return val;
                }
                System.out.printf("Please enter a finite number between %.4f and %.4f.%n", min, max);
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a valid decimal number.");
            }
        }
    }

    private static String readBitstream(Scanner scanner, String prompt) {
        while (true) {
            System.out.print(prompt);
            String line = scanner.nextLine().trim();
            if (line.matches("[01]+")) {
                return line;
            }
            System.out.println("Invalid bitstream. Input must be non-empty and contain only '0' and '1'.");
        }
    }
}
