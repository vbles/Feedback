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
@Table(name = "f360_comments_department")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "F360CommentsDepartment.findAll", query = "SELECT f FROM F360CommentsDepartment f"),
    @NamedQuery(name = "F360CommentsDepartment.findByUid", query = "SELECT f FROM F360CommentsDepartment f WHERE f.uid = :uid"),
    @NamedQuery(name = "F360CommentsDepartment.findById", query = "SELECT f FROM F360CommentsDepartment f WHERE f.id = :id")})
public class F360CommentsDepartment implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Lob
    @Size(max = 65535)
    @Column(name = "comments")
    private String comments;
    @JoinColumn(name = "uid", referencedColumnName = "uid")
    @ManyToOne
    private F360User uid;

    public F360CommentsDepartment() {
    }

    public F360CommentsDepartment(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
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
        if (!(object instanceof F360CommentsDepartment)) {
            return false;
        }
        F360CommentsDepartment other = (F360CommentsDepartment) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entities.F360CommentsDepartment[ id=" + id + " ]";
    }
    
}
