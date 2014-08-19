package controllers;

import entities.F360FeedbackPeer;
import controllers.util.JsfUtil;
import controllers.util.PaginationHelper;
import beans.F360FeedbackPeerFacade;
import entities.F360QuestionPeer;
import entities.F360User;
import entities.Faculty;

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

@Named("f360FeedbackPeerController")
@SessionScoped
public class F360FeedbackPeerController implements Serializable {

    private F360FeedbackPeer current;
    private DataModel items = null;
    @EJB
    private beans.F360FeedbackPeerFacade ejbFacade;
    private PaginationHelper pagination;
    private int selectedItemIndex;
    private Faculty idFaculty;

    public F360FeedbackPeerController() {
    }

    public F360FeedbackPeer getSelected() {
        if (current == null) {
            current = new F360FeedbackPeer();
            selectedItemIndex = -1;
        }
        return current;
    }

    private F360FeedbackPeerFacade getFacade() {
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
        current = (F360FeedbackPeer) getItems().getRowData();
        selectedItemIndex = pagination.getPageFirstItem() + getItems().getRowIndex();
        return "View";
    }

    public String prepareCreate() {
        current = new F360FeedbackPeer();
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
    
    public List<F360FeedbackPeer> getItemsUser(F360User uid) {
        return getFacade().getItemsUsernameFaculty(uid, idFaculty);
    }
    
    public List<F360FeedbackPeer> getItemsUsers(F360User uid, Faculty idFaculty) {
        return getFacade().getItemsUsernameFaculty(uid, idFaculty);
    }

    public List<F360FeedbackPeer> getItemsUser(F360User uid, F360QuestionPeer qid) {
        return getFacade().getItemsUsernameFaculty(uid, qid, idFaculty);
    }
    
    public String giveFeedback(Faculty idFaculty) {
        this.idFaculty = idFaculty;
        FacesContext facesContext = FacesContext.getCurrentInstance();
        F360CommentsPeerController f360CommentsPeerController = (F360CommentsPeerController) facesContext.getApplication().getELResolver().getValue(facesContext.getELContext(), null, "f360CommentsPeerController");
        f360CommentsPeerController.setIdFaculty(idFaculty);
        f360CommentsPeerController.prepareCreate();
        return "Peer1?faces-redirect=true";
    }
    
    public void onrate(RateEvent rateEvent) {
        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Rate Event", "You rated:" + ((Integer) rateEvent.getRating()).intValue());
        FacesContext facesContext = FacesContext.getCurrentInstance();
        facesContext.addMessage(null, message);
        F360UserController f360UserController = (F360UserController) facesContext.getApplication().getELResolver().getValue(facesContext.getELContext(), null, "f360UserController");
        String uid = facesContext.getExternalContext().getRemoteUser();

        System.out.println(((Integer) rateEvent.getRating()).shortValue());
        F360QuestionPeer temp = facesContext.getApplication().evaluateExpressionGet(facesContext, "#{item}", F360QuestionPeer.class);
        System.out.println(temp);
        prepareCreate();
        current.setQid(temp);
        current.setIdAns(((Integer) rateEvent.getRating()).shortValue());
        current.setIdF360FeedbackPeer(0);
        current.setIdFaculty(idFaculty);
        System.out.println(uid);
        current.setUid(f360UserController.getF360User(Integer.parseInt(uid)));
        System.out.println(getFacade().getItemsUsernameFaculty(f360UserController.getF360User(Integer.parseInt(uid)), temp, idFaculty).isEmpty());
        List<F360FeedbackPeer> userList = getFacade().getItemsUsernameFaculty(f360UserController.getF360User(Integer.parseInt(uid)), temp, idFaculty);
        System.out.println(userList);
        if (getFacade().getItemsUsernameFaculty(f360UserController.getF360User(Integer.parseInt(uid)), temp, idFaculty).isEmpty()) {
            create();
        } else {
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
        current = (F360FeedbackPeer) getItems().getRowData();
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
        current = (F360FeedbackPeer) getItems().getRowData();
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
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("F360FeedbackPeerDeleted"));
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

    public F360FeedbackPeer getF360FeedbackPeer(java.lang.Integer id) {
        return ejbFacade.find(id);
    }

    public Faculty getIdFaculty() {
        return idFaculty;
    }

    @FacesConverter(forClass = F360FeedbackPeer.class)
    public static class F360FeedbackPeerControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            F360FeedbackPeerController controller = (F360FeedbackPeerController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "f360FeedbackPeerController");
            return controller.getF360FeedbackPeer(getKey(value));
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
            if (object instanceof F360FeedbackPeer) {
                F360FeedbackPeer o = (F360FeedbackPeer) object;
                return getStringKey(o.getIdF360FeedbackPeer());
            } else {
                throw new IllegalArgumentException("object " + object + " is of type " + object.getClass().getName() + "; expected type: " + F360FeedbackPeer.class.getName());
            }
        }
    }
}
