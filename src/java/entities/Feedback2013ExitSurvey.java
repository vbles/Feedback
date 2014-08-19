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
@Table(name = "feedback2013_exit_survey")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Feedback2013ExitSurvey.findAll", query = "SELECT f FROM Feedback2013ExitSurvey f"),
    @NamedQuery(name = "Feedback2013ExitSurvey.findByUidSubject", query = "SELECT f FROM Feedback2013ExitSurvey f WHERE f.uid = :uid AND f.idSubjectQuestions.idSubject = :idSubject"),
    @NamedQuery(name = "Feedback2013ExitSurvey.findByUid", query = "SELECT f FROM Feedback2013ExitSurvey f WHERE f.uid = :uid"),
    @NamedQuery(name = "Feedback2013ExitSurvey.findByUidSubjectQ", query = "SELECT f FROM Feedback2013ExitSurvey f WHERE f.uid = :uid AND f.idSubjectQuestions = :idSubjectQuestions"),
    @NamedQuery(name = "Feedback2013ExitSurvey.findByIdFeedback2013exitsurvey", query = "SELECT f FROM Feedback2013ExitSurvey f WHERE f.idFeedback2013exitsurvey = :idFeedback2013exitsurvey"),
    @NamedQuery(name = "Feedback2013ExitSurvey.findByAns", query = "SELECT f FROM Feedback2013ExitSurvey f WHERE f.ans = :ans")})
public class Feedback2013ExitSurvey implements Serializable {
    @JoinColumn(name = "uid", referencedColumnName = "uid")
    @ManyToOne
    private Feedback2013Student uid;
    @JoinColumn(name = "id_subject_questions", referencedColumnName = "id_subject_questions")
    @ManyToOne
    private SubjectQuestion idSubjectQuestions;
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id_Feedback2013_exit_survey")
    private Integer idFeedback2013exitsurvey;
    @Column(name = "ans")
    private Short ans;

    public Feedback2013ExitSurvey() {
    }

    public Feedback2013ExitSurvey(Integer idFeedback2013exitsurvey) {
        this.idFeedback2013exitsurvey = idFeedback2013exitsurvey;
    }

    public Integer getIdFeedback2013exitsurvey() {
        return idFeedback2013exitsurvey;
    }

    public void setIdFeedback2013exitsurvey(Integer idFeedback2013exitsurvey) {
        this.idFeedback2013exitsurvey = idFeedback2013exitsurvey;
    }

    public Short getAns() {
        return ans;
    }

    public void setAns(Short ans) {
        this.ans = ans;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idFeedback2013exitsurvey != null ? idFeedback2013exitsurvey.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Feedback2013ExitSurvey)) {
            return false;
        }
        Feedback2013ExitSurvey other = (Feedback2013ExitSurvey) object;
        if ((this.idFeedback2013exitsurvey == null && other.idFeedback2013exitsurvey != null) || (this.idFeedback2013exitsurvey != null && !this.idFeedback2013exitsurvey.equals(other.idFeedback2013exitsurvey))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entities.Feedback2013ExitSurvey[ idFeedback2013exitsurvey=" + idFeedback2013exitsurvey + " ]";
    }

    public Feedback2013Student getUid() {
        return uid;
    }

    public void setUid(Feedback2013Student uid) {
        this.uid = uid;
    }

    public SubjectQuestion getIdSubjectQuestions() {
        return idSubjectQuestions;
    }

    public void setIdSubjectQuestions(SubjectQuestion idSubjectQuestions) {
        this.idSubjectQuestions = idSubjectQuestions;
    }
    
}
