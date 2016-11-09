/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SageCollegeProject;

/**
 *
 * @author SageCollegeProject
 */
public class fanart {

    private static String posterScale = "252";
    private static String bannerScale = "758";
    private static String backgroundScale = "1920";
    private static String thumb_backgroundScale = "240";


    public static Object GetScaledFanart(String Tag, String Path) {
        String Scale = "200";
        if (Tag.contains("Poster")) {
            Scale = posterScale;
        } else if (Tag.contains("Background")) {
            if (Tag.contains("thumb"))
                    {
                    Scale=thumb_backgroundScale;
                    }
            else{
            Scale = backgroundScale;}
        } else {
            Scale = bannerScale;
        }

        if (phoenix.image.GetImage(Path, Tag) == null || Path.length() < 0) {

            phoenix.image.CreateImage(Tag, Path, "[{name:scale, width:" + Scale + "}]", true);

        }
        return phoenix.image.GetImage(Path, Tag);

    }
    
    public static Object GetShowImage(String Title,Object SageObj)
    {
    if(guideBox.hasGuideBoxImage(Title)){return guideBox.getGuideBoxImage(Title);}
    else{return phoenix.fanart.GetDefaultEpisode(SageObj);}
    }
}
