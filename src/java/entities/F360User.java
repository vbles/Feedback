/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package entities;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author Ashish Mathew
 */
@Entity
@Table(name = "f360_user")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "F360User.findAll", query = "SELECT f FROM F360User f"),
    @NamedQuery(name = "F360User.findByUid", query = "SELECT f FROM F360User f WHERE f.uid = :uid"),
    @NamedQuery(name = "F360User.findByPwd", query = "SELECT f FROM F360User f WHERE f.pwd = :pwd"),
    @NamedQuery(name = "F360User.findByDepartment", query = "SELECT f FROM F360User f WHERE f.department = :department"),
    @NamedQuery(name = "F360User.findByTimeFirstLogin", query = "SELECT f FROM F360User f WHERE f.timeFirstLogin = :timeFirstLogin"),
    @NamedQuery(name = "F360User.findByTimeLastLogout", query = "SELECT f FROM F360User f WHERE f.timeLastLogout = :timeLastLogout")})
public class F360User implements Serializable {
    @OneToMany(mappedBy = "uid")
    private List<F360PeerInteraction> f360PeerInteractionList;
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "uid")
    private Integer uid;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "pwd")
    private String pwd;
    @Size(max = 2)
    @Column(name = "department")
    private String department;
    @Column(name = "time_first_login")
    @Temporal(TemporalType.TIMESTAMP)
    private Date timeFirstLogin;
    @Column(name = "time_last_logout")
    @Temporal(TemporalType.TIMESTAMP)
    private Date timeLastLogout;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "uid")
    private List<F360FeedbackPeer> f360FeedbackPeerList;
    @OneToMany(mappedBy = "uid")
    private List<F360FeedbackDepartment> f360FeedbackDepartmentList;
    @OneToMany(mappedBy = "uid")
    private List<F360CommentsDepartment> f360CommentsDepartmentList;
    @OneToMany(mappedBy = "uid")
    private List<F360CommentsPeer> f360CommentsPeerList;

    public F360User() {
    }

    public F360User(Integer uid) {
        this.uid = uid;
    }

    public F360User(Integer uid, String pwd) {
        this.uid = uid;
        this.pwd = pwd;
    }

    public Integer getUid() {
        return uid;
    }

    public void setUid(Integer uid) {
        this.uid = uid;
    }

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public Date getTimeFirstLogin() {
        return timeFirstLogin;
    }

    public void setTimeFirstLogin(Date timeFirstLogin) {
        this.timeFirstLogin = timeFirstLogin;
    }

    public Date getTimeLastLogout() {
        return timeLastLogout;
    }

    public void setTimeLastLogout(Date timeLastLogout) {
        this.timeLastLogout = timeLastLogout;
    }

    @XmlTransient
    public List<F360FeedbackPeer> getF360FeedbackPeerList() {
        return f360FeedbackPeerList;
    }

    public void setF360FeedbackPeerList(List<F360FeedbackPeer> f360FeedbackPeerList) {
        this.f360FeedbackPeerList = f360FeedbackPeerList;
    }

    @XmlTransient
    public List<F360FeedbackDepartment> getF360FeedbackDepartmentList() {
        return f360FeedbackDepartmentList;
    }

    public void setF360FeedbackDepartmentList(List<F360FeedbackDepartment> f360FeedbackDepartmentList) {
        this.f360FeedbackDepartmentList = f360FeedbackDepartmentList;
    }

    @XmlTransient
    public List<F360CommentsDepartment> getF360CommentsDepartmentList() {
        return f360CommentsDepartmentList;
    }

    public void setF360CommentsDepartmentList(List<F360CommentsDepartment> f360CommentsDepartmentList) {
        this.f360CommentsDepartmentList = f360CommentsDepartmentList;
    }

    @XmlTransient
    public List<F360CommentsPeer> getF360CommentsPeerList() {
        return f360CommentsPeerList;
    }

    public void setF360CommentsPeerList(List<F360CommentsPeer> f360CommentsPeerList) {
        this.f360CommentsPeerList = f360CommentsPeerList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (uid != null ? uid.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof F360User)) {
            return false;
        }
        F360User other = (F360User) object;
        if ((this.uid == null && other.uid != null) || (this.uid != null && !this.uid.equals(other.uid))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entities.F360User[ uid=" + uid + " ]";
    }

    @XmlTransient
    public List<F360PeerInteraction> getF360PeerInteractionList() {
        return f360PeerInteractionList;
    }

    public void setF360PeerInteractionList(List<F360PeerInteraction> f360PeerInteractionList) {
        this.f360PeerInteractionList = f360PeerInteractionList;
    }
    
}
