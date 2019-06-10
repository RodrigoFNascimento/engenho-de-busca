public class EngenhoBusca {

    /**
     * Calculates the hash of a string.
     * @param value String to be converted
     * @return Hash of the string
     */
    private static int getHash(String value) {
        int total = 0;
        for (int i = 0; i < value.length(); i++){
            int asc = (int) value.charAt(i);
            total ^= asc;
        }
        return total;
    }

    public static void main(String[] args) {
        try {
            System.out.println(getHash("ufs"));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}