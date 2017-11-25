import org.json.simple.JsonArray;
import org.json.simple.JsonObject;
import org.json.simple.Jsoner;

import java.io.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import static java.lang.System.*;

/**
 * Created by todd.elwood on 11/21/2017.
 */
public class ReadParseOutput {

    public static void main(String[] args) throws IOException {

        //Exit if no agruments passed
        if(args.length == 0) {
            System.out.println("Proper Usage is: java inputfile.txt outputfile.txt");
            System.exit(0);
        }

        //Assign agruments
        String inputfile=args[0];
        String outputfile=args[1];

        //Verify inputfile size is not empty
        if (getFileSize(inputfile)<0) {
            System.out.println("Source input filesize is empty: ");
            System.exit(0);
        }

        List<String> rowsInputFile = new ArrayList<String>();
        rowsInputFile=retrieveAllRows(inputfile);

        ArrayList<Integer> errorList = new ArrayList<>();
        ArrayList<ParseString> validRowList = new ArrayList<ParseString>();


        //evaluateRows=evaluateRows(rowsInputFile);

        //errorList =
        //validRowList=

        for (int i = 0; i < rowsInputFile.size(); i++) {

            String pattern = "\\d{3} \\d{3} \\d{4}";
            String inputString = rowsInputFile.get(i);
            Pattern r = Pattern.compile(pattern);
            Matcher m = r.matcher(inputString);

            if (m.find()) {  //Match
                validRowList.add(new ParseString(rowsInputFile.get(i), true));
                out.println(inputString);
            } else { //No Match
                out.println(inputString);
                int actualline = i + 1;
                errorList.add(actualline);
            }
        }

        sortJsonArray(validRowList);

        JsonObject json = new JsonObject();
        loadValidDataJson(json,validRowList);
        loadErrorDataJson(json,errorList);

        createJsonFile(json);

    }

    public static List<String> retrieveAllRows(String file) throws IOException {

        List<String> rowsInputFile = new ArrayList<String>();
        BufferedReader reader = new BufferedReader(new FileReader(file));
        String line;

        int count = 1;
        try {
            while ((line = reader.readLine()) != null) {
                rowsInputFile.add(line);
                out.println(count + ". " + line);
                count++;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        reader.close();
        System.out.println("Total rows evaluated " + rowsInputFile.size());
        return rowsInputFile;

    }

    public static void loadErrorDataJson(JsonObject json,ArrayList<Integer> errList) {
    //Add error array data to JSON output

        JsonArray errlist = new JsonArray(Arrays.asList(errList));
        json.put("errors",errlist);
    }

    public static void loadValidDataJson(JsonObject json,ArrayList<ParseString> rowList) {
    //Add valid row array data to JSON output

        JsonArray custarr = new JsonArray();

        for (int n = 0; n < rowList.size(); n++) {

            JsonObject cust = new JsonObject();
            cust.put("color",rowList.get(n).color);
            cust.put("firstname",rowList.get(n).firstname);
            cust.put("lastname",rowList.get(n).lastname);
            cust.put("phonenumber",rowList.get(n).phonenumber);
            cust.put("zipcode",rowList.get(n).zipcode);
            custarr.add(cust);
            cust.remove(n);
        }
        json.put("entries",custarr);
    }

    public static void sortJsonArray(ArrayList<ParseString> rowList) {
    //Sort by LastName

        Collections.sort(rowList, new Comparator<ParseString>() {
            @Override
            public int compare(ParseString list2, ParseString list1) {
                String s1 = list2.lastname;
                String s2 = list1.lastname;
                return s1.compareToIgnoreCase(s2);
            }
        });
    }

    public static void createJsonFile(JsonObject json) throws IOException {
    //Create JSON content file with 2 space indentation

        FileWriter file = new FileWriter("outputfile", false);
        try {
            file.write(Jsoner.prettyPrint(json.toJson(),2));
            file.flush();
        } catch (IOException e) {
            out.println("Error " + e);
        }
    }

    public static long getFileSize(String filename) {

        File file = new File(filename);
        if (!file.exists() || !file.isFile()) {
            System.out.println("File does not exist");
            return -1;
        }
        return file.length();
    }

}