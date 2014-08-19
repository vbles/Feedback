package controllers;

import entities.Feedback2013User;
import controllers.util.JsfUtil;
import controllers.util.PaginationHelper;
import beans.Feedback2013UserFacade;
import entities.Feedback2013Student;

import java.io.Serializable;
import java.util.List;
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
import org.apache.commons.codec.digest.DigestUtils;


@Named("feedback2013UserController")
@SessionScoped
public class Feedback2013UserController implements Serializable {

    private Feedback2013User current;
    private DataModel items = null;
    @EJB
    private beans.Feedback2013UserFacade ejbFacade;
    private PaginationHelper pagination;
    private int selectedItemIndex;

    public Feedback2013UserController() {
    }

    public Feedback2013User getSelected() {
        if (current == null) {
            current = new Feedback2013User();
            selectedItemIndex = -1;
        }
        return current;
    }

    private Feedback2013UserFacade getFacade() {
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
        current = (Feedback2013User) getItems().getRowData();
        selectedItemIndex = pagination.getPageFirstItem() + getItems().getRowIndex();
        return "View";
    }

    public String prepareCreate() {
        current = new Feedback2013User();
        selectedItemIndex = -1;
        return "Create";
    }

    public String create() {
        try {
            getFacade().create(current);
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("Feedback2013UserCreated"));
            return prepareCreate();
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
            return null;
        }
    }

    public String prepareEdit() {
        current = (Feedback2013User) getItems().getRowData();
        selectedItemIndex = pagination.getPageFirstItem() + getItems().getRowIndex();
        return "Edit";
    }

    public String update() {
        try {
            getFacade().edit(current);
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("Feedback2013UserUpdated"));
            return "View";
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
            return null;
        }
    }
    
    public String updatePassword() {
                    FacesContext facesContext = FacesContext.getCurrentInstance();

                    Feedback2013StudentController studentController = (Feedback2013StudentController) facesContext.getApplication().getELResolver().getValue(facesContext.getELContext(), null, "feedback2013StudentController");
                    FuserGroupController groupController = (FuserGroupController) facesContext.getApplication().getELResolver().getValue(facesContext.getELContext(), null, "fuserGroupController");

                    List<Feedback2013Student> l = studentController.findAll();
                    for (Feedback2013Student item : l) {
                        prepareCreate();
                        current.setUid(item.getUid());
                        final String hash = DigestUtils.sha256Hex(item.getPwd());
                        current.setPwd(hash);
                        create();
                        
                        groupController.prepareCreate();
                        groupController.getSelected().getFuserGroupPK().setUserName(item.getUid().toString());
                        groupController.getSelected().getFuserGroupPK().setRoleName("student");
                        groupController.create();
                        

                    }
                    return "SelectBatch?faces-redirect=true";
    
    }

    public String destroy() {
        current = (Feedback2013User) getItems().getRowData();
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
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("Feedback2013UserDeleted"));
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

    public Feedback2013User getFeedback2013User(java.lang.Integer id) {
        return ejbFacade.find(id);
    }

    @FacesConverter(forClass = Feedback2013User.class)
    public static class Feedback2013UserControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            Feedback2013UserController controller = (Feedback2013UserController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "feedback2013UserController");
            return controller.getFeedback2013User(getKey(value));
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
            if (object instanceof Feedback2013User) {
                Feedback2013User o = (Feedback2013User) object;
                return getStringKey(o.getUid());
            } else {
                throw new IllegalArgumentException("object " + object + " is of type " + object.getClass().getName() + "; expected type: " + Feedback2013User.class.getName());
            }
        }
    }
}
