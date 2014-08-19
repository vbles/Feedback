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
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Ashish Mathew
 */
@Entity
@Table(name = "subject_objective")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "SubjectObjective.findAll", query = "SELECT s FROM SubjectObjective s"),
    @NamedQuery(name = "SubjectObjective.findByIdSubject", query = "SELECT s FROM SubjectObjective s WHERE s.idSubject = :idSubject"),
    @NamedQuery(name = "SubjectObjective.findByIdSubjectObjective", query = "SELECT s FROM SubjectObjective s WHERE s.idSubjectObjective = :idSubjectObjective")})
public class SubjectObjective implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id_subject_objective")
    private Integer idSubjectObjective;
    @Lob
    @Size(max = 65535)
    @Column(name = "objective")
    private String objective;
    @JoinColumn(name = "id_subject", referencedColumnName = "id_subject")
    @ManyToOne(optional = false)
    private Subject idSubject;

    public SubjectObjective() {
    }

    public SubjectObjective(Integer idSubjectObjective) {
        this.idSubjectObjective = idSubjectObjective;
    }

    public Integer getIdSubjectObjective() {
        return idSubjectObjective;
    }

    public void setIdSubjectObjective(Integer idSubjectObjective) {
        this.idSubjectObjective = idSubjectObjective;
    }

    public String getObjective() {
        return objective;
    }

    public void setObjective(String objective) {
        this.objective = objective;
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
        hash += (idSubjectObjective != null ? idSubjectObjective.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof SubjectObjective)) {
            return false;
        }
        SubjectObjective other = (SubjectObjective) object;
        if ((this.idSubjectObjective == null && other.idSubjectObjective != null) || (this.idSubjectObjective != null && !this.idSubjectObjective.equals(other.idSubjectObjective))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entities.SubjectObjective[ idSubjectObjective=" + idSubjectObjective + " ]";
    }
    
}
