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
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Ashish Mathew
 */
@Entity
@Table(name = "f360_feedback_peer")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "F360FeedbackPeer.findAll", query = "SELECT f FROM F360FeedbackPeer f"),
    @NamedQuery(name = "F360FeedbackPeer.findByIdF360FeedbackPeer", query = "SELECT f FROM F360FeedbackPeer f WHERE f.idF360FeedbackPeer = :idF360FeedbackPeer"),
    @NamedQuery(name = "F360FeedbackPeer.findByQid", query = "SELECT f FROM F360FeedbackPeer f WHERE f.qid = :qid"),
    @NamedQuery(name = "F360FeedbackPeer.findByUidQidIdFac", query = "SELECT f FROM F360FeedbackPeer f WHERE f.qid = :qid AND f.uid = :uid AND f.idFaculty = :idFaculty"),
    @NamedQuery(name = "F360FeedbackPeer.findByUidIdFac", query = "SELECT f FROM F360FeedbackPeer f WHERE f.uid = :uid AND f.idFaculty = :idFaculty"),
    @NamedQuery(name = "F360FeedbackPeer.findByIdAns", query = "SELECT f FROM F360FeedbackPeer f WHERE f.idAns = :idAns")})
public class F360FeedbackPeer implements Serializable {
    @JoinColumn(name = "qid", referencedColumnName = "qid")
    @ManyToOne(optional = false)
    private F360QuestionPeer qid;
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id_f360_feedback_peer")
    private Integer idF360FeedbackPeer;
    @Basic(optional = false)
    @NotNull
    @Column(name = "id_ans")
    private short idAns;
    @JoinColumn(name = "id_faculty", referencedColumnName = "id_faculty")
    @ManyToOne
    private Faculty idFaculty;
    @JoinColumn(name = "uid", referencedColumnName = "uid")
    @ManyToOne(optional = false)
    private F360User uid;

    public F360FeedbackPeer() {
    }

    public F360FeedbackPeer(Integer idF360FeedbackPeer) {
        this.idF360FeedbackPeer = idF360FeedbackPeer;
    }


    public Integer getIdF360FeedbackPeer() {
        return idF360FeedbackPeer;
    }

    public void setIdF360FeedbackPeer(Integer idF360FeedbackPeer) {
        this.idF360FeedbackPeer = idF360FeedbackPeer;
    }



    public short getIdAns() {
        return idAns;
    }

    public void setIdAns(short idAns) {
        this.idAns = idAns;
    }

    public Faculty getIdFaculty() {
        return idFaculty;
    }

    public void setIdFaculty(Faculty idFaculty) {
        this.idFaculty = idFaculty;
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
        hash += (idF360FeedbackPeer != null ? idF360FeedbackPeer.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof F360FeedbackPeer)) {
            return false;
        }
        F360FeedbackPeer other = (F360FeedbackPeer) object;
        if ((this.idF360FeedbackPeer == null && other.idF360FeedbackPeer != null) || (this.idF360FeedbackPeer != null && !this.idF360FeedbackPeer.equals(other.idF360FeedbackPeer))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entities.F360FeedbackPeer[ idF360FeedbackPeer=" + idF360FeedbackPeer + " ]";
    }

    public F360QuestionPeer getQid() {
        return qid;
    }

    public void setQid(F360QuestionPeer qid) {
        this.qid = qid;
    }
    
}
