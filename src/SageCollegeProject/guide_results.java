/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package SageCollegeProject;

import java.util.ArrayList;

/**
 *
 * @author SageCollegeProject
 */
public class guide_results {
    
    private ArrayList<guide_ShowObject> results;
    private String total_results;

    public String getTotal_results() {
        return total_results;
    }

    public void setTotal_results(String total_results) {
        this.total_results = total_results;
    }
    
    public ArrayList<guide_ShowObject> getResults() {
        return results;
    }

    public void setResults(ArrayList<guide_ShowObject> results) {
        this.results = results;
    }
   
    
}
