/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package beans;

import entities.F360QuestionDepartment;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author Ashish Mathew
 */
@Stateless
public class F360QuestionDepartmentFacade extends AbstractFacade<F360QuestionDepartment> {
    @PersistenceContext(unitName = "FeedbackJPAPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public F360QuestionDepartmentFacade() {
        super(F360QuestionDepartment.class);
    }
    
}
