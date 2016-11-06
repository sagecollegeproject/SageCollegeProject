/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package SageCollegeProject;

import sagex.phoenix.factory.ConfigurableOption;
import sagex.phoenix.factory.ConfigurableOption.DataType;
import sagex.phoenix.factory.ConfigurableOption.ListSelection;
import sagex.phoenix.vfs.IMediaFile;
import sagex.phoenix.vfs.IMediaFolder;
import sagex.phoenix.vfs.IMediaResource;
import sagex.phoenix.vfs.filters.Filter;

/**
 *
 * @author SageCollegeProject
 */
public class WatchedCompletelyFilter extends Filter{

    public WatchedCompletelyFilter(boolean watchedCompletely)
    {
    super();
    addOption(new ConfigurableOption(OPT_VALUE, "WatchedCompletely", "true", DataType.string, true, ListSelection.single,
                "true:Yes,false:No"));
    setValue(String.valueOf(watchedCompletely));
    
    }
    
    public WatchedCompletelyFilter()
    {
    this(true);
    }
    
    @Override
    protected boolean canAccept(IMediaResource imr) {
       boolean watchedCompletely = getOption(OPT_VALUE).getBoolean(true);
        if (imr instanceof IMediaFolder)
            return true;
        if (imr instanceof IMediaFile) {
            return (sagex.api.AiringAPI.IsWatchedCompletely(phoenix.media.GetSageMediaFile(imr))) == watchedCompletely;
        } else {
            return false;
        }
}
    }


