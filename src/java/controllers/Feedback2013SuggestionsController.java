package controllers;

import entities.Feedback2013Suggestions;
import controllers.util.JsfUtil;
import controllers.util.PaginationHelper;
import beans.Feedback2013SuggestionsFacade;
import entities.Feedback2013Student;

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

@Named("feedback2013SuggestionsController")
@SessionScoped
public class Feedback2013SuggestionsController implements Serializable {

    private Feedback2013Suggestions current;
    private DataModel items = null;
    @EJB
    private beans.Feedback2013SuggestionsFacade ejbFacade;
    private PaginationHelper pagination;
    private int selectedItemIndex;

    public Feedback2013SuggestionsController() {
    }

    public Feedback2013Suggestions getSelected() {
        if (current == null) {
            current = new Feedback2013Suggestions();
            selectedItemIndex = -1;
        }
        return current;
    }

    private Feedback2013SuggestionsFacade getFacade() {
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
        current = (Feedback2013Suggestions) getItems().getRowData();
        selectedItemIndex = pagination.getPageFirstItem() + getItems().getRowIndex();
        return "View";
    }

    public String prepareCreate() {
        current = new Feedback2013Suggestions();
        selectedItemIndex = -1;
        return "Create";
    }
    
    public String prepareSuggestion() {
        current = new Feedback2013Suggestions();
        return "Suggestion";
    }

    public String create() {
        
        FacesContext facesContext = FacesContext.getCurrentInstance();
        String uid = facesContext.getExternalContext().getRemoteUser();
        //String uid = "100";
        Feedback2013StudentController studentController = (Feedback2013StudentController) facesContext.getApplication().getELResolver().getValue(facesContext.getELContext(), null, "feedback2013StudentController");
        Feedback2013Student fs = studentController.getFeedback2013Student(Integer.parseInt(uid));
        current.setUid(fs.getUid());
        try {
            getFacade().create(current);
            JsfUtil.addSuccessMessage("Your suggestion was recorded. Thank you.");
            return "SelectSubject";
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
            return null;
        }
    }

    public String prepareEdit() {
        current = (Feedback2013Suggestions) getItems().getRowData();
        selectedItemIndex = pagination.getPageFirstItem() + getItems().getRowIndex();
        return "Edit";
    }

    public String update() {
        try {
            getFacade().edit(current);
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("Feedback2013SuggestionsUpdated"));
            return "View";
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
            return null;
        }
    }

    public String destroy() {
        current = (Feedback2013Suggestions) getItems().getRowData();
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
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("Feedback2013SuggestionsDeleted"));
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

    public Feedback2013Suggestions getFeedback2013Suggestions(java.lang.Integer id) {
        return ejbFacade.find(id);
    }

    @FacesConverter(forClass = Feedback2013Suggestions.class)
    public static class Feedback2013SuggestionsControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            Feedback2013SuggestionsController controller = (Feedback2013SuggestionsController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "feedback2013SuggestionsController");
            return controller.getFeedback2013Suggestions(getKey(value));
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
            if (object instanceof Feedback2013Suggestions) {
                Feedback2013Suggestions o = (Feedback2013Suggestions) object;
                return getStringKey(o.getId());
            } else {
                throw new IllegalArgumentException("object " + object + " is of type " + object.getClass().getName() + "; expected type: " + Feedback2013Suggestions.class.getName());
            }
        }
    }
}
