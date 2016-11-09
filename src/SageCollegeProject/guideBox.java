/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SageCollegeProject;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author SageCollegeProject
 */
public class guideBox {

    public static String base_URL = "http://api-public.guidebox.com/v1.43/1/rKBCCjBUyk3HpNDywPozT2DIFZqLFkOk/";
    private static String AllShowsJsonFile = "userdata/SCP/GuideBox/AllShows.json";
    private static HashMap<String,String> URLMap=new HashMap<String,String>();
    public static String guideboxUpdateProp=menu.Props+"LastGuideBoxUpdate";

    public static void main(String[] args) throws MalformedURLException, IOException, URISyntaxException {
    Long t= System.currentTimeMillis();
        GetShowPoster("2097");
        System.out.println(System.currentTimeMillis()-t);
    }

    public static void UpdateAllShowJsonFile() {
        System.out.println("Getting ready to read all guidebox data for shows.");
        ArrayList<guide_ShowObject> res = new ArrayList<>();
        ReadAllShowsFromWeb(res);
        Gson gson = new GsonBuilder().create();
        String json = gson.toJson(res);
        System.out.println(res.size());
        WriteJsonToFile(json, AllShowsJsonFile);
        sagex.api.Configuration.SetServerProperty(guideboxUpdateProp,java.lang.String.valueOf(System.currentTimeMillis()));
    }

    public static String GetEncWebCall(String webcall) {
        try {
            URL url = new URL(webcall);
            URI test = new URI(url.getProtocol(), url.getUserInfo(), url.getHost(), url.getPort(), url.getPath(), url.getQuery(), url.getRef());
            return test.toASCIIString();
        } catch (URISyntaxException | MalformedURLException ex) {
            Logger.getLogger(guideBox.class.getName()).log(Level.SEVERE, null, ex);
        }
        return "";
    }

    public static void WriteJsonToFile(String json, String fileName) {
        CheckMakeDir(fileName.substring(0, fileName.lastIndexOf("/")));

        try {
            FileWriter writer = new FileWriter(fileName);
            writer.write(json);
            writer.close();
        } catch (IOException ex) {
            System.out.println("Error writing SCPGuideBox json file to disk");
            System.out.println(ex);
        }

    }

    public static void CheckMakeDir(String filepath) {
        File chkdir = new File(filepath);
        if (!chkdir.exists()) {
            chkdir.mkdirs();
        }
    }

    public static void ReadAllShowsFromWeb(ArrayList<guide_ShowObject> res) {
        int resultamount = 2;
        int pos = 1;
        while (pos < resultamount) {
            String out = GetURLReturn("shows/all/" + pos + "/250/all/all");
            Gson gson = new GsonBuilder().create();
            guide_results test = gson.fromJson(out, guide_results.class);
           // for(guide_ShowObject curShow:test.getResults())
           // {
           //     curShow.setPoster(GetShowPoster(curShow.getId()));
           //     System.out.println("Poster found +"+curShow.getPoster());
           // }
            System.out.println(test.getTotal_results() + "Current " + pos);
            resultamount = Integer.parseInt(test.getTotal_results());
            res.addAll(test.getResults());
            pos = pos + 250;
            System.out.println("SCP GuideBox_Reading all shows total of="+resultamount);
            System.out.println("SCP GuideBox_Current position of shows ="+pos);
        }
    }
    
    public static String GetShowPoster(String showID)
    {
    
    String out= GetURLReturn("show/"+showID+"/images/poster");
    JsonParser parser = new JsonParser();
   JsonObject obj = parser.parse(out).getAsJsonObject();
   JsonObject test=obj.get("results").getAsJsonObject();
   JsonArray posters=test.getAsJsonArray("posters");
   JsonObject lPosters=posters.get(0).getAsJsonObject();
   JsonObject medPoster=lPosters.getAsJsonObject("medium");
   return medPoster.get("url").getAsString();
    }

    public static String GetURLReturn(String Call) {
        String out = "";
        try {
            out = new Scanner(new URL(base_URL + Call).openStream(), "UTF-8").useDelimiter("\\A").next();
        } catch (MalformedURLException ex) {
            System.out.println("Error SCP guidebox malformed URL call " + Call);
            Logger.getLogger(guideBox.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            System.out.println("Error SCP guidebox IO exception on url call");
            Logger.getLogger(guideBox.class.getName()).log(Level.SEVERE, null, ex);
        }
        return out;
    }

    public static ArrayList<guide_ShowObject> ReadAllShowsfromFile() {
        BufferedReader br = ReadJsonFromFile(AllShowsJsonFile);
        Gson gson = new GsonBuilder().create();
        ArrayList<guide_ShowObject> test = gson.fromJson(br, new TypeToken<List<guide_ShowObject>>() {
        }.getType());
        return test;
    }

    public static BufferedReader ReadJsonFromFile(String fileName) {
        try {
            BufferedReader br = new BufferedReader(new FileReader(fileName));
            return br;
        } catch (FileNotFoundException ex) {
            System.out.println("File not found exception on reading from file.");
            Logger.getLogger(guideBox.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
    
    public static void updateURLMap()
    {
    clearURLMap();
    setAllTitleURLs();
    }

    public static void setAllTitleURLs() {
        HashMap<String, String> urlsMap = new HashMap<String, String>();
        for (guide_ShowObject cur : ReadAllShowsfromFile()) {
            urlsMap.put(cur.getTitle(), cur.getArtwork_208x117());
        }
        URLMap.putAll(urlsMap);
    }
    public static void clearURLMap()
    {
    URLMap.clear();
    }
    
    public static boolean hasGuideBoxImage(String title)
    {
    return URLMap.containsKey(title);
    }
    
    public static String getGuideBoxImage(String title)
    {
    return URLMap.get(title);
    }
    
   
}
