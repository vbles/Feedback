/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package entities;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Ashish Mathew
 */
public class FQuestions {

    private Feedback2013Question q;
    private List<FacultySubject> fsList = new ArrayList<>();
    private Map<Integer,Short> ratings = new HashMap<>();

    public List<FacultySubject> getFsList() {
        return fsList;
    }

    public void setFsList(List<FacultySubject> fsList) {
        this.fsList = fsList;
    }

    public Feedback2013Question getQ() {
        return q;
    }

    public void setQ(Feedback2013Question q) {
        this.q = q;
    }

    public Map<Integer,Short> getRatings() {
        return ratings;
    }

    public void setRatings(Map<Integer,Short> ratings) {
        this.ratings = ratings;
    }
}
