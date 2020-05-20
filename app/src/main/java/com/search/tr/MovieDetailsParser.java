package com.search.tr;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MovieDetailsParser {

    private String year, languages, quality, size;
    private int sizeMBInInt = 0;
    private float sizeGBInFloat = 0;

    private String yearRegex = "\\(\\d{4}\\)";
    private String MBRegex = "\\d*MB";
    private String GBRegex = "\\d*.?\\d*GB";


    public MovieDetailsParser(String movieName){

        year = reSolveRegex(yearRegex, movieName);
        year = movieName.substring(movieName.indexOf('(') + 1, movieName.indexOf(')'));

        if(!reSolveRegex(MBRegex, movieName).equals("")){
            size = reSolveRegex(MBRegex, movieName);
            sizeMBInInt = Integer.parseInt(size.substring(0, size.length()-2));
        }
        else if(!reSolveRegex(GBRegex, movieName).equals("")){
            size = reSolveRegex(GBRegex, movieName);
            sizeGBInFloat = Float.parseFloat(size.substring(0, size.length()-2));
        }

        if(movieName.contains("1080p") || movieName.contains("1080P"))
            quality = "1080P";
        else if(movieName.contains("720p") || movieName.contains("720P"))
            quality = "720P";
        else if(sizeGBInFloat > 1 | sizeMBInInt >= 700)
            quality = "HD";
        else if(sizeMBInInt < 700)
            quality = "SD";

        if(!reSolveRegex("TAMIL", movieName).equals(""))
            languages = addLanguage(languages, "TAMIL");
        else if(!reSolveRegex("ENGLISH", movieName).equals(""))
            languages = addLanguage(languages, "ENGLISH");
        else if(!reSolveRegex("TELUN?GU", movieName).equals(""))
            languages = addLanguage(languages, "TELUGU");
        else if(!reSolveRegex("MALAI?YA+LAM", movieName).equals(""))
            languages = addLanguage(languages, "MALAYALAM");
        else if(!reSolveRegex("KAN+ADA+M?", movieName).equals(""))
            languages = addLanguage(languages, "KANNADA");


    }

    private String reSolveRegex(String regex, String movieName){

        Pattern pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(movieName);
        if (matcher.find())
            return matcher.group(0);
        else
            return  "";
    }

    private String addLanguage(String languages, String language){

        if(languages == null)
            languages = language;
        else
            languages = languages + ", " + language;

        return languages;
    }



    public int getSizeMBInInt() {
        return sizeMBInInt;
    }

    public float getSizeGBInFloat() {
        return sizeGBInFloat;
    }

    public String getSize() {
        return size;
    }

    public String getQuality() {
        return quality;
    }

    public String getLanguages() {
        return languages;
    }

    public String getYear() {
        return year;
    }
}
