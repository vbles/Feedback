/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package entities;

import java.io.Serializable;
import java.util.Collection;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
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
@Table(name = "subject_question")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "SubjectQuestion.findAll", query = "SELECT s FROM SubjectQuestion s"),
    @NamedQuery(name = "SubjectQuestion.findByIdSubjectQuestions", query = "SELECT s FROM SubjectQuestion s WHERE s.idSubjectQuestions = :idSubjectQuestions"),
    @NamedQuery(name = "SubjectQuestion.findByIdSubject", query = "SELECT s FROM SubjectQuestion s WHERE s.idSubject = :idSubject ORDER BY s.qno"),
    @NamedQuery(name = "SubjectQuestion.findByQno", query = "SELECT s FROM SubjectQuestion s WHERE s.qno = :qno")})
public class SubjectQuestion implements Serializable {
    @OneToMany(mappedBy = "idSubjectQuestions")
    private Collection<Feedback2013ExitSurvey> feedback2013ExitSurveyCollection;
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id_subject_questions")
    private Integer idSubjectQuestions;
    @Column(name = "qno")
    private Short qno;
    @Lob
    @Size(max = 65535)
    @Column(name = "qtext")
    private String qtext;
    @JoinColumn(name = "id_subject", referencedColumnName = "id_subject")
    @ManyToOne
    private Subject idSubject;
    @Transient 
    private int rating;

    public SubjectQuestion() {
    }

    public SubjectQuestion(Integer idSubjectQuestions) {
        this.idSubjectQuestions = idSubjectQuestions;
    }

    public Integer getIdSubjectQuestions() {
        return idSubjectQuestions;
    }

    public void setIdSubjectQuestions(Integer idSubjectQuestions) {
        this.idSubjectQuestions = idSubjectQuestions;
    }

    public Short getQno() {
        return qno;
    }

    public void setQno(Short qno) {
        this.qno = qno;
    }

    public String getQtext() {
        return qtext;
    }

    public void setQtext(String qtext) {
        this.qtext = qtext;
    }

    public Subject getIdSubject() {
        return idSubject;
    }

    public void setIdSubject(Subject idSubject) {
        this.idSubject = idSubject;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idSubjectQuestions != null ? idSubjectQuestions.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof SubjectQuestion)) {
            return false;
        }
        SubjectQuestion other = (SubjectQuestion) object;
        if ((this.idSubjectQuestions == null && other.idSubjectQuestions != null) || (this.idSubjectQuestions != null && !this.idSubjectQuestions.equals(other.idSubjectQuestions))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entities.SubjectQuestion[ idSubjectQuestions=" + idSubjectQuestions + " ]";
    }

    @XmlTransient
    public Collection<Feedback2013ExitSurvey> getFeedback2013ExitSurveyCollection() {
        return feedback2013ExitSurveyCollection;
    }

    public void setFeedback2013ExitSurveyCollection(Collection<Feedback2013ExitSurvey> feedback2013ExitSurveyCollection) {
        this.feedback2013ExitSurveyCollection = feedback2013ExitSurveyCollection;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }
    
}
