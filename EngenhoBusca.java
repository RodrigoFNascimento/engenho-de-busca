public class EngenhoBusca {

    /**
     * Calculates the checksum of a string.
     * @param value String to be converted
     * @return Checksum of the string
     */
    private static int getChecksum(String value) {
        int total = 0;
        for (int i = 0; i < value.length(); i++){
            int asc = (int) value.charAt(i);
            total ^= asc;
        }
        return total;
    }

    private static int getHash(String x, int T) {
        return 7919 * getChecksum(x) % T;
    }

    private static int getDoubleHash(String x, int T) {
        return 104729 * getChecksum(x) + 123 % T;
    }

    public static void main(String[] args) {
        try {
            System.out.println("checksum = " + getChecksum("ufs"));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}