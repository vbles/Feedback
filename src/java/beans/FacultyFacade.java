/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package beans;

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
public class FacultyFacade extends AbstractFacade<Faculty> {
    @PersistenceContext(unitName = "FeedbackJPAPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public FacultyFacade() {
        super(Faculty.class);
    }
    
    public List findByDepartment (String department) {
        Query q = em.createNamedQuery("Faculty.findByFacultyDepartment");
        q.setParameter("facultyDepartment", department);
        return q.getResultList();
        
    }
    
}
