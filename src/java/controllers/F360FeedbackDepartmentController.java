package controllers;

import entities.F360FeedbackDepartment;
import controllers.util.JsfUtil;
import controllers.util.PaginationHelper;
import beans.F360FeedbackDepartmentFacade;
import entities.F360QuestionDepartment;
import entities.F360User;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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

@Named("f360FeedbackDepartmentController")
@SessionScoped
public class F360FeedbackDepartmentController implements Serializable {

    private F360FeedbackDepartment current;
    private DataModel items = null;
    @EJB
    private beans.F360FeedbackDepartmentFacade ejbFacade;
    private PaginationHelper pagination;
    private int selectedItemIndex;
    private HashMap<Integer, Integer> rating;
    private int rate;

    public F360FeedbackDepartmentController() {
    }

    public F360FeedbackDepartment getSelected() {
        if (current == null) {
            current = new F360FeedbackDepartment();
            selectedItemIndex = -1;
        }
        return current;
    }

    private F360FeedbackDepartmentFacade getFacade() {
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
        current = (F360FeedbackDepartment) getItems().getRowData();
        selectedItemIndex = pagination.getPageFirstItem() + getItems().getRowIndex();
        return "View";
    }

    public String prepareCreate() {
        current = new F360FeedbackDepartment();
        selectedItemIndex = -1;
        return "Create";
    }

    public String create() {
        try {
            getFacade().create(current);
            JsfUtil.addSuccessMessage("Feedback recorded");
            return prepareCreate();
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
            return null;
        }
    }

    public String submit() {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        F360UserController f360UserController = (F360UserController) facesContext.getApplication().getELResolver().getValue(facesContext.getELContext(), null, "f360UserController");
        //Feedback2013Controller feedbackController = (Feedback2013Controller) facesContext.getApplication().getELResolver().getValue(facesContext.getELContext(), null, "feedback2013Controller");
        String uid = facesContext.getExternalContext().getRemoteUser();

        for (Map.Entry<Integer, Integer> entry : rating.entrySet()) {
            int qid = entry.getKey();
            int ans = entry.getValue();

            prepareCreate();
//            current.setQid((short) qid);
            current.setIdAns((short) ans);
            current.setId(0);
            current.setUid(f360UserController.getF360User(Integer.parseInt(uid)));
            create();

        }
        //Status of Department has to be set.
        return "SelectSubject?faces-redirect=true";
    }

    public void onrate(RateEvent rateEvent) {
        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Rate Event", "You rated:" + ((Integer) rateEvent.getRating()).intValue());
        FacesContext facesContext = FacesContext.getCurrentInstance();
        facesContext.addMessage(null, message);
        F360UserController f360UserController = (F360UserController) facesContext.getApplication().getELResolver().getValue(facesContext.getELContext(), null, "f360UserController");
        String uid = facesContext.getExternalContext().getRemoteUser();

        System.out.println(((Integer) rateEvent.getRating()).shortValue());
        F360QuestionDepartment temp = facesContext.getApplication().evaluateExpressionGet(facesContext, "#{item}", F360QuestionDepartment.class);
        System.out.println(temp);
        prepareCreate();
        current.setQid(temp);
        current.setIdAns(((Integer) rateEvent.getRating()).shortValue());
        current.setId(0);
        System.out.println(uid);
        current.setUid(f360UserController.getF360User(Integer.parseInt(uid)));
        System.out.println(getFacade().getItemsUsername(f360UserController.getF360User(Integer.parseInt(uid)), temp).isEmpty());
        List<F360FeedbackDepartment> userList = getFacade().getItemsUsername(f360UserController.getF360User(Integer.parseInt(uid)), temp);
        System.out.println(userList);
        if( getFacade().getItemsUsername(f360UserController.getF360User(Integer.parseInt(uid)), temp).isEmpty())
        {
            create();
        }
        else
        {
            current = userList.get(0);
            current.setIdAns(((Integer) rateEvent.getRating()).shortValue());
            update();
        }
    }

    public void oncancel() {
        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Cancel Event", "Rate Reset");

        FacesContext.getCurrentInstance().addMessage(null, message);
    }

    public String prepareEdit() {
        current = (F360FeedbackDepartment) getItems().getRowData();
        selectedItemIndex = pagination.getPageFirstItem() + getItems().getRowIndex();
        return "Edit";
    }

    public String update() {
        try {
            getFacade().edit(current);
            JsfUtil.addSuccessMessage("Feedback updated");
            return "View";
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
            return null;
        }
    }

    public String destroy() {
        current = (F360FeedbackDepartment) getItems().getRowData();
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
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("F360FeedbackDepartmentDeleted"));
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

    public List<F360FeedbackDepartment> getItemsUser(F360User uid) {
        return getFacade().getItemsUsername(uid);
    }

    public List<F360FeedbackDepartment> getItemsUser(F360User uid, F360QuestionDepartment qid) {
        return getFacade().getItemsUsername(uid, qid);
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

    public F360FeedbackDepartment getF360FeedbackDepartment(java.lang.Integer id) {
        return ejbFacade.find(id);
    }

    public HashMap<Integer, Integer> getRating() {
        return rating;
    }

    public void setRating(HashMap<Integer, Integer> rating) {
        this.rating = rating;
    }

    public int getRate() {
        return rate;
    }

    public void setRate(int rate) {
        this.rate = rate;
    }

    @FacesConverter(forClass = F360FeedbackDepartment.class)
    public static class F360FeedbackDepartmentControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            F360FeedbackDepartmentController controller = (F360FeedbackDepartmentController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "f360FeedbackDepartmentController");
            return controller.getF360FeedbackDepartment(getKey(value));
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
            if (object instanceof F360FeedbackDepartment) {
                F360FeedbackDepartment o = (F360FeedbackDepartment) object;
                return getStringKey(o.getId());
            } else {
                throw new IllegalArgumentException("object " + object + " is of type " + object.getClass().getName() + "; expected type: " + F360FeedbackDepartment.class.getName());
            }
        }
    }
}
