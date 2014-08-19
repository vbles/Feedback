/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package beans;

import entities.Feedback2013ExitSurvey;
import entities.Feedback2013Student;
import entities.Subject;
import entities.SubjectQuestion;
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
public class Feedback2013ExitSurveyFacade extends AbstractFacade<Feedback2013ExitSurvey> {
    @PersistenceContext(unitName = "FeedbackJPAPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public Feedback2013ExitSurveyFacade() {
        super(Feedback2013ExitSurvey.class);
    }
    public List<Feedback2013ExitSurvey> getByUserName(Feedback2013Student uid){
        Query q = em.createNamedQuery("Feedback2013ExitSurvey.findByUid");
        q.setParameter("uid", uid);
        return q.getResultList();
    }
    public List<Feedback2013ExitSurvey> getByUserName(Feedback2013Student uid, Subject idSubject) {
        Query q = em.createNamedQuery("Feedback2013ExitSurvey.findByUidSubject");
        q.setParameter("idSubject", idSubject);
        q.setParameter("uid", uid);
        return q.getResultList();
    }
    public List<Feedback2013ExitSurvey> getByUserNameSubject(Feedback2013Student uid, SubjectQuestion idSubjectQuestions){
        Query q = em.createNamedQuery("Feedback2013ExitSurvey.findByUidSubjectQ");
        q.setParameter("uid", uid);
        q.setParameter("idSubjectQuestions", idSubjectQuestions);
        return q.getResultList();
    }
    
}
