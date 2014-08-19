/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package beans;

import entities.F360FeedbackDepartment;
import entities.F360QuestionDepartment;
import entities.F360User;
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
public class F360FeedbackDepartmentFacade extends AbstractFacade<F360FeedbackDepartment> {
    @PersistenceContext(unitName = "FeedbackJPAPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public F360FeedbackDepartmentFacade() {
        super(F360FeedbackDepartment.class);
    }
    
    public List getItemsUsername(F360User username) {
        Query q = em.createNamedQuery("F360FeedbackDepartment.findByUid");
        q.setParameter("uid", username);
        return q.getResultList();
    }
    public List getItemsUsername(F360User username, F360QuestionDepartment qid) {
        Query q = em.createNamedQuery("F360FeedbackDepartment.findByUidQid");
        q.setParameter("uid", username);
        q.setParameter("qid", qid);

        return q.getResultList();
    }
}
