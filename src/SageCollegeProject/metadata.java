/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SageCollegeProject;

import java.util.ArrayList;
import java.util.List;
import sagex.api.*;
import sagex.phoenix.vfs.IMediaFolder;
import sagex.phoenix.vfs.IMediaResource;

/**
 *
 * @author SageCollegeProject
 */
public class metadata {

    public static String GetEpisodeNumPad(Object sageObj) {
        String epNum = Integer.toString(ShowAPI.GetShowEpisodeNumber(sageObj));
        if (epNum.length() < 2) {
            epNum = "0" + epNum;
        }
        return epNum;
    }

    public static String GetSeasonNumPad(Object sageObj) {
        String seasNum = Integer.toString(ShowAPI.GetShowSeasonNumber(sageObj));
        if (seasNum.length() < 2) {
            seasNum = "0" + seasNum;
        }
        return seasNum;
    }

    public static Object getSetMissingSeries(Object sObj) {
        Object rSeries = ShowAPI.GetShowSeriesInfo(sObj);
        if (rSeries == null) {
            System.out.println("Setting seriesinfo as it wasn't set.");
            String cTitle = phoenix.metadata.GetMediaTitle(sObj);
            for (Object series : sagex.api.SeriesInfoAPI.GetAllSeriesInfo()) {
                String sTitle = sagex.api.SeriesInfoAPI.GetSeriesTitle(series);
                if (sTitle.equals(cTitle)) {

                    System.out.println("Series Matched For title: " + cTitle + " mediaID: " + sagex.api.MediaFileAPI.GetMediaFileID(sObj));

                    rSeries = series;
                    break;
                }
            }
        }
        return rSeries;
    }

    public static int GetSeasonElementForFocus(Object episode, IMediaFolder show) {
        int loc = 0;
        Object currEpisode = phoenix.media.GetSageMediaFile(episode);
        String seasonNum = GetSeasonNumPad(currEpisode);
        if (!isSingleSeason(show)) {
            for (int i = 0; i < phoenix.umb.GetChildCount(show); i++) {
                IMediaResource currSeason = phoenix.umb.GetChild(show, i);
                String curName = phoenix.media.GetTitle(currSeason);
                if (curName.endsWith(seasonNum)) {
                    loc = i;
                    break;
                }
            }
        }
        return loc;
    }

    public static Boolean isSingleSeason(IMediaFolder show) {
        return phoenix.umb.GetChildCount(show) == 1 || phoenix.umb.IsFolder(phoenix.umb.GetChild(show, 0)) == false;
    }
    
    public static String ScrapLeadingArticles(String title)
    {
        java.lang.Character[] test={'a','b'};
        String lTitle=title.toLowerCase();
    if(lTitle.startsWith("the "))
    {
    return title.substring(4);
    }
    else if (lTitle.startsWith("a "))
    {
    return title.substring(2);
    }
    else if(lTitle.startsWith("an "))
    {
    return title.substring(3);
    }
    else
    {
    return title;
    }
    }
}
