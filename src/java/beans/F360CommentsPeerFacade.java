/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package beans;

import entities.F360CommentsPeer;
import entities.F360User;
import entities.Faculty;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author Ashish Mathew
 */
@Stateless
public class F360CommentsPeerFacade extends AbstractFacade<F360CommentsPeer> {
    @PersistenceContext(unitName = "FeedbackJPAPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public F360CommentsPeerFacade() {
        super(F360CommentsPeer.class);
    }
    
    public F360CommentsPeer getByUidIdFac(Faculty idFaculty, F360User username) {
        Query q = em.createNamedQuery("F360CommentsPeer.findByUidIdFac");
        q.setParameter("idFaculty", idFaculty);
        q.setParameter("uid", username);
                
        if (q.getResultList().size() > 0) {
           return (F360CommentsPeer) q.getResultList().get(0);
        }
        return null;
    }
}
