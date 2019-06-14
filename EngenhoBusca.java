import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;

class Text {
    public String content;
    public Text next;

    public Text(String content) {
        this.content = content;
        next = null;
    }
}

class Server {
    public Text texts = new Text("");
    public int capacity;
    public boolean isFull;

    public Server(int capacity) {
        this.capacity = capacity;
    }

    /**
     * Adds text to the server if there's avaiable space
     * @param newText Text to be added
     * @return {@code true} if the Text was added, {@code false} otherwise
     */
    public boolean addText(Text newText) {
        if (isFull)
            return false;

        // Adds to the end of {@link #texts}
        Text currentNode = texts;
        int capacityUsed = 1;
        while (currentNode.next != null) {
            currentNode = currentNode.next;
            capacityUsed++;
        }
        currentNode.next = new Text(newText.content);

        if (capacityUsed >= capacity)
            isFull = true;

        return true;
    }

    public StringBuilder getTexts() {
        Text currentNode = texts;
        StringBuilder result = new StringBuilder();

        // Prints the first {@link Text}
        if (currentNode.next != null) {
            currentNode = currentNode.next;
            result.append(currentNode.content);
        }

        // Prints the rest of the {@link Text}
        while (currentNode.next != null) {
            currentNode = currentNode.next;
            result.append(", " + currentNode.content);
        }

        return result;
    }
}

public class EngenhoBusca {

    private static Server[] servers;
    private static Text texts = new Text("");

    /**
     * Recursively adds a node to the end of a list.
     * @param head Head of the list
     * @param currentNode Node currently being used by the method
     * @param newNode Node to be added at the end of the list
     * @return Head of the list with the added node
     */
    private static Text addToEndOfList(Text head, Text currentNode, Text newNode) {
        
        if (currentNode.next == null) {
            currentNode.next = newNode;
        } else {
            addToEndOfList(head, currentNode.next, newNode);
        }
        return head;
    }

    /**
     * Reads a file and returns it's content.
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
            int numberOfTexts = Integer.parseInt(reader.readLine());

            // Reads the texts
            for (int n = 0; n < numberOfTexts; n++) {
                line = reader.readLine();
                int indexOfFirstSpace = line.indexOf(" ");
                String content = line.substring(++indexOfFirstSpace);
                texts = addToEndOfList(texts, texts, new Text(content));
            }
        }
    }

    /**
     * Writes content to file.
     * @param fileName Name of the file (with extension) to be writen
     * @param content Content to be writen on the file
     * @throws FileNotFoundException
     */
    private static void writeToFile(String fileName, StringBuilder content) throws FileNotFoundException {

        // Opens file
        try (PrintWriter out = new PrintWriter(fileName)) {
            // Writes content to file
            out.print(content.substring(0, content.length() - 1));
        }
    }

    /**
     * Generates the checksum of a string.
     * @param value String that will have it's checksum generated
     * @return Checksum of the string
     */
    private static int getChecksum(String value) {
        int total = 0;
        for (int i = 0; i < value.length(); i++) {
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

    /**
     * Stores Text into {@link #servers}.
     * @param head Head of {@link #texts}
     */
    private static StringBuilder storeText(Text head) {
        StringBuilder result = new StringBuilder();
        int hash = 0;
        Text currentNode = head.next;
        while (currentNode != null) {

            // Applies the double hash until the value is stored
            boolean stored = false;
            int tries = 0;
            int primaryServer = 0;  // Result of the first try
            while (!stored) {
                hash = getHash(currentNode.content, servers.length, tries++);

                if (hash > -1 && hash < servers.length)
                    stored = servers[hash].addText(currentNode);

                if (tries == 1)
                    primaryServer = hash;

                // Prevents an infinite loop
                if (tries > 10000000)
                    throw new RuntimeException("Number of double hash tries exceeded");
            }

            // Prints the Server in which the Text was stored,
            // and all of it's previous Text
            if (tries == 1) {
                result.append("[S" + hash + "] ");
                result.append(servers[hash].getTexts() + "\n");
            } else {
                result.append("S" + primaryServer + "->S" + hash + "\n");
                result.append("[S" + hash + "] ");
                result.append(servers[hash].getTexts() + "\n");
            }

            currentNode = currentNode.next;
        }

        return result;
    }

    public static void main(String[] args) {
        try {
            readFile(args[0]);

            writeToFile(args[1], storeText(texts));
            
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}