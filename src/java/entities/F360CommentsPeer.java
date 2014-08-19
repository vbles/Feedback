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
@Table(name = "f360_comments_peer")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "F360CommentsPeer.findAll", query = "SELECT f FROM F360CommentsPeer f"),
    @NamedQuery(name = "F360CommentsPeer.findByUidIdFac", query = "SELECT f FROM F360CommentsPeer f WHERE f.idFaculty = :idFaculty AND f.uid = :uid"),
    @NamedQuery(name = "F360CommentsPeer.findById", query = "SELECT f FROM F360CommentsPeer f WHERE f.id = :id")})
public class F360CommentsPeer implements Serializable {
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
    @JoinColumn(name = "id_faculty", referencedColumnName = "id_faculty")
    @ManyToOne(optional = false)
    private Faculty idFaculty;
    @JoinColumn(name = "uid", referencedColumnName = "uid")
    @ManyToOne
    private F360User uid;

    public F360CommentsPeer() {
    }

    public F360CommentsPeer(Integer id) {
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
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof F360CommentsPeer)) {
            return false;
        }
        F360CommentsPeer other = (F360CommentsPeer) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entities.F360CommentsPeer[ id=" + id + " ]";
    }
    
}
