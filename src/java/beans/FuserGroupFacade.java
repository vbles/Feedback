/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package beans;

import entities.FuserGroup;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author Ashish Mathew
 */
@Stateless
public class FuserGroupFacade extends AbstractFacade<FuserGroup> {
    @PersistenceContext(unitName = "FeedbackJPAPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public FuserGroupFacade() {
        super(FuserGroup.class);
    }
    
}
