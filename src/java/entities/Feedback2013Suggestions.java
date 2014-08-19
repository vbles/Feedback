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
import javax.persistence.Lob;
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
@Table(name = "feedback2013_suggestions")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Feedback2013Suggestions.findAll", query = "SELECT f FROM Feedback2013Suggestions f"),
    @NamedQuery(name = "Feedback2013Suggestions.findById", query = "SELECT f FROM Feedback2013Suggestions f WHERE f.id = :id"),
    @NamedQuery(name = "Feedback2013Suggestions.findByUid", query = "SELECT f FROM Feedback2013Suggestions f WHERE f.uid = :uid")})
public class Feedback2013Suggestions implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Column(name = "uid")
    private Integer uid;
    @Lob
    @Size(max = 16777215)
    @Column(name = "suggestions")
    private String suggestions;

    public Feedback2013Suggestions() {
    }

    public Feedback2013Suggestions(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getUid() {
        return uid;
    }

    public void setUid(Integer uid) {
        this.uid = uid;
    }

    public String getSuggestions() {
        return suggestions;
    }

    public void setSuggestions(String suggestions) {
        this.suggestions = suggestions;
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
        if (!(object instanceof Feedback2013Suggestions)) {
            return false;
        }
        Feedback2013Suggestions other = (Feedback2013Suggestions) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entities.Feedback2013Suggestions[ id=" + id + " ]";
    }
    
}
