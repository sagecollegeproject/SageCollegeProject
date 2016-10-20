/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SageCollegeProject;

import sagex.api.*;

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
}
