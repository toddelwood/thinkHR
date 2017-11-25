import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by todd.elwood on 11/21/2017.
 */
public class ParseString {

    public String color;
    public String firstname;
    public String lastname;
    public String phonenumber;
    public String zipcode;

    public ParseString (String line, boolean bValidRow) {

        String[] split = line.split(",");
        System.out.println("Split length " + split.length);
        int max_columns=split.length;

        boolean bLastName = false;
        boolean bFirstName = false;
        boolean bColor = false;
        boolean bZipCode = false;
        boolean bPhoneNumber = false;

        for (int i=0;i<max_columns;i++) {

            System.out.println(split[i].trim());
            String currentAttr = split[i].trim();
            if (bValidRow) {

                if (StringUtils.containsAny("abcdefghijklmnopqrstuvwxyz", currentAttr)) {

                    if (max_columns == 5) {
                        if (i == 0) {
                            bFirstName = true;
                            this.firstname = currentAttr;
                        }
                        if (i == 1) {
                            bLastName = true;
                            lastname = currentAttr;
                        }
                    } else if (max_columns == 4) {
                        if ((!bFirstName) && (i == 0)) {
                            if (VerifyWhiteSpaceString(split[i].trim())) {
                                int totalLength = split[i].trim().length();
                                String fname = currentAttr.substring(0, RetrieveWhiteSpace(currentAttr));
                                String lname = currentAttr.substring(RetrieveWhiteSpace(currentAttr) + 1);
                                firstname = fname;
                                lastname = lname;
                                System.out.println("first and last name detected");
                                bFirstName = true;
                                bLastName = true;
                            } //if (VerifyWhiteSpaceString(split[i].trim())) {
                        } //if (!bFirstName) && (i==0) {
                    } //else if (max_columns == 4) {

                    if ((!bColor) && (bFirstName) && (bLastName)) {
                        if (VerifyColor(split[i].trim())) {
                            System.out.println("color detected");
                            color = split[i].trim();
                            bColor = true;
                        }
                    }
                } //if (StringUtils.containsAny("abcdefghijklmnopqrstuvwxyz", currentAttr)) {

                if (StringUtils.containsAny("0123456789", split[i].trim())) {
                    if (!bPhoneNumber) {
                        if (VerifyPhoneNumber(split[i].trim())) {
                            phonenumber = String.format("%s-%s-%s", currentAttr.substring(0, 3), currentAttr.substring(4, 7), currentAttr.substring(8, 12));
                            System.out.println("phone number detected");
                            bPhoneNumber = true;
                        }
                    } //if (!bPhoneNumber) {
                    if (!bZipCode) {
                        if (VerifyZipCode(split[i].trim())) {
                            zipcode = currentAttr;
                            System.out.println("zip code detected");
                            bZipCode = true;
                        }
                    } // if (!bZipCode) {
                } //if (StringUtils.containsAny("0123456789", split[i].trim())) {
            }
        } //for (int i=0;i<max_columns;i++)

    } //public ParseString (String line, boolean bValidRow) {

    public boolean VerifyColor(String evalColor) {
    //Verify if string is color

        boolean IsColor=false;
        try {
            List<String> color_name = new ArrayList<String>();
            color_name.add("red");
            color_name.add("yellow");
            color_name.add("purple");
            color_name.add("orange");
            color_name.add("blue");
            color_name.add("black");
            String color = evalColor.toLowerCase();

            Set<String> set = new HashSet<String>(color_name);
            if (set.contains(color)) {
                IsColor = true;
                System.out.println("Success: Found Color " + color );
            }
            return IsColor;
        } catch (Exception e) {
            System.out.println("Color Not Found Error: \n" + e +"\n");
        }
        return IsColor;
    }

    public boolean VerifyPhoneNumber (String evalPhoneNumber) {
    //Verify if string matches phone number pattern
        boolean IsPhoneNumber=false;
        String pattern = "\\d{3} \\d{3} \\d{4}";
        String inputString = evalPhoneNumber;
        Pattern r = Pattern.compile(pattern);
        Matcher m = r.matcher(inputString);

        if (m.find()) {
            System.out.println("Success: Found Phone Number\n");
            IsPhoneNumber=true;
        }
        return IsPhoneNumber;
    }


    public boolean VerifyZipCode (String evalZipCode) {
    //Verify if string matches zip code pattern

        boolean IsZipCode=false;
        String pattern = "\\d{5}";
        String inputString = evalZipCode;
        Pattern r = Pattern.compile(pattern);
        Matcher m = r.matcher(inputString);

        if (m.find()) {
            System.out.println("Success: Found\n");
            IsZipCode=true;
        }
        return IsZipCode;
    }


    public boolean VerifyWhiteSpaceString(String evalString) {
    //Verify if white space exists in string
        boolean whitespacefound=false;
        if (evalString.indexOf(" ") > 0) {
            whitespacefound=true;
        }
        return whitespacefound;
    }

    public int RetrieveWhiteSpace(String evalString) {
    //Return white space location in string
        return evalString.indexOf(" ");
    }

}
