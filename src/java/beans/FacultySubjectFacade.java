/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package beans;

import entities.FacultySubject;
import entities.Feedback2013Student;
import entities.Subject;
import java.util.List;
import java.util.ArrayList;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author Ashish Mathew
 */
@Stateless
public class FacultySubjectFacade extends AbstractFacade<FacultySubject> {
    @PersistenceContext(unitName = "FeedbackJPAPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public FacultySubjectFacade() {
        super(FacultySubject.class);
    }
    
    public FacultySubject findByBatch(Feedback2013Student fs, Subject idSubject, short batch){
        List<FacultySubject> l = new ArrayList<FacultySubject>();
        Query q = em.createNamedQuery("FacultySubject.findByBatchFS");
        Integer yr = 2014;
        q.setParameter("division", fs.getDivision());
        q.setParameter("idSubject", idSubject);
        q.setParameter("batch", batch);
        q.setParameter("ac_yr", yr);
        l = q.getResultList();
        if(l.size()> 0)
        return l.get(0);
        else
            return null;
    }
    
}
