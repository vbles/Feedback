/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package beans;

import controllers.Feedback2013StudentController;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.bean.ManagedBean;

import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;


/**
 *
 * @author Ashish
 */
@ManagedBean
@RequestScoped
public class AuthBackingBean {

private static final Logger log = Logger.getLogger(AuthBackingBean.class.getName());

public String logout() {
String result="/index?faces-redirect=true";

FacesContext context = FacesContext.getCurrentInstance();
Feedback2013StudentController studentController = (Feedback2013StudentController) context.getApplication().getELResolver().getValue(context.getELContext(), null, "feedback2013StudentController");
studentController.getSelected().setLogoutTime(new Date());
studentController.update();


HttpServletRequest request = (HttpServletRequest)context.getExternalContext().getRequest();

try {
request.logout();
} catch (ServletException e) {
log.log(Level.SEVERE, "Failed to logout user!", e);
result = "/loginError?faces-redirect=true";
}

return result;
}
}
