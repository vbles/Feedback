/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package entities;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
@Table(name = "faculty_subject")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "FacultySubject.findAll", query = "SELECT f FROM FacultySubject f"),
    @NamedQuery(name = "FacultySubject.findByIdFacultySubject", query = "SELECT f FROM FacultySubject f WHERE f.idFacultySubject = :idFacultySubject"),
    @NamedQuery(name = "FacultySubject.findByDivision", query = "SELECT f FROM FacultySubject f WHERE f.division = :division"),
    @NamedQuery(name = "FacultySubject.findByBatch", query = "SELECT f FROM FacultySubject f WHERE f.batch = :batch"),
    @NamedQuery(name = "FacultySubject.findByBatchFS", query = "SELECT f FROM FacultySubject f WHERE f.division = :division AND f.idSubject = :idSubject AND f.batch = :batch AND f.academicYear = :ac_yr"),
    @NamedQuery(name = "FacultySubject.findByAcademicYear", query = "SELECT f FROM FacultySubject f WHERE f.academicYear = :academicYear")})
public class FacultySubject implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id_faculty_subject")
    private Integer idFacultySubject;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 1)
    @Column(name = "division")
    private String division;
    @Basic(optional = false)
    @NotNull
    @Column(name = "batch")
    private short batch;
    @Column(name = "academic_year")
    private Integer academicYear;
    @OneToMany(mappedBy = "idFacultySubject")
    private List<Feedback2013> feedback2013List;
    @JoinColumn(name = "id_subject", referencedColumnName = "id_subject")
    @ManyToOne(optional = false)
    private Subject idSubject;
    @JoinColumn(name = "id_faculty", referencedColumnName = "id_faculty")
    @ManyToOne(optional = false)
    private Faculty idFaculty;
    @OneToMany(mappedBy = "idFacultySubject")
    private List<Feedback2013Comments> feedback2013CommentsList;

    @Transient
    private Map<Integer, Integer> ratings = new HashMap<>();
    @Transient
    private boolean submitted;
    @Transient
    private int rating;
    @Transient
    private int rating2;
    @Transient
    private String theoryComment;
    @Transient
    private String practicalComment;
    
    public FacultySubject() {
    }

    public FacultySubject(Integer idFacultySubject) {
        this.idFacultySubject = idFacultySubject;
    }

    public boolean isSubmitted() {
        return submitted;
    }

    public void setSubmitted(boolean submitted) {
        this.submitted = submitted;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public int getRating2() {
        return rating2;
    }

    public void setRating2(int rating2) {
        this.rating2 = rating2;
    }

    public String getTheoryComment() {
        return theoryComment;
    }

    public void setTheoryComment(String theoryComment) {
        this.theoryComment = theoryComment;
    }

    public String getPracticalComment() {
        return practicalComment;
    }

    public void setPracticalComment(String practicalComment) {
        this.practicalComment = practicalComment;
    }
    
    

    public FacultySubject(Integer idFacultySubject, String division, short batch) {
        this.idFacultySubject = idFacultySubject;
        this.division = division;
        this.batch = batch;
    }

    public Integer getIdFacultySubject() {
        return idFacultySubject;
    }

    public void setIdFacultySubject(Integer idFacultySubject) {
        this.idFacultySubject = idFacultySubject;
    }

    public String getDivision() {
        return division;
    }

    public void setDivision(String division) {
        this.division = division;
    }

    public short getBatch() {
        return batch;
    }

    public void setBatch(short batch) {
        this.batch = batch;
    }

    public Integer getAcademicYear() {
        return academicYear;
    }

    public void setAcademicYear(Integer academicYear) {
        this.academicYear = academicYear;
    }

    @XmlTransient
    public List<Feedback2013> getFeedback2013List() {
        return feedback2013List;
    }

    public void setFeedback2013List(List<Feedback2013> feedback2013List) {
        this.feedback2013List = feedback2013List;
    }

    public Subject getIdSubject() {
        return idSubject;
    }

    public void setIdSubject(Subject idSubject) {
        this.idSubject = idSubject;
    }

    public Faculty getIdFaculty() {
        return idFaculty;
    }

    public void setIdFaculty(Faculty idFaculty) {
        this.idFaculty = idFaculty;
    }

    @XmlTransient
    public List<Feedback2013Comments> getFeedback2013CommentsList() {
        return feedback2013CommentsList;
    }

    public void setFeedback2013CommentsList(List<Feedback2013Comments> feedback2013CommentsList) {
        this.feedback2013CommentsList = feedback2013CommentsList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idFacultySubject != null ? idFacultySubject.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof FacultySubject)) {
            return false;
        }
        FacultySubject other = (FacultySubject) object;
        if ((this.idFacultySubject == null && other.idFacultySubject != null) || (this.idFacultySubject != null && !this.idFacultySubject.equals(other.idFacultySubject))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entities.FacultySubject[ idFacultySubject=" + idFacultySubject + " ]";
    }

    public Map<Integer, Integer> getRatings() {
        return ratings;
    }

    public void setRatings(Map<Integer, Integer> ratings) {
        this.ratings = ratings;
    }
    
}
