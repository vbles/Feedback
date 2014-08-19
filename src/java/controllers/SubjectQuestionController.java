package controllers;

import entities.SubjectQuestion;
import controllers.util.JsfUtil;
import controllers.util.PaginationHelper;
import beans.SubjectQuestionFacade;
import entities.Feedback2013ExitSurvey;
import entities.Feedback2013Student;
import entities.Subject;

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

@Named("subjectQuestionController")
@SessionScoped
public class SubjectQuestionController implements Serializable {

    private SubjectQuestion current;
    private DataModel items = null;
    @EJB
    private beans.SubjectQuestionFacade ejbFacade;
    private PaginationHelper pagination;
    private int selectedItemIndex;
    private List<Subject> subjectList;
    private int index;
    private boolean submitCourse;
    private Subject sub;

    public SubjectQuestionController() {
    }

    public SubjectQuestion getSelected() {
        if (current == null) {
            current = new SubjectQuestion();
            selectedItemIndex = -1;
        }
        return current;
    }

    private SubjectQuestionFacade getFacade() {
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
        current = (SubjectQuestion) getItems().getRowData();
        selectedItemIndex = pagination.getPageFirstItem() + getItems().getRowIndex();
        return "View";
    }

    public String prepareCreate() {
        current = new SubjectQuestion();
        selectedItemIndex = -1;
        return "Create";
    }

    public String create() {
        try {
            getFacade().create(current);
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("SubjectQuestionCreated"));
            return prepareCreate();
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
            return null;
        }
    }

    public String prepareEdit() {
        current = (SubjectQuestion) getItems().getRowData();
        selectedItemIndex = pagination.getPageFirstItem() + getItems().getRowIndex();
        return "Edit";
    }

    public String update() {
        try {
            getFacade().edit(current);
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("SubjectQuestionUpdated"));
            return "View";
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
            return null;
        }
    }

    public String destroy() {
        current = (SubjectQuestion) getItems().getRowData();
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
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("SubjectQuestionDeleted"));
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
    
    public String navCourseSurvey() {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        Feedback2013StudentController controller = (Feedback2013StudentController) facesContext.getApplication().getELResolver().
                getValue(facesContext.getELContext(), null, "feedback2013StudentController");
        SubjectController subjectController = (SubjectController) facesContext.getApplication().getELResolver().
                getValue(facesContext.getELContext(), null, "subjectController");

        Feedback2013Student student = controller.getLoggedUser();
        short semester = student.getSemester();
        subjectList = subjectController.getSubjectBySemester(student.getProgramCourse(),semester);
        sub = subjectList.get(index);
        return "CourseExit?faces-redirect=true";
    }

    public List<SubjectQuestion> getSubjectQuestions(){
        FacesContext facesContext = FacesContext.getCurrentInstance();
        Feedback2013StudentController controller = (Feedback2013StudentController) facesContext.getApplication().getELResolver().
                getValue(facesContext.getELContext(), null, "feedback2013StudentController");
        Feedback2013ExitSurveyController feedback2013ExitSurveyController = (Feedback2013ExitSurveyController) facesContext.getApplication().getELResolver().
                getValue(facesContext.getELContext(), null, "feedback2013ExitSurveyController");

        Feedback2013Student student = controller.getLoggedUser();
        List<SubjectQuestion> list = getFacade().getSubjectQuestions(sub);
        
        List<Feedback2013ExitSurvey> list2 = feedback2013ExitSurveyController.getItemsByUserId(student, sub);
        for(Feedback2013ExitSurvey item : list2) {
            list.get(item.getIdSubjectQuestions().getQno()-1).setRating(item.getAns());
        }
        return list; 
         
         
    }
    public String nextQuestion() {
        System.out.println(++index);
        if (index < subjectList.size()) {
            sub = subjectList.get(index);
            return "CourseExit?faces-redirect=true";

        }
        else {
            submitCourse = true;
            return "SelectSubject?faces-redirect=true";
        }
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

    public SubjectQuestion getSubjectQuestion(java.lang.Integer id) {
        return ejbFacade.find(id);
    }

    public boolean isSubmitCourse() {
        return submitCourse;
    }

    public void setSubmitCourse(boolean submitCourse) {
        this.submitCourse = submitCourse;
    }

    public Subject getSub() {
        return sub;
    }

    public void setSub(Subject sub) {
        this.sub = sub;
    }

    @FacesConverter(forClass = SubjectQuestion.class)
    public static class SubjectQuestionControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            SubjectQuestionController controller = (SubjectQuestionController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "subjectQuestionController");
            return controller.getSubjectQuestion(getKey(value));
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
            if (object instanceof SubjectQuestion) {
                SubjectQuestion o = (SubjectQuestion) object;
                return getStringKey(o.getIdSubjectQuestions());
            } else {
                throw new IllegalArgumentException("object " + object + " is of type " + object.getClass().getName() + "; expected type: " + SubjectQuestion.class.getName());
            }
        }
    }
}
