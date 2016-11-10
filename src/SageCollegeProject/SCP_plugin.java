/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package SageCollegeProject;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Map;
import sage.SageTVPlugin;
import javax.swing.Timer;



/**
 *
 * @author SageCollegeProject
 */
public class SCP_plugin implements SageTVPlugin {
    
    private Timer timer;
    private Integer timeBetween=1000*60*60*24;

    public SCP_plugin(sage.SageTVPluginRegistry arg)
    {
    
    }
    @Override
    public void start() {
      System.out.println("Background loading of GuideBox data started for SageCollegeProject");
      System.out.println("Go ahead and set next files");
      nextEpisodes.CreateAllViewsForEpisodeNext();
      timer= new Timer(timeBetween,new ActionListener(){
    public void actionPerformed(ActionEvent e) {
    System.out.println("Timer up lets reupdate GuideBoxData");
    guideBox.UpdateAllShowJsonFile();
    guideBox.updateURLMap();
    }});
      Long lastUpdate=java.lang.Long.getLong(sagex.api.Configuration.GetServerProperty(guideBox.guideboxUpdateProp,"100"));
      if(lastUpdate == null)
      {
      lastUpdate=java.lang.Long.parseLong("100");
      }
      Long currTime=System.currentTimeMillis();
      Long delayTime=(currTime-lastUpdate>timeBetween.longValue())||lastUpdate==100?1000:timeBetween-(currTime-lastUpdate);
      System.out.println("SCP GuideBox delay until next update ="+delayTime);
      timer.setInitialDelay(delayTime.intValue());
      timer.start();
    }

    @Override
    public void stop() {
        timer.stop();
    }

    @Override
    public void destroy() {
    timer.stop();
    }

    @Override
    public String[] getConfigSettings() {
    return null;
    }

    @Override
    public String getConfigValue(String string) {
    return null;
    }

    @Override
    public String[] getConfigValues(String string) {
    return null;
    }

    @Override
    public int getConfigType(String string) {
    return 0;
    }

    @Override
    public void setConfigValue(String string, String string1) {
    }

    @Override
    public void setConfigValues(String string, String[] strings) {
    }

    @Override
    public String[] getConfigOptions(String string) {
    return null;
    }

    @Override
    public String getConfigHelpText(String string) {
    return null;
     }

    @Override
    public String getConfigLabel(String string) {
    return null;
    }

    @Override
    public void resetConfig() {
    }

    @Override
    public void sageEvent(String string, Map map) {
    }

   

}
