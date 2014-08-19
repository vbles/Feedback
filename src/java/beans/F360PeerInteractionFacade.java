/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package beans;

import entities.F360PeerInteraction;
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
public class F360PeerInteractionFacade extends AbstractFacade<F360PeerInteraction> {

    @PersistenceContext(unitName = "FeedbackJPAPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public F360PeerInteractionFacade() {
        super(F360PeerInteraction.class);
    }

    public F360PeerInteraction getByIdFac(Faculty idFaculty, F360User username) {
        Query q = em.createNamedQuery("F360PeerInteraction.findByUidIdFac");
        q.setParameter("idFaculty", idFaculty);
        q.setParameter("uid", username);
        if (q.getResultList().size() > 0) {
            return (F360PeerInteraction) q.getResultList().get(0);
        }
        return null;
    }
}
