/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package beans;

import entities.Feedback2013User;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author Ashish Mathew
 */
@Stateless
public class Feedback2013UserFacade extends AbstractFacade<Feedback2013User> {
    @PersistenceContext(unitName = "FeedbackJPAPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public Feedback2013UserFacade() {
        super(Feedback2013User.class);
    }
    
}
