import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;

class Server {
    public String[] texts;

    public Server(int capacity) {
        texts = new String[capacity];
    }
}

public class EngenhoBusca {

    public static Server[] servers;
    public static String[] texts;

    /**
     * Reads file and returns it's content
     * @param file File to be read
     * @return The content of the file
     * @throws IOException
     * @throws FileNotFoundException
     */
    private static void readFile(String file) throws IOException, FileNotFoundException {

        // Opens the file
        try (FileInputStream inputStream = new FileInputStream(file)) {
            
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            String line;

            // Reads the number of servers and their capacity
            line = reader.readLine();
            String[] serversLine = line.split(" ");
            int numberOfServers = Integer.parseInt(serversLine[0]);
            int serversCapacity = Integer.parseInt(serversLine[1]);
            servers = new Server[numberOfServers];
            for (int i = 0; i < servers.length; i++) {
                servers[i] = new Server(serversCapacity);
            }

            // Reads the number of texts
            texts = new String[Integer.parseInt(reader.readLine())];

            // Reads the texts
            for (int n = 0; n < texts.length; n++) {
                line = reader.readLine();
                int indexOfFirstSpace = line.indexOf(" ");
                texts[n] = line.substring(++indexOfFirstSpace);
            }
        }
    }

    /**
     * Writes content to file
     * @param fileName Name of the file (with extension) to be writen
     * @param content Content to be writen on the file
     * @throws FileNotFoundException
     */
    private static void writeToFile(String fileName, StringBuilder content) throws FileNotFoundException {

        // Opens file
        try (PrintWriter out = new PrintWriter(fileName)) {
            // Writes content to file
            out.print(content);
        }
    }

    /**
     * Generates the checksum of a string.
     * @param value String that will have it's checksum generated
     * @return Checksum of the string
     */
    private static int getChecksum(String value) {
        int total = 0;
        for (int i = 0; i < value.length(); i++){
            if (value.charAt(i) != ' ') {
                int asc = (int) value.charAt(i);
                total ^= asc;
            }
        }
        return total;
    }

    /**
     * Generates the hash of a string.
     * @param x String that will have it's hash generated
     * @param T Size of the destination array
     * @param i Number of tries
     * @return Hash of x
     */
    private static int getHash(String x, int T, int i) {
        int checksum = getChecksum(x);
        int h1 = 7919 * checksum;
        int h2 = 104729 * checksum + 123;
        return (h1 + i * h2) % T;
    }

    public static void main(String[] args) {
        try {
            readFile(args[0]);

            /*for (int i = 0; i < texts.length; i++) {
                System.out.println(texts[i]);
            }*/

            //System.out.println(getHash("cd ef", 3, 0));
            
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}