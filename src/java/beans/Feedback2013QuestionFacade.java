/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package beans;

import entities.Feedback2013Question;
import entities.Program;
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
public class Feedback2013QuestionFacade extends AbstractFacade<Feedback2013Question> {
    @PersistenceContext(unitName = "FeedbackJPAPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public Feedback2013QuestionFacade() {
        super(Feedback2013Question.class);
    }
    
    public List<Feedback2013Question> getTheory(Program p) {
        Query q = em.createNamedQuery("Feedback2013Question.findByQtype");
        q.setParameter("idProgram", p);
        q.setParameter("qtype", 0);
        return q.getResultList();
                
    }
    public List<Feedback2013Question> getPractical(Program p) {
        Query q = em.createNamedQuery("Feedback2013Question.findByQtype");
        q.setParameter("idProgram", p);
        q.setParameter("qtype", 1);
        return q.getResultList();
                
    }
    
}
