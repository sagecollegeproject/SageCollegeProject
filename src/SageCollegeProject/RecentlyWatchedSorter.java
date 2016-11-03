/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package SageCollegeProject;

import java.io.Serializable;
import java.util.Comparator;
import sagex.api.AiringAPI;
import sagex.api.ChannelAPI;
import sagex.phoenix.vfs.IMediaResource;

/**
 *
 * @author SageCollegeProject
 */
public class RecentlyWatchedSorter implements Comparator<IMediaResource>, Serializable {
      
    private static final long serialVersionUID = 1L;


    @Override
    public int compare(IMediaResource o1, IMediaResource o2) {
   Long watchT1 = AiringAPI.GetRealWatchedEndTime(phoenix.media.GetSageMediaFile(o1));
   Long watchT2 = AiringAPI.GetRealWatchedEndTime(phoenix.media.GetSageMediaFile(02));
   if(watchT2.equals(0)||watchT2.equals(0))
   {
   return -1;
   }
   return watchT1.compareTo(watchT2);
    }

}
