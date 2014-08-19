/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package beans;

import entities.F360CommentsDepartment;
import entities.F360User;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author Ashish Mathew
 */
@Stateless
public class F360CommentsDepartmentFacade extends AbstractFacade<F360CommentsDepartment> {
    @PersistenceContext(unitName = "FeedbackJPAPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public F360CommentsDepartmentFacade() {
        super(F360CommentsDepartment.class);
    }

    public F360CommentsDepartment getByUid(F360User f360User) {
        Query q = em.createNamedQuery("F360CommentsDepartment.findByUid");
        q.setParameter("uid", f360User);

        if (q.getResultList().size() > 0) {
            return (F360CommentsDepartment) q.getResultList().get(0);
        }
        return null;
    }
    
}
