package controllers;

import entities.Feedback2013Question;
import controllers.util.JsfUtil;
import controllers.util.PaginationHelper;
import beans.Feedback2013QuestionFacade;
import entities.FacultySubject;
import entities.Feedback2013Student;
import entities.Program;

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
import entities.FQuestions;
import java.util.HashMap;
import java.util.Map;

@Named("feedback2013QuestionController")
@SessionScoped
public class Feedback2013QuestionController implements Serializable {

    private Feedback2013Question current;
    private DataModel items = null;
    @EJB
    private beans.Feedback2013QuestionFacade ejbFacade;
    private PaginationHelper pagination;
    private int selectedItemIndex;
    private Program idProgram;
    private FacultySubject idFacultySubject;
    private int qNo;
    private String qText;
    private int qNo2;
    private String qText2;
    private List<Feedback2013Question> questionList;
    private int index;
    private boolean submitTheory;
    private boolean submitPractical;
    private int indexTheory;
    private int indexPractical;
    private List<FacultySubject> theory;
    private List<FacultySubject> practical;
    private List<FQuestions> qList;

    public Feedback2013QuestionController() {
    }

    public Feedback2013Question getSelected() {
        if (current == null) {
            current = new Feedback2013Question();
            selectedItemIndex = -1;
        }
        return current;
    }

    private Feedback2013QuestionFacade getFacade() {
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
        current = (Feedback2013Question) getItems().getRowData();
        selectedItemIndex = pagination.getPageFirstItem() + getItems().getRowIndex();
        return "View";
    }

    public String prepareCreate() {
        current = new Feedback2013Question();
        selectedItemIndex = -1;
        return "Create";
    }

    public String create() {
        try {
            getFacade().create(current);
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("Feedback2013QuestionCreated"));
            return prepareCreate();
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
            return null;
        }
    }

    public String prepareEdit() {
        current = (Feedback2013Question) getItems().getRowData();
        selectedItemIndex = pagination.getPageFirstItem() + getItems().getRowIndex();
        return "Edit";
    }

    public Program getProgram() {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        String uid = facesContext.getExternalContext().getRemoteUser();
        //String uid = "100";
        Feedback2013StudentController studentController = (Feedback2013StudentController) facesContext.getApplication().getELResolver().getValue(facesContext.getELContext(), null, "feedback2013StudentController");
        Feedback2013Student fs = studentController.getFeedback2013Student(Integer.parseInt(uid));
        FacultySubjectController facultySubjectController = (FacultySubjectController) facesContext.getApplication().getELResolver().getValue(facesContext.getELContext(), null, "facultySubjectController");

        theory = facultySubjectController.getTheory();
        practical = facultySubjectController.getPractical();
        // idFacultySubject = item;
        idProgram = fs.getProgramCourse().getProgram();
        return idProgram;
    }

    public String theoryQuestionnaire() {
        index = indexTheory;
        getProgram();
        getTheory();
        return "Theory?faces-redirect=true";
    }

    public String theoryQuestionnaire1() {
        index = indexTheory;
        getProgram();
        getTheory();
        return "Theory_1?faces-redirect=true";
    }

    public String practicalQuestionnaire() {
        index = indexPractical;
        getProgram();
        getPractical();
        return "Practical?faces-redirect=true";
    }

    public String practicalQuestionnaire1() {
        index = indexPractical;
        getProgram();
        getPractical();
        return "Practical_1?faces-redirect=true";
    }

    public void getTheory() {

        questionList = getFacade().getTheory(idProgram);
        Map<Integer, Short> theoryRatingList = new HashMap<>();
        int i = 0;
        for (FacultySubject x : theory) {
            theoryRatingList.put(i++, (short) 0);
        }
        qList = new ArrayList<>();
        for (Feedback2013Question item : questionList) {
            FQuestions e = new FQuestions();
            e.setQ(item);
            e.setFsList(theory);
            e.setRatings(theoryRatingList);
            qList.add(e);
        }
        qNo = questionList.get(index).getQno();
        qText = questionList.get(index).getQtext();
        qNo2 = questionList.get(index + 1).getQno();
        qText2 = questionList.get(index + 1).getQtext();
    }

    public void getPractical() {
        questionList = getFacade().getPractical(idProgram);
        Map<Integer, Short> practicalRatingList = new HashMap<>();
        int i = 0;
        for (FacultySubject x : practical) {
            practicalRatingList.put(i++, (short) 0);
        }
        qList = new ArrayList<>();
        for (Feedback2013Question item : questionList) {
            FQuestions e = new FQuestions();
            e.setQ(item);
            e.setFsList(practical);
            e.setRatings(practicalRatingList);
            qList.add(e);
        }
        qNo = questionList.get(index).getQno();
        qText = questionList.get(index).getQtext();
        qNo2 = questionList.get(index + 1).getQno();
        qText2 = questionList.get(index + 1).getQtext();
    }

    public String nextQuestion(int type) {

        FacesContext facesContext = FacesContext.getCurrentInstance();
        FacultySubjectController facultySubjectController = (FacultySubjectController) facesContext.getApplication().getELResolver().getValue(facesContext.getELContext(), null, "facultySubjectController");
        Feedback2013Controller feedbackController = (Feedback2013Controller) facesContext.getApplication().getELResolver().getValue(facesContext.getELContext(), null, "feedback2013Controller");
        String uid = facesContext.getExternalContext().getRemoteUser();
        //String uid = "100";
        Feedback2013StudentController studentController = (Feedback2013StudentController) facesContext.getApplication().getELResolver().getValue(facesContext.getELContext(), null, "feedback2013StudentController");
        Feedback2013Student fs = studentController.getFeedback2013Student(Integer.parseInt(uid));
        List<FacultySubject> l = new ArrayList<>();
        if (type == 0) {
            l = facultySubjectController.getTheory();
        } else if (type == 1) {
            l = facultySubjectController.getPractical();

        }
        if (l == null) {
            return submit(type);
        }
        for (FacultySubject item : l) {
            //feedbackController.prepareCreate();
            feedbackController.getSelected().setIdFacultySubject(item);
            feedbackController.getSelected().setUid(fs);
            feedbackController.getSelected().setQid(questionList.get(index));
            feedbackController.getSelected().setIdAnswer((short) item.getRating());
            feedbackController.create();
            item.setRating(0);

            feedbackController.getSelected().setIdFacultySubject(item);
            feedbackController.getSelected().setUid(fs);
            feedbackController.getSelected().setQid(questionList.get(index + 1));
            feedbackController.getSelected().setIdAnswer((short) item.getRating2());
            feedbackController.create();
            item.setRating2(0);
        }
        if (index < questionList.size() - 2) {

            index = index + 2;
            qNo = questionList.get(index).getQno();
            qText = questionList.get(index).getQtext();
            qNo2 = questionList.get(index + 1).getQno();
            qText2 = questionList.get(index + 1).getQtext();

            if (type == 0) {
                indexTheory = index;
                return "Theory?faces-redirect=true";
            } else {
                indexPractical = index;
                return "Practical?faces-redirect=true";
            }


        } else {
            return submit(type);
        }
    }

    public int getqNo() {
        return qNo;
    }

    public void setqNo(int qNo) {
        this.qNo = qNo;
    }

    public String getqText() {
        return qText;
    }

    public void setqText(String qText) {
        this.qText = qText;
    }

    public int getqNo2() {
        return qNo2;
    }

    public void setqNo2(int qNo2) {
        this.qNo2 = qNo2;
    }

    public String getqText2() {
        return qText2;
    }

    public void setqText2(String qText2) {
        this.qText2 = qText2;
    }

    public String submit(int type) {
        //idFacultySubject.setSubmitted(true);
        index = 0;
        indexTheory = 0;
        indexPractical = 0;
        if (type == 0) {
            submitTheory = true;
            submitPractical = true; // Temporary - as no need for practical feedback
            
        }
        /*else
         submitPractical = true;*/
        return "SelectSubject?faces-redirect=true";
    }

    public boolean isSubmitTheory() {
        return submitTheory;
    }

    public void setSubmitTheory(boolean submitTheory) {
        this.submitTheory = submitTheory;
    }

    public boolean isSubmitPractical() {
        return submitPractical;
    }

    public void setSubmitPractical(boolean submitPractical) {
        this.submitPractical = submitPractical;
    }

    public String update() {
        try {
            getFacade().edit(current);
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("Feedback2013QuestionUpdated"));
            return "View";
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
            return null;
        }
    }

    public String destroy() {
        current = (Feedback2013Question) getItems().getRowData();
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
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("Feedback2013QuestionDeleted"));
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

    public Feedback2013Question getFeedback2013Question(java.lang.Integer id) {
        return ejbFacade.find(id);
    }

    public List<FQuestions> getqList() {
        return qList;
    }

    public void setqList(List<FQuestions> qList) {
        this.qList = qList;
    }

    @FacesConverter(forClass = Feedback2013Question.class)
    public static class Feedback2013QuestionControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            Feedback2013QuestionController controller = (Feedback2013QuestionController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "feedback2013QuestionController");
            return controller.getFeedback2013Question(getKey(value));
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
            if (object instanceof Feedback2013Question) {
                Feedback2013Question o = (Feedback2013Question) object;
                return getStringKey(o.getQid());
            } else {
                throw new IllegalArgumentException("object " + object + " is of type " + object.getClass().getName() + "; expected type: " + Feedback2013Question.class.getName());
            }
        }
    }
}
