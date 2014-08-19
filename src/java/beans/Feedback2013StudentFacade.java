/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package beans;

import entities.Feedback2013Student;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author Ashish Mathew
 */
@Stateless
public class Feedback2013StudentFacade extends AbstractFacade<Feedback2013Student> {
    @PersistenceContext(unitName = "FeedbackJPAPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public Feedback2013StudentFacade() {
        super(Feedback2013Student.class);
    }
    
    public List<Feedback2013Student> getLoggedInUser(){
        Query q = em.createNamedQuery("Feedback2013Student.findLoggedIn");
        return q.getResultList();
    }
}
