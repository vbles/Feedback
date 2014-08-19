/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package entities;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Ashish Mathew
 */
@Entity
@Table(name = "f360_feedback_department")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "F360FeedbackDepartment.findAll", query = "SELECT f FROM F360FeedbackDepartment f"),
    @NamedQuery(name = "F360FeedbackDepartment.findById", query = "SELECT f FROM F360FeedbackDepartment f WHERE f.id = :id"),
    @NamedQuery(name = "F360FeedbackDepartment.findByUid", query = "SELECT f FROM F360FeedbackDepartment f WHERE f.uid = :uid"),
    @NamedQuery(name = "F360FeedbackDepartment.findByUidQid", query = "SELECT f FROM F360FeedbackDepartment f WHERE f.uid = :uid AND f.qid = :qid"),
    @NamedQuery(name = "F360FeedbackDepartment.findByQid", query = "SELECT f FROM F360FeedbackDepartment f WHERE f.qid = :qid"),
    @NamedQuery(name = "F360FeedbackDepartment.findByIdAns", query = "SELECT f FROM F360FeedbackDepartment f WHERE f.idAns = :idAns")})
public class F360FeedbackDepartment implements Serializable {
    @JoinColumn(name = "qid", referencedColumnName = "qid")
    @ManyToOne
    private F360QuestionDepartment qid;
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Column(name = "id_ans")
    private Short idAns;
    @JoinColumn(name = "uid", referencedColumnName = "uid")
    @ManyToOne
    private F360User uid;

    public F360FeedbackDepartment() {
    }

    public F360FeedbackDepartment(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }


    public Short getIdAns() {
        return idAns;
    }

    public void setIdAns(int idAns) {
        this.idAns = (short) idAns;
    }

    public F360User getUid() {
        return uid;
    }

    public void setUid(F360User uid) {
        this.uid = uid;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof F360FeedbackDepartment)) {
            return false;
        }
        F360FeedbackDepartment other = (F360FeedbackDepartment) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entities.F360FeedbackDepartment[ id=" + id + " ]";
    }

    public F360QuestionDepartment getQid() {
        return qid;
    }

    public void setQid(F360QuestionDepartment qid) {
        this.qid = qid;
    }
    
}
