/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package beans;

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
public class SubjectQuestionFacade extends AbstractFacade<SubjectQuestion> {
    @PersistenceContext(unitName = "FeedbackJPAPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public SubjectQuestionFacade() {
        super(SubjectQuestion.class);
    }
    
    public List<SubjectQuestion> getSubjectQuestions(Subject idSubject){
        Query q = em.createNamedQuery("SubjectQuestion.findByIdSubject");
        q.setParameter("idSubject", idSubject);
        return q.getResultList();
    }
}
