/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package beans;

import entities.F360FeedbackPeer;
import entities.F360QuestionPeer;
import entities.F360User;
import entities.Faculty;
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
public class F360FeedbackPeerFacade extends AbstractFacade<F360FeedbackPeer> {
    @PersistenceContext(unitName = "FeedbackJPAPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public F360FeedbackPeerFacade() {
        super(F360FeedbackPeer.class);
    }

    public List getItemsUsernameFaculty(F360User f360User, F360QuestionPeer temp, Faculty idFaculty) {
        Query q = em.createNamedQuery("F360FeedbackPeer.findByUidQidIdFac");
        q.setParameter("uid", f360User);
        q.setParameter("qid", temp);
        q.setParameter("idFaculty", idFaculty);
        return q.getResultList();
    }
    
    public List getItemsUsernameFaculty(F360User f360User, Faculty idFaculty) {
        Query q = em.createNamedQuery("F360FeedbackPeer.findByUidIdFac");
        q.setParameter("uid", f360User);
        q.setParameter("idFaculty", idFaculty);
        return q.getResultList();
    }
    
}
