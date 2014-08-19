/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package beans;

import entities.ProgramCourse;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author Ashish Mathew
 */
@Stateless
public class ProgramCourseFacade extends AbstractFacade<ProgramCourse> {
    @PersistenceContext(unitName = "FeedbackJPAPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public ProgramCourseFacade() {
        super(ProgramCourse.class);
    }
    
}
