import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

            // Sets up the regular expressions
            String regexServers = "^(\\d+) (\\d+)$";
            String regexText = "(\\d+) (.+)";
            Pattern patternServers = Pattern.compile(regexServers);
            Pattern patternText = Pattern.compile(regexText);
            Matcher m;

            // Reads the number of servers and their capacity
            line = reader.readLine();
            m = patternServers.matcher(line);
            m.matches();
            servers = new Server[Integer.parseInt(m.group(1))];
            for (int i = 0; i < servers.length; i++) {
                servers[i] = new Server(Integer.parseInt(m.group(2)));
            }

            // Reads the number of texts
            texts = new String[Integer.parseInt(reader.readLine())];

            // Reads the texts
            for (int n = 0; n < texts.length; n++) {
                line = reader.readLine();
                m = patternText.matcher(line);
                m.matches();
                texts[n] = m.group(2);
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
     * @return Hash of x
     */
    private static int getHash(String x, int T) {
        return 7919 * getChecksum(x) % T;
    }

    /**
     * Generates the double hash of a string.
     * @param x String that will have it's hash generated
     * @param T Size of the destination array
     * @return Double hash of x
     */
    private static int getDoubleHash(String x, int T) {
        return (104729 * getChecksum(x) + 123) % T;
    }

    public static void main(String[] args) {
        try {
            readFile(args[0]);

            // TODO: test server capacity attribution
            
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}