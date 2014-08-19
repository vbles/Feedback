/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package beans;

import entities.F360QuestionPeer;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author Ashish Mathew
 */
@Stateless
public class F360QuestionPeerFacade extends AbstractFacade<F360QuestionPeer> {
    @PersistenceContext(unitName = "FeedbackJPAPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public F360QuestionPeerFacade() {
        super(F360QuestionPeer.class);
    }
    
}
