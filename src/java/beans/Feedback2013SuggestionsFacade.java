/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package beans;

import entities.Feedback2013Suggestions;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author Ashish Mathew
 */
@Stateless
public class Feedback2013SuggestionsFacade extends AbstractFacade<Feedback2013Suggestions> {
    @PersistenceContext(unitName = "FeedbackJPAPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public Feedback2013SuggestionsFacade() {
        super(Feedback2013Suggestions.class);
    }
    
}
