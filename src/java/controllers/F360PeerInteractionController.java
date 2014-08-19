package controllers;

import entities.F360PeerInteraction;
import controllers.util.JsfUtil;
import controllers.util.PaginationHelper;
import beans.F360PeerInteractionFacade;
import entities.F360User;
import entities.Faculty;

import java.io.Serializable;
import java.util.HashMap;
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
import javax.faces.event.ValueChangeEvent;
import javax.faces.model.DataModel;
import javax.faces.model.ListDataModel;
import javax.faces.model.SelectItem;

@Named("f360PeerInteractionController")
@SessionScoped
public class F360PeerInteractionController implements Serializable {

    private F360PeerInteraction current;
    private DataModel items = null;
    @EJB
    private beans.F360PeerInteractionFacade ejbFacade;
    private PaginationHelper pagination;
    private int selectedItemIndex;
    private HashMap<String, Boolean> value = new HashMap<>();  
    //private boolean value;

    public F360PeerInteractionController() {
    }

    public F360PeerInteraction getSelected() {
        if (current == null) {
            current = new F360PeerInteraction();
            selectedItemIndex = -1;
        }
        return current;
    }

    private F360PeerInteractionFacade getFacade() {
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
        current = (F360PeerInteraction) getItems().getRowData();
        selectedItemIndex = pagination.getPageFirstItem() + getItems().getRowIndex();
        return "View";
    }

    public String prepareCreate() {
        current = new F360PeerInteraction();
        selectedItemIndex = -1;
        return "Create";
    }

    public String create() {
        try {
            getFacade().create(current);
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("F360PeerInteractionCreated"));
            return prepareCreate();
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
            return null;
        }
    }
    
    public F360PeerInteraction getByIdFac(Faculty idFaculty){
        FacesContext facesContext = FacesContext.getCurrentInstance();
        F360UserController f360UserController = (F360UserController) facesContext.getApplication().getELResolver().getValue(facesContext.getELContext(), null, "f360UserController");
        String uid = facesContext.getExternalContext().getRemoteUser();
        F360User username = f360UserController.getF360User(Integer.parseInt(uid));
        return getFacade().getByIdFac(idFaculty, username);
    }
    
    public void ajaxCreate() {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        Faculty idFaculty = facesContext.getApplication().evaluateExpressionGet(facesContext, "#{item}", Faculty.class);
        F360UserController f360UserController = (F360UserController) facesContext.getApplication().getELResolver().getValue(facesContext.getELContext(), null, "f360UserController");
        String uid = facesContext.getExternalContext().getRemoteUser();
        F360User username = f360UserController.getF360User(Integer.parseInt(uid));
        String summary = value.get(idFaculty.getIdFaculty()) == false ? "Checked" : "Unchecked";
        prepareCreate();
        if(value.get(idFaculty.getIdFaculty()) == false) {
        current.setIdF360PeerInteraction(0);
        current.setIdFaculty(idFaculty);
        current.setUid(username);
        create();
        }
        else {
            current = getFacade().getByIdFac(idFaculty, username);
            if (current !=null)
            {
                performDestroy();
            }
        }
        facesContext.addMessage(null, new FacesMessage(summary));  
        
    }

    public String prepareEdit() {
        current = (F360PeerInteraction) getItems().getRowData();
        selectedItemIndex = pagination.getPageFirstItem() + getItems().getRowIndex();
        return "Edit";
    }

    public String update() {
        try {
            getFacade().edit(current);
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("F360PeerInteractionUpdated"));
            return "View";
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
            return null;
        }
    }

    public String destroy() {
        current = (F360PeerInteraction) getItems().getRowData();
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
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("F360PeerInteractionDeleted"));
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

    public F360PeerInteraction getF360PeerInteraction(java.lang.Integer id) {
        return ejbFacade.find(id);
    }

    public HashMap<String, Boolean> getValue() {
        return value;
    }

    public void setValue(HashMap<String, Boolean> value) {
        this.value = value;
    }

//    public boolean isValue() {
//        return value;
//    }
//
//    public void setValue(boolean value) {
//        this.value = value;
//    }

    @FacesConverter(forClass = F360PeerInteraction.class)
    public static class F360PeerInteractionControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            F360PeerInteractionController controller = (F360PeerInteractionController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "f360PeerInteractionController");
            return controller.getF360PeerInteraction(getKey(value));
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
            if (object instanceof F360PeerInteraction) {
                F360PeerInteraction o = (F360PeerInteraction) object;
                return getStringKey(o.getIdF360PeerInteraction());
            } else {
                throw new IllegalArgumentException("object " + object + " is of type " + object.getClass().getName() + "; expected type: " + F360PeerInteraction.class.getName());
            }
        }
    }
}
