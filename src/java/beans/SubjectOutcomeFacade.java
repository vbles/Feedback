/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package beans;

import entities.Subject;
import entities.SubjectOutcome;
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
public class SubjectOutcomeFacade extends AbstractFacade<SubjectOutcome> {
    @PersistenceContext(unitName = "FeedbackJPAPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public SubjectOutcomeFacade() {
        super(SubjectOutcome.class);
    }
    public List<SubjectOutcome> getByIdSubject(Subject subject) {
        Query q = em.createNamedQuery("SubjectOutcome.findByIdSubject");
        q.setParameter("idSubject", subject);
        return q.getResultList();

    }
    
}
