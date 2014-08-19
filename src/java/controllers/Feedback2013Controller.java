package controllers;

import entities.Feedback2013;
import controllers.util.JsfUtil;
import controllers.util.PaginationHelper;
import beans.Feedback2013Facade;
import entities.FQuestions;
import entities.FacultySubject;
import entities.Feedback2013Question;
import entities.Feedback2013Student;

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

@Named("feedback2013Controller")
@SessionScoped
public class Feedback2013Controller implements Serializable {

    private Feedback2013 current;
    private DataModel items = null;
    @EJB
    private beans.Feedback2013Facade ejbFacade;
    private PaginationHelper pagination;
    private int selectedItemIndex;

    public Feedback2013Controller() {
    }

    public Feedback2013 getSelected() {
        if (current == null) {
            current = new Feedback2013();
            selectedItemIndex = -1;
        }
        return current;
    }

    private Feedback2013Facade getFacade() {
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
        current = (Feedback2013) getItems().getRowData();
        selectedItemIndex = pagination.getPageFirstItem() + getItems().getRowIndex();
        return "View";
    }

    public String prepareCreate() {
        current = new Feedback2013();
        selectedItemIndex = -1;
        return "Create";
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

    public String prepareEdit() {
        current = (Feedback2013) getItems().getRowData();
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
        current = (Feedback2013) getItems().getRowData();
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
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("Feedback2013Deleted"));
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
public void onrate(RateEvent rateEvent) {
    System.out.println(rateEvent.getRating());
        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Rate Event", "You rated:" + Integer.parseInt((String) rateEvent.getRating()));
        FacesContext facesContext = FacesContext.getCurrentInstance();
        facesContext.addMessage(null, message);
        Feedback2013StudentController controller = (Feedback2013StudentController) facesContext.getApplication().getELResolver().
                getValue(facesContext.getELContext(), null, "feedback2013StudentController");

        Feedback2013Student student = controller.getLoggedUser();

        FQuestions temp = facesContext.getApplication().evaluateExpressionGet(facesContext, "#{c}", FQuestions.class);
        FacultySubject temp2 = facesContext.getApplication().evaluateExpressionGet(facesContext, "#{item}", FacultySubject.class);

        prepareCreate();
        current.setIdAnswer((short) Integer.parseInt((String) rateEvent.getRating()));
        current.setIdFacultySubject(temp2);
        current.setUid(student);
        current.setQid(temp.getQ());
        current.setId(0l);
        
        List<Feedback2013> list2 = getItemsByUserId(student, temp2, temp.getQ());
        if(list2.isEmpty()) {
            create();
        }
        else {
            current = list2.get(0);
            current.setIdAnswer((short) Integer.parseInt((String) rateEvent.getRating()));
            update();
        }

        
    }
    public void oncancel() {
        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Cancel Event", "Rate Reset");

        FacesContext.getCurrentInstance().addMessage(null, message);
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

    public Feedback2013 getFeedback2013(java.lang.Long id) {
        return ejbFacade.find(id);
    }

    private List<Feedback2013> getItemsByUserId(Feedback2013Student student, FacultySubject fs, Feedback2013Question fq) {
        return getFacade().findByUserId(student, fs, fq);
    }

    @FacesConverter(forClass = Feedback2013.class)
    public static class Feedback2013ControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            Feedback2013Controller controller = (Feedback2013Controller) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "feedback2013Controller");
            return controller.getFeedback2013(getKey(value));
        }

        java.lang.Long getKey(String value) {
            java.lang.Long key;
            key = Long.valueOf(value);
            return key;
        }

        String getStringKey(java.lang.Long value) {
            StringBuilder sb = new StringBuilder();
            sb.append(value);
            return sb.toString();
        }

        @Override
        public String getAsString(FacesContext facesContext, UIComponent component, Object object) {
            if (object == null) {
                return null;
            }
            if (object instanceof Feedback2013) {
                Feedback2013 o = (Feedback2013) object;
                return getStringKey(o.getId());
            } else {
                throw new IllegalArgumentException("object " + object + " is of type " + object.getClass().getName() + "; expected type: " + Feedback2013.class.getName());
            }
        }
    }
}
