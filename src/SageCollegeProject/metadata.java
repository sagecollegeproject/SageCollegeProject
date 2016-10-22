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
    public static Object GetNextShowToWatch(IMediaFolder show)
    {
    return getNextShow(SortShowsByWatched(show));
    }
    
    public static int GetSeasonElementForFocus(Object episode, IMediaFolder show)
    {
        int loc=0;
        Object currEpisode=phoenix.media.GetSageMediaFile(episode);
        String seasonNum=GetSeasonNumPad(currEpisode);
        if(!isSingleSeason(show))
        {
        for(int i=0;i<phoenix.umb.GetChildCount(show);i++)
        {
         IMediaResource currSeason= phoenix.umb.GetChild(show,i); 
         String curName=phoenix.media.GetTitle(currSeason);
         if(curName.endsWith(seasonNum))
         {
         loc=i;
         break;
         }
        }
        }
    return loc;
    }
    
    public static Boolean isSingleSeason(IMediaFolder show)
    {
    return phoenix.umb.GetChildCount(show)==1;
    }
    
    public static Object[] SortShowsByWatched(IMediaFolder show)
    {
        List<IMediaResource> Episodes= new ArrayList<>();
        if(isSingleSeason(show))
        {
         IMediaResource showsChild= phoenix.umb.GetChild(show,0);
         Episodes.addAll(phoenix.media.GetChildren(showsChild));
        }
        else
        {
        for(int i=0;i<phoenix.umb.GetChildCount(show);i++)
        {
            IMediaResource cChild=phoenix.umb.GetChild(show, i);
            Episodes.addAll(phoenix.media.GetChildren(cChild));
            
        }
        }
         return (Object[]) sagex.api.Database.SortLexical(Episodes, true,"SageCollegeProject_metadata_getSortedByWatchTime");
    }
    
    public static Object getNextShow(Object[] episodes)
    {
      Object currEpisode= episodes[0];
      Object sageObj=phoenix.media.GetSageMediaFile(currEpisode);
      if(sagex.api.AiringAPI.IsWatchedCompletely(sageObj))
      {
      Object[] episodesResorted=(Object[]) sagex.api.Database.SortLexical(episodes,false,"SageCollegeProject_metadata_getSortedByAiringTime");
      int elementloc=sagex.api.Utility.FindElementIndex(episodesResorted,currEpisode);
      return episodesResorted[elementloc+1];
      }
      else
      {
      return currEpisode;
      }
    }
    
    public static long getSortedByWatchTime(IMediaResource imr)
    {
    return sagex.api.AiringAPI.GetRealWatchedEndTime(imr.getMediaObject());
    }
    public static long getSortedByAiringTime(IMediaResource imr)
    {
    return sagex.api.ShowAPI.GetOriginalAiringDate(imr.getMediaObject());
    }
}
