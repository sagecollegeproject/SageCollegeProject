/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package SageCollegeProject;

import java.io.Serializable;
import java.util.Comparator;
import java.util.List;
import sagex.api.AiringAPI;
import sagex.api.ChannelAPI;
import sagex.phoenix.vfs.IMediaFile;
import sagex.phoenix.vfs.IMediaFolder;
import sagex.phoenix.vfs.IMediaResource;

/**
 *
 * @author SageCollegeProject
 */
public class RecentlyWatchedSorter implements Comparator<IMediaResource>, Serializable {
      
    private static final long serialVersionUID = 1L;


    @Override
    public int compare(IMediaResource o1, IMediaResource o2) {
   if(o1==null)
   {
   return 1;
   }
   if(o2==null)
   {
   return -1;    
   }
   
   Long watchT1 =getMostRecentWatchedendTime(o1);
   Long watchT2 = getMostRecentWatchedendTime(o2);
   
   return watchT1.compareTo(watchT2);
    }
     private long getMostRecentWatchedendTime(IMediaResource o) {
        if (o instanceof IMediaFile) {
            return AiringAPI.GetRealWatchedEndTime(phoenix.media.GetSageMediaFile(o));
        }

        if (o instanceof IMediaFolder) {
            return searchFolderForRecentTime((IMediaFolder) o);
        }

        // If it's not a File or Folder just return a 0
        return 0;
}
      private long searchFolderForRecentTime(final IMediaFolder folder) {

        List<IMediaResource> children = folder.getChildren();
        long returnValue = 0;

        for (IMediaResource r : children) {

            long candidate = getMostRecentWatchedendTime(r);
            if (candidate > returnValue) {
                // this item is more recent than the previously saved item
                returnValue = candidate;
            }
        }
        return returnValue;
}
    

}
