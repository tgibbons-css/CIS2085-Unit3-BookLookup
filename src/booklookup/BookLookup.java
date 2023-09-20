package booklookup;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Scanner;

public class BookLookup
{
    public static void main(String[] args)
    {
        // Set up Scanner and welcome user.
        Scanner input = new Scanner(System.in);
        System.out.println("Welcome, this program will help you look up information about a book.");
        
        // Get user's ISBN number.
        System.out.print("What is the ISBN number of the book you would like information about? ");
        long isbn = input.nextLong();
        
        // Look up the title and description.
        String results = retrieveBookData(isbn);
        String title = parseTitle(results);
        String description = parseDescription(results);
        
        // Output the results.
        System.out.println("The title is: " + title);
        System.out.println("Description: " + description);
    }
    
    public static String parseTitle(String json)
    {
        String title = "Not found";
        int startIndex = json.lastIndexOf("\"title\": ");
        
        if (startIndex > -1)
        {
            int endIndex = json.indexOf(", \"", startIndex);

            if (startIndex >= 0 && endIndex >= 0)
            {
                title = json.substring(startIndex + 10, endIndex - 1);
            }
        }
        
        return title;
    }
    
    public static String parseDescription(String json)
    {
        String description = "Not found";
        int descriptionIndex = json.indexOf("description");
        
        if (descriptionIndex > -1)
        {   
            int startIndex = json.indexOf("value\": ", descriptionIndex);
            int endIndex = json.indexOf("}, \"");

            if (startIndex >= 0 && endIndex >= 0)
            {
                description = json.substring(startIndex + 9, endIndex - 1);
            }
        }
        
        return description;
    }
    
    public static String retrieveBookData(long isbn)
    {
        String url = "https://openlibrary.org/isbn/" + isbn + ".json";
        String data = "";
        String line;
        
        try (BufferedReader inFile = new BufferedReader(new InputStreamReader(new URL(url).openStream())))
        {
            line = inFile.readLine();
            
            while (line != null) {
                data += line + "\n";
                line = inFile.readLine();
            }
        }
        catch (IOException ioException)
        {
            data += ioException;
        }
        
        return data;
    }
}
