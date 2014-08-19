/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package beans;

import entities.FacultySubject;
import entities.Feedback2013;
import entities.Feedback2013Question;
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
public class Feedback2013Facade extends AbstractFacade<Feedback2013> {
    @PersistenceContext(unitName = "FeedbackJPAPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public Feedback2013Facade() {
        super(Feedback2013.class);
    }

    public List<Feedback2013> findByUserId(Feedback2013Student uid, FacultySubject idFacultySubject, Feedback2013Question qid) {
        Query q = em.createNamedQuery("Feedback2013.findByUId");
        q.setParameter("uid", uid);
        q.setParameter("qid", qid);
        q.setParameter("idFacultySubject", idFacultySubject);
        return q.getResultList();
    }
    
}
