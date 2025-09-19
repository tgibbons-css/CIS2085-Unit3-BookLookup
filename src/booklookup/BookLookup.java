package booklookup;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Scanner;

/**
 *  Starting program for CIS 2085 Book Lookup activity
 *  Updated in 2025 to work with new API
 */
public class BookLookup {
    public static void main(String[] args)
    {
        // Set up Scanner and welcome user.
        Scanner input = new Scanner(System.in);
        System.out.println("Welcome, this program will help you look up information about a book.");
        System.out.println("Each book has an book number (ISBN) which uniquely identifies the book");
        System.out.println("ISBNs are either 10-digits like 1492072508 or 13-digits like 9781492072508");

        // Get user's ISBN number.
        System.out.print("Enter the ISBN number of the book you would like information about (do not enter the dash, only the digits)? ");
        long isbn = input.nextLong();

        // Look up the title and description.
        String results = retrieveBookData(isbn);
        String title = parseTitle(results);
        String date = parseDate(results);

        // Output the results.
        System.out.println("The title is: " + title);
        System.out.println("Published date: " + date);
    }


    // ====================================================================
    // Do not motify any code below this line
    // ====================================================================


    /**
     * Grab the title from the JSON file.
     * It should look like:  "title": "Think Java",
     * @param json
     * @return title as a string
     * TODO -- This should really use a JSON parsing library like GSON
     */
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

    /**
     * Grab the date from the JSON file.
     * It should look like:  "publish_date": "2019",
     * @param json
     * @return date as a string
     * TODO -- This should really use a JSON parsing library like GSON
     */
    public static String parseDate(String json)
    {
        String publishDate = "Not found";
        int dateIndex = json.indexOf("publish_date");

        if (dateIndex > -1)
        {
            int startIndex = json.indexOf(": \"", dateIndex);
            int endIndex = json.indexOf("\",", dateIndex);
            //System.out.println("start:"+startIndex+" end:"+endIndex);

            if (startIndex >= 0 && endIndex >= 0)
            {
                publishDate = json.substring(startIndex+3, endIndex);
            }
        }

        return publishDate;
    }

    /**
     * Grab the description from the JSON file.
     *    --- no longer used because the API does not provide the description
     * @param json
     * @return description as a string
     */
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

    /**
     * Retrieves the book data from the Open Library Books API and returns a JSON string
     * Updated to use: https://openlibrary.org/api/books?bibkeys=ISBN:{isbn}&format=json&jscmd=data
     * @param isbn as a long int (no dashes)
     * @return JSON as a string
     */
    public static String retrieveBookData(long isbn)
    {
        // UPDATED: use the Books API with bibkeys and jscmd=data
        String url = "https://openlibrary.org/api/books?bibkeys=ISBN:" + isbn + "&format=json&jscmd=data";
        String data = "";
        String line;

        try (BufferedReader inFile = new BufferedReader(new InputStreamReader(new URL(url).openStream())))
        {
            line = inFile.readLine();
            //System.out.println("   FILE: "+line);

            while (line != null) {
                data += line + "\n";
                line = inFile.readLine();
                //System.out.println("   FILE: "+line);
            }
        }
        catch (IOException ioException)
        {
            data += ioException;
        }

        return data;
    }
}

