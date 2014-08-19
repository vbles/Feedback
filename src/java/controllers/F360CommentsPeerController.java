package controllers;

import entities.F360CommentsPeer;
import controllers.util.JsfUtil;
import controllers.util.PaginationHelper;
import beans.F360CommentsPeerFacade;
import entities.Faculty;

import java.io.Serializable;
import java.util.ResourceBundle;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import javax.faces.model.DataModel;
import javax.faces.model.ListDataModel;
import javax.faces.model.SelectItem;

@Named("f360CommentsPeerController")
@SessionScoped
public class F360CommentsPeerController implements Serializable {

    private F360CommentsPeer current;
    private DataModel items = null;
    @EJB
    private beans.F360CommentsPeerFacade ejbFacade;
    private PaginationHelper pagination;
    private int selectedItemIndex;
    private Faculty idFaculty;
      
    public F360CommentsPeerController() {
    }

    public F360CommentsPeer getSelected() {
        if (current == null) {
            current = new F360CommentsPeer();
            selectedItemIndex = -1;
        }
        return current;
    }

    private F360CommentsPeerFacade getFacade() {
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
        current = (F360CommentsPeer) getItems().getRowData();
        selectedItemIndex = pagination.getPageFirstItem() + getItems().getRowIndex();
        return "View";
    }

    public String prepareCreate() {
        current = new F360CommentsPeer();
        selectedItemIndex = -1;
        return "Create";
    }

    public String getComment() {
        
        FacesContext facesContext = FacesContext.getCurrentInstance();
        F360UserController f360UserController = (F360UserController) facesContext.getApplication().getELResolver().getValue(facesContext.getELContext(), null, "f360UserController");
        String uid = facesContext.getExternalContext().getRemoteUser();
        F360CommentsPeer comment = getFacade().getByUidIdFac(idFaculty, f360UserController.getF360User(Integer.parseInt(uid)));
        if(comment != null)
            return comment.getComments();
        return null;
    }
    
    public String getComment(Faculty idFaculty) {

        FacesContext facesContext = FacesContext.getCurrentInstance();
        F360UserController f360UserController = (F360UserController) facesContext.getApplication().getELResolver().getValue(facesContext.getELContext(), null, "f360UserController");
        String uid = facesContext.getExternalContext().getRemoteUser();
        F360CommentsPeer comment = getFacade().getByUidIdFac(idFaculty, f360UserController.getF360User(Integer.parseInt(uid)));
        if (comment != null) {
            return comment.getComments();
        }
        return null;
    }
    
    public void ajaxCreate(){
        current.setIdFaculty(idFaculty);
        FacesContext facesContext = FacesContext.getCurrentInstance();
        F360UserController f360UserController = (F360UserController) facesContext.getApplication().getELResolver().getValue(facesContext.getELContext(), null, "f360UserController");
        String uid = facesContext.getExternalContext().getRemoteUser();
        current.setId(0);
        current.setUid(f360UserController.getF360User(Integer.parseInt(uid)));
        create();
    }
    public String create() {
        try {
            getFacade().create(current);
            JsfUtil.addSuccessMessage("Comment Recorded");
            return prepareCreate();
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
            return null;
        }
    }

    public String prepareEdit() {
        current = (F360CommentsPeer) getItems().getRowData();
        selectedItemIndex = pagination.getPageFirstItem() + getItems().getRowIndex();
        return "Edit";
    }

    public String update() {
        try {
            getFacade().edit(current);
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("F360CommentsPeerUpdated"));
            return "View";
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
            return null;
        }
    }

    public String destroy() {
        current = (F360CommentsPeer) getItems().getRowData();
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
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("F360CommentsPeerDeleted"));
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

    public F360CommentsPeer getF360CommentsPeer(java.lang.Integer id) {
        return ejbFacade.find(id);
    }

    public Faculty getIdFaculty() {
        return idFaculty;
    }

    public void setIdFaculty(Faculty idFaculty) {
        this.idFaculty = idFaculty;
    }

    @FacesConverter(forClass = F360CommentsPeer.class)
    public static class F360CommentsPeerControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            F360CommentsPeerController controller = (F360CommentsPeerController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "f360CommentsPeerController");
            return controller.getF360CommentsPeer(getKey(value));
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
            if (object instanceof F360CommentsPeer) {
                F360CommentsPeer o = (F360CommentsPeer) object;
                return getStringKey(o.getId());
            } else {
                throw new IllegalArgumentException("object " + object + " is of type " + object.getClass().getName() + "; expected type: " + F360CommentsPeer.class.getName());
            }
        }
    }
}
