/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package SageCollegeProject;

import sagex.phoenix.vfs.IMediaResource;
import sagex.phoenix.vfs.groups.IGrouper;

/**
 *
 * @author SageCollegeProject
 */
public class NowFutureAiringGrouper implements IGrouper {

    @Override
    public String getGroupName(IMediaResource imr) {
    return GetAiringNowGroup(phoenix.media.GetSageMediaFile(imr));
    }

    private String GetAiringNowGroup(Object Airing)
    {
    Long currTime=sagex.api.Utility.Time();
     if((sagex.api.AiringAPI.GetAiringStartTime(Airing) < currTime && (sagex.api.AiringAPI.GetAiringEndTime(Airing) > currTime)) || (sagex.api.AiringAPI.GetMediaFileForAiring(Airing)!= null))
             {
             return "Now Showing";
             }    
     else
     {
         return "Playing Soon";
     }
    }
    
}
