/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package entities;

import java.io.Serializable;
import java.util.Collection;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author Ashish Mathew
 */
@Entity
@Table(name = "f360_question_department")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "F360QuestionDepartment.findAll", query = "SELECT f FROM F360QuestionDepartment f"),
    @NamedQuery(name = "F360QuestionDepartment.findByQid", query = "SELECT f FROM F360QuestionDepartment f WHERE f.qid = :qid"),
    @NamedQuery(name = "F360QuestionDepartment.findByQtext", query = "SELECT f FROM F360QuestionDepartment f WHERE f.qtext = :qtext")})
public class F360QuestionDepartment implements Serializable {
    @OneToMany(mappedBy = "qid")
    private Collection<F360FeedbackDepartment> f360FeedbackDepartmentCollection;
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "qid")
    private Short qid;
    @Size(max = 200)
    @Column(name = "qtext")
    private String qtext;
    @Transient
    private int rating;

    public F360QuestionDepartment() {
    }

    public F360QuestionDepartment(Short qid) {
        this.qid = qid;
    }

    public Short getQid() {
        return qid;
    }

    public void setQid(Short qid) {
        this.qid = qid;
    }

    public String getQtext() {
        return qtext;
    }

    public void setQtext(String qtext) {
        this.qtext = qtext;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (qid != null ? qid.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof F360QuestionDepartment)) {
            return false;
        }
        F360QuestionDepartment other = (F360QuestionDepartment) object;
        if ((this.qid == null && other.qid != null) || (this.qid != null && !this.qid.equals(other.qid))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entities.F360QuestionDepartment[ qid=" + qid + " ]";
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    @XmlTransient
    public Collection<F360FeedbackDepartment> getF360FeedbackDepartmentCollection() {
        return f360FeedbackDepartmentCollection;
    }

    public void setF360FeedbackDepartmentCollection(Collection<F360FeedbackDepartment> f360FeedbackDepartmentCollection) {
        this.f360FeedbackDepartmentCollection = f360FeedbackDepartmentCollection;
    }
    
}
