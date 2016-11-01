/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package SageCollegeProject;

import java.util.Map;
import sage.SageTVEventListener;
import sage.SageTVPluginRegistry;



/**
 *
 * @author SageCollegeProject
 */
public class SCP_plugin implements SageTVPluginRegistry {

    public void start()
    {
     System.out.println("We are running");
      for(int i=0;i<100;i++)
      {
      System.out.println("We are running");
      }
    }

    @Override
    public void eventSubscribe(SageTVEventListener sl, String string) {
    }

    @Override
    public void eventUnsubscribe(SageTVEventListener sl, String string) {
    }

    @Override
    public void postEvent(String string, Map map) {
    }

    @Override
    public void postEvent(String string, Map map, boolean bln) {
    }
    
    

   

}
