package data;
import java.util.Random;

public class DigitalDataGenerator {
    
    private final Random random;
    public DigitalDataGenerator(){
        random = new Random();
    }
    public String generate(int length, DataType type) {
        if (length <= 0) {
            throw new IllegalArgumentException("Data length must be positive.");
        }
        if (type == null) {
            throw new IllegalArgumentException("Data type cannot be null.");
        }
        switch (type) {
            case RANDOM:
                return generateRandomData(length);
            case FOUR_ZEROS:
                return generateDataWithFixedZeros(length, 4);
            case EIGHT_ZEROS:
                return generateDataWithFixedZeros(length, 8);
            default:
                throw new IllegalArgumentException("Invalid data type: " + type);
        }
    }

    public String generateRandomData(int length) {
        if (length <= 0) {
            throw new IllegalArgumentException("Data length must be positive.");
        }
        StringBuilder data = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            data.append(random.nextInt(2));
        }
        return data.toString();
    }

    public String generateDataWithFixedZeros(int length, int zeroCount) {
        if (length <= 0) {
            throw new IllegalArgumentException("Data length must be positive.");
        }
        if (zeroCount <= 0) {
            throw new IllegalArgumentException("Zero count must be positive.");
        }
        if (zeroCount > length) {
            throw new IllegalArgumentException("Zero sequence cannot be longer than data length.");
        }
        StringBuilder data = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            data.append(random.nextInt(2));
        }
        int start = random.nextInt(length - zeroCount + 1);
        for (int i = start; i < start + zeroCount; i++) {
            data.setCharAt(i, '0');
        }
        return data.toString();
    }
}
