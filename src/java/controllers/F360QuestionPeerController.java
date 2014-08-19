package controllers;

import entities.F360QuestionPeer;
import controllers.util.JsfUtil;
import controllers.util.PaginationHelper;
import beans.F360QuestionPeerFacade;
import entities.F360FeedbackDepartment;
import entities.F360FeedbackPeer;
import entities.F360QuestionDepartment;
import entities.F360User;
import entities.Faculty;

import java.io.Serializable;
import java.util.HashMap;
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

@Named("f360QuestionPeerController")
@SessionScoped
public class F360QuestionPeerController implements Serializable {

    private F360QuestionPeer current;
    private DataModel items = null;
    @EJB
    private beans.F360QuestionPeerFacade ejbFacade;
    private PaginationHelper pagination;
    private int selectedItemIndex;
    private List<F360QuestionPeer> peerQuestionList;
    private short qType;
    private String qText;
    private Integer qNo;
    private F360User user;

    public F360QuestionPeerController() {
    }

    public F360QuestionPeer getSelected() {
        if (current == null) {
            current = new F360QuestionPeer();
            selectedItemIndex = -1;
        }
        return current;
    }

    private F360QuestionPeerFacade getFacade() {
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
        current = (F360QuestionPeer) getItems().getRowData();
        selectedItemIndex = pagination.getPageFirstItem() + getItems().getRowIndex();
        return "View";
    }

    public String prepareCreate() {
        current = new F360QuestionPeer();
        selectedItemIndex = -1;
        return "Create";
    }

    public String create() {
        try {
            getFacade().create(current);
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("F360QuestionPeerCreated"));
            return prepareCreate();
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
            return null;
        }
    }

    public String prepareEdit() {
        current = (F360QuestionPeer) getItems().getRowData();
        selectedItemIndex = pagination.getPageFirstItem() + getItems().getRowIndex();
        return "Edit";
    }
    
    public String peerQuestionnaire() {
        user = getUserSession();
        getQuestionList(getIndex());
        //List<Faculty> temp = getFacultyList();
        return "Peer?faces-redirect=true";
    }
    
    public String update() {
        try {
            getFacade().edit(current);
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("F360QuestionPeerUpdated"));
            return "View";
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
            return null;
        }
    }

    public String destroy() {
        current = (F360QuestionPeer) getItems().getRowData();
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
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("F360QuestionPeerDeleted"));
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
    
    public DataModel getItemsNoPagination() {
        List<F360QuestionPeer> list = getFacade().findAll();
        FacesContext facesContext = FacesContext.getCurrentInstance();
        F360UserController f360UserController = (F360UserController) facesContext.getApplication().getELResolver().getValue(facesContext.getELContext(), null, "f360UserController");
        F360FeedbackPeerController f360FeedbackPeerController = (F360FeedbackPeerController) facesContext.getApplication().getELResolver().getValue(facesContext.getELContext(), null, "f360FeedbackPeerController");
        String username = facesContext.getExternalContext().getRemoteUser();
        F360User uid = f360UserController.getF360User(Integer.parseInt(username));
        List<F360FeedbackPeer> list2 = f360FeedbackPeerController.getItemsUser(f360UserController.getF360User(Integer.parseInt(username)));
        for (F360FeedbackPeer item : list2) {
            list.get(list.indexOf(item.getQid())).setRating(item.getIdAns());

        }

        return new ListDataModel(list);
        
        //return new ListDataModel(getFacade().findAll());
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

    public F360QuestionPeer getF360QuestionPeer(java.lang.Integer id) {
        return ejbFacade.find(id);
    }

    private void getQuestionList(int index) {
        peerQuestionList = getFacade().findAll();
        qNo = peerQuestionList.get(index).getQid();
        qText = peerQuestionList.get(index).getQtext();
        qType = peerQuestionList.get(index).getQtype();
    }
    
    private int getIndex() {
        return 0;
    }

    public List<Faculty> getFacultyList() {
        
        FacesContext facesContext = FacesContext.getCurrentInstance();
        FacultyController facultyController = (FacultyController) facesContext.getApplication().getELResolver().getValue(facesContext.getELContext(), null, "facultyController");
        F360PeerInteractionController f360PeerInteractionController = (F360PeerInteractionController) facesContext.getApplication().getELResolver().getValue(facesContext.getELContext(), null, "f360PeerInteractionController");
        List<Faculty> fList = facultyController.getFacultyList(user.getDepartment());
        HashMap<String, Boolean> temp = f360PeerInteractionController.getValue();
        for(Faculty item : fList) {
            if (f360PeerInteractionController.getByIdFac(item) != null) {
              temp.put(item.getIdFaculty(), Boolean.TRUE);
            }
            else 
            {
                temp.put(item.getIdFaculty(), Boolean.FALSE);
            }
        }
        return facultyController.getFacultyList(user.getDepartment());
    
    }

    private F360User getUserSession() {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        F360UserController f360UserController = (F360UserController) facesContext.getApplication().getELResolver().getValue(facesContext.getELContext(), null, "f360UserController");
        return f360UserController.getF360User(Integer.parseInt(facesContext.getExternalContext().getRemoteUser()));
    }

    public short getqType() {
        return qType;
    }

    public String getqText() {
        return qText;
    }

    public Integer getqNo() {
        return qNo;
    }

    public F360User getUser() {
        return user;
    }

    @FacesConverter(forClass = F360QuestionPeer.class)
    public static class F360QuestionPeerControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            F360QuestionPeerController controller = (F360QuestionPeerController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "f360QuestionPeerController");
            return controller.getF360QuestionPeer(getKey(value));
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
            if (object instanceof F360QuestionPeer) {
                F360QuestionPeer o = (F360QuestionPeer) object;
                return getStringKey(o.getQid());
            } else {
                throw new IllegalArgumentException("object " + object + " is of type " + object.getClass().getName() + "; expected type: " + F360QuestionPeer.class.getName());
            }
        }
    }
}
