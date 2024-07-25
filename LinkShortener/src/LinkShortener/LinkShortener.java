package LinkShortener;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.Scanner;

public class LinkShortener {
    private static final String CHARACTERS = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    private static final int SHORT_URL_LENGTH = 6;
    private Map<String, String> urlMap;
    private Map<String, String> shortUrlMap;
    private Random random;

    public LinkShortener() {
        urlMap = new HashMap<>();
        shortUrlMap = new HashMap<>();
        random = new Random();
    }

    public String shortenUrl(String longUrl) {
        if (urlMap.containsKey(longUrl)) {
            return urlMap.get(longUrl);
        }

        String shortUrl;
        do {
            shortUrl = generateShortUrl();
        } while (shortUrlMap.containsKey(shortUrl));

        urlMap.put(longUrl, shortUrl);
        shortUrlMap.put(shortUrl, longUrl);

        return shortUrl;
    }

    public String expandUrl(String shortUrl) {
        return shortUrlMap.getOrDefault(shortUrl, "URL not found");
    }

    private String generateShortUrl() {
        StringBuilder shortUrl = new StringBuilder(SHORT_URL_LENGTH);
        for (int i = 0; i < SHORT_URL_LENGTH; i++) {
            shortUrl.append(CHARACTERS.charAt(random.nextInt(CHARACTERS.length())));
        }
        return shortUrl.toString();
    }

    public static void main(String[] args) {
        LinkShortener linkShortener = new LinkShortener();
        Scanner scanner = new Scanner(System.in);
        
        System.out.println("Welcome to the Link Shortener!");
        while (true) {
            System.out.println("Choose an option:");
            System.out.println("1. Shorten a URL");
            System.out.println("2. Expand a URL");
            System.out.println("3. Exit");

            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume the newline

            switch (choice) {
                case 1:
                    System.out.println("Enter the long URL:");
                    String longUrl = scanner.nextLine();
                    String shortUrl = linkShortener.shortenUrl(longUrl);
                    System.out.println("Shortened URL: " + shortUrl);
                    break;
                case 2:
                    System.out.println("Enter the short URL:");
                    String shortUrlToExpand = scanner.nextLine();
                    String expandedUrl = linkShortener.expandUrl(shortUrlToExpand);
                    System.out.println("Expanded URL: " + expandedUrl);
                    break;
                case 3:
                    System.out.println("Exiting...");
                    scanner.close();
                    return;
                default:
                    System.out.println("Invalid choice. Please choose again.");
            }
        }
    }
}
