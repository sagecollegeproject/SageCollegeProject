/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SageCollegeProject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import sagex.phoenix.Phoenix;
import sagex.phoenix.vfs.IMediaResource;
import sagex.phoenix.vfs.IMediaFolder;
import sagex.phoenix.vfs.views.ViewFolder;

/**
 *
 * @author SageCollegeProject
 */
public class nextEpisodes {

    public static ViewFolder lastWatched;
    public static ViewFolder sortedEpisodes;
    public static ViewFolder recentEpisodes;
    public static List<IMediaResource> newIMR = new ArrayList<IMediaResource>();
    public static List<IMediaResource> nextIMR = new ArrayList<IMediaResource>();

    public static void main(String[] args) {
        sage.Sage.TESTING = true;
        Phoenix.getInstance().initServices();
        long time = System.currentTimeMillis();
        CreateAllViewsForEpisodeNext();
        System.out.println("time to run " + (time - System.currentTimeMillis()));
        System.out.println(GetTopBarItems(3, 3).size());
        for (Object test : GetTopBarItems(3, 3)) {
            System.out.println(phoenix.metadata.GetEpisodeName(test));
        }

    }

    public static void CreateAllViewsForEpisodeNext() {  
        lastWatched = CreateView("sagecollegeproject.allTVRecentlyWatched");
        sortedEpisodes = CreateView("sagecollegeproject.allTVnoSeasons");
        recentEpisodes = CreateView("sagecollegeproject.RecentActivity");
    }

    public static ViewFolder CreateView(String View) {
        return phoenix.umb.CreateView(View);
    }

    public static IMediaResource GetNextEpisodeForTitle(String title) {
        IMediaResource cFolder = lastWatched.getChild(title);
        IMediaResource firstEp = phoenix.umb.GetChild((IMediaFolder) cFolder, 0);
        return CheckAndGetEpisode(firstEp);
    }

    public static List<IMediaResource> GetTopBarItems(int countNew, int countRecent) {

        newIMR.clear();
        nextIMR.clear();
        newIMR = GetNewestEpisodesToWatch(countNew);
        nextIMR = GetNextEpisodesToWatch(countRecent);
        List<IMediaResource> shuffled = new ArrayList<IMediaResource>();
        shuffled.addAll(newIMR);
        shuffled.addAll(nextIMR);
        Collections.shuffle(shuffled);
        return shuffled;
    }

    public static boolean isNew(IMediaResource imr) {
        return newIMR.contains(imr);
    }

    public static boolean isNext(IMediaResource imr) {
        return nextIMR.contains(imr);
    }

    public static IMediaResource GetEpisodeChild(IMediaResource imr) {
        return phoenix.umb.GetFolder(imr);
    }

    public static List<IMediaResource> GetNewestEpisodesToWatch(int num) {
        List<IMediaResource> results = new ArrayList<IMediaResource>();
        for (int count = 0; count < num; count++) {
            results.add(recentEpisodes.getChildren().get(count));
        }
        return results;
    }

    public static List<IMediaResource> GetNextEpisodesToWatch(int num) {
        List<IMediaResource> results = new ArrayList<IMediaResource>();
        for (int count = 0; count < num&&count<lastWatched.getChildren().size();) {
            IMediaResource cFolder = lastWatched.getChildren().get(count);
            IMediaResource firstEp = phoenix.umb.GetChild((IMediaFolder) cFolder, 0);
            IMediaResource nEpisode = CheckAndGetEpisode(firstEp);
            if (nEpisode != null) {
                results.add(CheckAndGetEpisode(firstEp));
                count++;
            } else {
                count++;
                num++;
                //We didn't find a next episode for this show so don't increase count
            }
        }

        return results;

    }

    public static IMediaResource CheckAndGetEpisode(IMediaResource firstEp) {
        if (sagex.api.AiringAPI.IsWatchedCompletely(phoenix.media.GetSageMediaFile(firstEp))) {
            List<IMediaResource> epSorted = phoenix.media.GetChildren(sortedEpisodes.getChild(firstEp.getTitle()));
            int element = epSorted.indexOf(firstEp);
            if (epSorted.size() > element+1) {
                return epSorted.get(element + 1);
            } else {
                //Return null for no next episode to watch show is done.   
                return null;
            }
        }
        return firstEp;

    }

}
