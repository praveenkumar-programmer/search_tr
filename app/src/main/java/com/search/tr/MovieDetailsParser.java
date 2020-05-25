package com.search.tr;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MovieDetailsParser {

    private String year, languages, quality, size;
    private int sizeInMB = 0;

    private String yearRegex = "(\\(\\d{4}\\))|(\\[\\D{4}\\])";
    private String MBRegex = "\\d+\\s?MB";
    private String GBRegex = "\\d+\\.?\\d*\\s?GB";


    public MovieDetailsParser(String movieName){

        year = reSolveRegex(yearRegex, movieName);
        year = reSolveRegex("\\d{4}", year);

        if(!reSolveRegex(MBRegex, movieName).equals("")){
            size = reSolveRegex(MBRegex, movieName);
            sizeInMB = Integer.parseInt(reSolveRegex("\\d+", size));
        }
        else if(!reSolveRegex(GBRegex, movieName).equals("")){
            size = reSolveRegex(GBRegex, movieName);
            sizeInMB = (int) ((Float.parseFloat(reSolveRegex("\\d+\\.?\\d*", size))) *1024 );
        }

        if(movieName.contains("1080p") || movieName.contains("1080P"))
            quality = "1080P";
        else if(movieName.contains("720p") || movieName.contains("720P"))
            quality = "720P";
        else if(sizeInMB >= 700)
            quality = "HD";
        else
            quality = "SD";

        if(!reSolveRegex("TAMIL", movieName).equals(""))
            languages = addLanguage(languages, "TAMIL");
        if(!reSolveRegex("ENGLISH", movieName).equals(""))
            languages = addLanguage(languages, "ENGLISH");
        if(!reSolveRegex("TELUN?GU", movieName).equals(""))
            languages = addLanguage(languages, "TELUGU");
        if(!reSolveRegex("MALAI?YA+LAM", movieName).equals(""))
            languages = addLanguage(languages, "MALAYALAM");
        if(!reSolveRegex("KAN+ADA+M?", movieName).equals(""))
            languages = addLanguage(languages, "KANNADA");
        if(!reSolveRegex("HINDH?I", movieName).equals(""))
            languages = addLanguage(languages, "HINDI");


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



    public int getSizeInMB() {
        return sizeInMB;
    }

    public String getSize() {
        if(size.equals(""))
            return "0MB";
        return size;
    }

    public String getQuality() {
        return quality;
    }

    public String getLanguages() {
        if(languages.equals(""))
            return "NO INFO";
        return languages;
    }

    public String getYear() {
        if(year.equals(""))
            return "NO INFO";
        return year;
    }
}
