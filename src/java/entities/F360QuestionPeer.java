/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package entities;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
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
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author Ashish Mathew
 */
@Entity
@Table(name = "f360_question_peer")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "F360QuestionPeer.findAll", query = "SELECT f FROM F360QuestionPeer f"),
    @NamedQuery(name = "F360QuestionPeer.findByQid", query = "SELECT f FROM F360QuestionPeer f WHERE f.qid = :qid"),
    @NamedQuery(name = "F360QuestionPeer.findByQtext", query = "SELECT f FROM F360QuestionPeer f WHERE f.qtext = :qtext"),
    @NamedQuery(name = "F360QuestionPeer.findByQtype", query = "SELECT f FROM F360QuestionPeer f WHERE f.qtype = :qtype")})
public class F360QuestionPeer implements Serializable {
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "qid")
    private List<F360FeedbackPeer> f360FeedbackPeerList;
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "qid")
    private Integer qid;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 500)
    @Column(name = "qtext")
    private String qtext;
    @Basic(optional = false)
    @NotNull
    @Column(name = "qtype")
    private short qtype;
    @Transient
    private int rating;
        
    public F360QuestionPeer() {
    }

    public F360QuestionPeer(Integer qid) {
        this.qid = qid;
    }

    public F360QuestionPeer(Integer qid, String qtext, short qtype) {
        this.qid = qid;
        this.qtext = qtext;
        this.qtype = qtype;
    }

    public Integer getQid() {
        return qid;
    }

    public void setQid(Integer qid) {
        this.qid = qid;
    }

    public String getQtext() {
        return qtext;
    }

    public void setQtext(String qtext) {
        this.qtext = qtext;
    }

    public short getQtype() {
        return qtype;
    }

    public void setQtype(short qtype) {
        this.qtype = qtype;
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
        if (!(object instanceof F360QuestionPeer)) {
            return false;
        }
        F360QuestionPeer other = (F360QuestionPeer) object;
        if ((this.qid == null && other.qid != null) || (this.qid != null && !this.qid.equals(other.qid))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entities.F360QuestionPeer[ qid=" + qid + " ]";
    }

    @XmlTransient
    public List<F360FeedbackPeer> getF360FeedbackPeerList() {
        return f360FeedbackPeerList;
    }

    public void setF360FeedbackPeerList(List<F360FeedbackPeer> f360FeedbackPeerList) {
        this.f360FeedbackPeerList = f360FeedbackPeerList;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }
    
}
