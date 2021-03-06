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
public class ChannelGrouper implements IGrouper {

    @Override
    public String getGroupName(IMediaResource imr) {
    return sagex.api.ChannelAPI.GetChannelNumber(sagex.api.AiringAPI.GetChannel(imr.getMediaObject()));
    }

}
