package controllers;

import entities.Feedback2013ExitSurvey;
import controllers.util.JsfUtil;
import controllers.util.PaginationHelper;
import beans.Feedback2013ExitSurveyFacade;
import entities.F360QuestionPeer;
import entities.Feedback2013Student;
import entities.Subject;
import entities.SubjectQuestion;

import java.io.Serializable;
import java.util.List;
import java.util.ResourceBundle;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import javax.faces.model.DataModel;
import javax.faces.model.ListDataModel;
import javax.faces.model.SelectItem;
import org.primefaces.event.RateEvent;

@Named("feedback2013ExitSurveyController")
@SessionScoped
public class Feedback2013ExitSurveyController implements Serializable {

    private Feedback2013ExitSurvey current;
    private DataModel items = null;
    @EJB
    private beans.Feedback2013ExitSurveyFacade ejbFacade;
    private PaginationHelper pagination;
    private int selectedItemIndex;

    public Feedback2013ExitSurveyController() {
    }

    public Feedback2013ExitSurvey getSelected() {
        if (current == null) {
            current = new Feedback2013ExitSurvey();
            selectedItemIndex = -1;
        }
        return current;
    }

    private Feedback2013ExitSurveyFacade getFacade() {
        return ejbFacade;
    }

    public PaginationHelper getPagination() {
        if (pagination == null) {
            pagination = new PaginationHelper(10) {
                @Override
                public int getItemsCount() {
                    return getFacade().count();
                }

                @Override
                public DataModel createPageDataModel() {
                    return new ListDataModel(getFacade().findRange(new int[]{getPageFirstItem(), getPageFirstItem() + getPageSize()}));
                }
            };
        }
        return pagination;
    }

    public String prepareList() {
        recreateModel();
        return "List";
    }

    public String prepareView() {
        current = (Feedback2013ExitSurvey) getItems().getRowData();
        selectedItemIndex = pagination.getPageFirstItem() + getItems().getRowIndex();
        return "View";
    }

    public String prepareCreate() {
        current = new Feedback2013ExitSurvey();
        selectedItemIndex = -1;
        return "Create";
    }
    public void onrate(RateEvent rateEvent) {
        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Rate Event", "You rated:" + ((Integer) rateEvent.getRating()).intValue());
        FacesContext facesContext = FacesContext.getCurrentInstance();
        facesContext.addMessage(null, message);
        Feedback2013StudentController controller = (Feedback2013StudentController) facesContext.getApplication().getELResolver().
                getValue(facesContext.getELContext(), null, "feedback2013StudentController");

        Feedback2013Student student = controller.getLoggedUser();

        SubjectQuestion temp = facesContext.getApplication().evaluateExpressionGet(facesContext, "#{item}", SubjectQuestion.class);

        prepareCreate();
        current.setAns(((Integer) rateEvent.getRating()).shortValue());
        current.setIdSubjectQuestions(temp);
        current.setUid(student);
        current.setIdFeedback2013exitsurvey(0);
        
        List<Feedback2013ExitSurvey> list2 = getItemsByUserId(student, temp);
        if(list2.isEmpty()) {
            create();
        }
        else {
            current = list2.get(0);
            current.setAns(((Integer) rateEvent.getRating()).shortValue());
            update();
        }

        
    }
    public void oncancel() {
        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Cancel Event", "Rate Reset");

        FacesContext.getCurrentInstance().addMessage(null, message);
    }
    public String create() {
        try {
            getFacade().create(current);
            JsfUtil.addSuccessMessage("Feedback Recorded");
            return prepareCreate();
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
            return null;
        }
    }
    
    public List<Feedback2013ExitSurvey> getItemsByUserId(Feedback2013Student uid){
        return getFacade().getByUserName(uid);
    }
    public List<Feedback2013ExitSurvey> getItemsByUserId(Feedback2013Student uid, Subject subject) {
        return getFacade().getByUserName(uid, subject);
    }
    public List<Feedback2013ExitSurvey> getItemsByUserId(Feedback2013Student uid, SubjectQuestion idSubjectQuestions) {
        return getFacade().getByUserNameSubject(uid, idSubjectQuestions);
    }

    public String prepareEdit() {
        current = (Feedback2013ExitSurvey) getItems().getRowData();
        selectedItemIndex = pagination.getPageFirstItem() + getItems().getRowIndex();
        return "Edit";
    }

    public String update() {
        try {
            getFacade().edit(current);
            JsfUtil.addSuccessMessage("Feedback Updated");
            return "View";
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
            return null;
        }
    }

    public String destroy() {
        current = (Feedback2013ExitSurvey) getItems().getRowData();
        selectedItemIndex = pagination.getPageFirstItem() + getItems().getRowIndex();
        performDestroy();
        recreatePagination();
        recreateModel();
        return "List";
    }

    public String destroyAndView() {
        performDestroy();
        recreateModel();
        updateCurrentItem();
        if (selectedItemIndex >= 0) {
            return "View";
        } else {
            // all items were removed - go back to list
            recreateModel();
            return "List";
        }
    }

    private void performDestroy() {
        try {
            getFacade().remove(current);
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("Feedback2013ExitSurveyDeleted"));
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
        }
    }

    private void updateCurrentItem() {
        int count = getFacade().count();
        if (selectedItemIndex >= count) {
            // selected index cannot be bigger than number of items:
            selectedItemIndex = count - 1;
            // go to previous page if last page disappeared:
            if (pagination.getPageFirstItem() >= count) {
                pagination.previousPage();
            }
        }
        if (selectedItemIndex >= 0) {
            current = getFacade().findRange(new int[]{selectedItemIndex, selectedItemIndex + 1}).get(0);
        }
    }

    public DataModel getItems() {
        if (items == null) {
            items = getPagination().createPageDataModel();
        }
        return items;
    }

    private void recreateModel() {
        items = null;
    }

    private void recreatePagination() {
        pagination = null;
    }

    public String next() {
        getPagination().nextPage();
        recreateModel();
        return "List";
    }

    public String previous() {
        getPagination().previousPage();
        recreateModel();
        return "List";
    }

    public SelectItem[] getItemsAvailableSelectMany() {
        return JsfUtil.getSelectItems(ejbFacade.findAll(), false);
    }

    public SelectItem[] getItemsAvailableSelectOne() {
        return JsfUtil.getSelectItems(ejbFacade.findAll(), true);
    }

    public Feedback2013ExitSurvey getFeedback2013ExitSurvey(java.lang.Integer id) {
        return ejbFacade.find(id);
    }

    @FacesConverter(forClass = Feedback2013ExitSurvey.class)
    public static class Feedback2013ExitSurveyControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            Feedback2013ExitSurveyController controller = (Feedback2013ExitSurveyController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "feedback2013ExitSurveyController");
            return controller.getFeedback2013ExitSurvey(getKey(value));
        }

        java.lang.Integer getKey(String value) {
            java.lang.Integer key;
            key = Integer.valueOf(value);
            return key;
        }

        String getStringKey(java.lang.Integer value) {
            StringBuilder sb = new StringBuilder();
            sb.append(value);
            return sb.toString();
        }

        @Override
        public String getAsString(FacesContext facesContext, UIComponent component, Object object) {
            if (object == null) {
                return null;
            }
            if (object instanceof Feedback2013ExitSurvey) {
                Feedback2013ExitSurvey o = (Feedback2013ExitSurvey) object;
                return getStringKey(o.getIdFeedback2013exitsurvey());
            } else {
                throw new IllegalArgumentException("object " + object + " is of type " + object.getClass().getName() + "; expected type: " + Feedback2013ExitSurvey.class.getName());
            }
        }
    }
}
