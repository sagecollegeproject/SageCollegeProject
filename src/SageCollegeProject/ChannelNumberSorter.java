package SageCollegeProject;

import sagex.phoenix.vfs.IMediaFile;
import sagex.phoenix.vfs.IMediaResource;
import java.io.Serializable;
import java.util.Comparator;
import sagex.api.*;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author SageCollegeProject
 */
public class ChannelNumberSorter implements Comparator<IMediaResource>, Serializable {

    private static final long serialVersionUID = 1L;

    @Override
    public int compare(IMediaResource o1, IMediaResource o2) {
        String chan1 = ChannelAPI.GetChannelNumber(AiringAPI.GetChannel(phoenix.media.GetSageMediaFile(o1)));
        String chan2 = ChannelAPI.GetChannelNumber(AiringAPI.GetChannel(phoenix.media.GetSageMediaFile(o2)));
        if (chan1 == null || chan2 == null) {
            return -1;
        }
        Double dChan1 = Double.parseDouble(addDecimal(chan1));
        Double dChan2 = Double.parseDouble(addDecimal(chan2));
        return dChan1.compareTo(dChan2);
    }

    private String addDecimal(String channel) {
        if (channel.contains("-")) {
            return channel.replace("-", ".");
        } else {
            return channel;
        }
    }

}
