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
    private static String backgroundScale = "720";

    public static Object GetScaledFanart(String Tag, String Path) {
        String Scale = "200";
        if (Tag.contains("Poster")) {
            Scale = posterScale;
        } else if (Tag.contains("Background")) {
            Scale = backgroundScale;
        } else {
            Scale = bannerScale;
        }

        if (phoenix.image.GetImage(Path, Tag) == null || Path.length() < 0) {

            phoenix.image.CreateImage(Tag, Path, "[{name:scale, width:" + Scale + "}]", true);

        }
        return phoenix.image.GetImage(Path, Tag);

    }
}
