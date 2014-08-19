package controllers;

import entities.Feedback2013Comments;
import controllers.util.JsfUtil;
import controllers.util.PaginationHelper;
import beans.Feedback2013CommentsFacade;
import entities.FacultySubject;
import entities.Feedback2013Student;

import java.io.Serializable;
import java.util.ArrayList;
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

@Named("feedback2013CommentsController")
@SessionScoped
public class Feedback2013CommentsController implements Serializable {

    private Feedback2013Comments current;
    private DataModel items = null;
    @EJB
    private beans.Feedback2013CommentsFacade ejbFacade;
    private PaginationHelper pagination;
    private int selectedItemIndex;

    public Feedback2013CommentsController() {
    }

    public Feedback2013Comments getSelected() {
        if (current == null) {
            current = new Feedback2013Comments();
            selectedItemIndex = -1;
        }
        return current;
    }

    private Feedback2013CommentsFacade getFacade() {
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
        current = (Feedback2013Comments) getItems().getRowData();
        selectedItemIndex = pagination.getPageFirstItem() + getItems().getRowIndex();
        return "View";
    }

    public String prepareCreate() {
        current = new Feedback2013Comments();
        selectedItemIndex = -1;
        return "Create";
    }

    public String create() {
        try {
            getFacade().create(current);
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("Feedback2013CommentsCreated"));
            return prepareCreate();
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
            return null;
        }
    }
    public String createComment() {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        String uid = facesContext.getExternalContext().getRemoteUser();
        //String uid = "100";
        Feedback2013StudentController studentController = (Feedback2013StudentController) facesContext.getApplication().getELResolver().getValue(facesContext.getELContext(), null, "feedback2013StudentController");
        FacultySubjectController facultySubjectController = (FacultySubjectController) facesContext.getApplication().getELResolver().getValue(facesContext.getELContext(), null, "facultySubjectController");

        Feedback2013Student fs = studentController.getFeedback2013Student(Integer.parseInt(uid));
        List<FacultySubject> l = facultySubjectController.getTheory();
        List<FacultySubject> k = facultySubjectController.getPractical();

        for (FacultySubject item : l) {
            prepareCreate();
            current.setUid(fs);
            current.setComments(item.getTheoryComment());
            current.setIdFacultySubject(item);
            create();
        }
        for (FacultySubject item : k) {
            prepareCreate();
            current.setUid(fs);
            current.setComments(item.getPracticalComment());
            current.setIdFacultySubject(item);
            create();
        }

            return "SelectSubject?faces-redirect=true";
    }

    public String createComment(int type) {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        String uid = facesContext.getExternalContext().getRemoteUser();
        //String uid = "100";
        Feedback2013StudentController studentController = (Feedback2013StudentController) facesContext.getApplication().getELResolver().getValue(facesContext.getELContext(), null, "feedback2013StudentController");
        FacultySubjectController facultySubjectController = (FacultySubjectController) facesContext.getApplication().getELResolver().getValue(facesContext.getELContext(), null, "facultySubjectController");

        Feedback2013Student fs = studentController.getFeedback2013Student(Integer.parseInt(uid));
        List<FacultySubject> l;
        if (type == 0) {
            l = facultySubjectController.getTheory();
        } else {
            l = facultySubjectController.getPractical();
        }

        for (FacultySubject item : l) {
            prepareCreate();
            current.setUid(fs);
            if (type == 0) {
                current.setComments(item.getTheoryComment());
            } else {
                current.setComments(item.getPracticalComment());
            }

            current.setIdFacultySubject(item);
            create();
        }
        if (type == 0) {
            return "CommentPractical?faces-redirect=true";
        } else {
            return "SelectSubject?faces-redirect=true";
        }
    }
    
    public String editComment(int type) {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        String uid = facesContext.getExternalContext().getRemoteUser();
        //String uid = "100";
        Feedback2013StudentController studentController = (Feedback2013StudentController) facesContext.getApplication().getELResolver().getValue(facesContext.getELContext(), null, "feedback2013StudentController");
        FacultySubjectController facultySubjectController = (FacultySubjectController) facesContext.getApplication().getELResolver().getValue(facesContext.getELContext(), null, "facultySubjectController");

        Feedback2013Student fs = studentController.getFeedback2013Student(Integer.parseInt(uid));
        List<FacultySubject> l;
        if (type == 0) {
            l = facultySubjectController.getTheory();
        } else {
            l = facultySubjectController.getPractical();
        }
        for (FacultySubject item : l) {
            prepareCreate();
            current.setUid(fs);
            if (type == 0) {
                current.setComments(item.getTheoryComment());
            } else {
                current.setComments(item.getPracticalComment());
            }

            current.setIdFacultySubject(item);
            create();
        }
        if (type == 0) {
            return "SelectSubject?faces-redirect=true";
        } else {
            return "SelectSubject?faces-redirect=true";
        }
    }

    public String prepareEdit() {
        current = (Feedback2013Comments) getItems().getRowData();
        selectedItemIndex = pagination.getPageFirstItem() + getItems().getRowIndex();
        return "Edit";
    }

    public String update() {
        try {
            getFacade().edit(current);
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("Feedback2013CommentsUpdated"));
            return "View";
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
            return null;
        }
    }

    public String destroy() {
        current = (Feedback2013Comments) getItems().getRowData();
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
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("Feedback2013CommentsDeleted"));
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

    public Feedback2013Comments getFeedback2013Comments(java.lang.Integer id) {
        return ejbFacade.find(id);
    }

    @FacesConverter(forClass = Feedback2013Comments.class)
    public static class Feedback2013CommentsControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            Feedback2013CommentsController controller = (Feedback2013CommentsController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "feedback2013CommentsController");
            return controller.getFeedback2013Comments(getKey(value));
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
            if (object instanceof Feedback2013Comments) {
                Feedback2013Comments o = (Feedback2013Comments) object;
                return getStringKey(o.getId());
            } else {
                throw new IllegalArgumentException("object " + object + " is of type " + object.getClass().getName() + "; expected type: " + Feedback2013Comments.class.getName());
            }
        }
    }
}
