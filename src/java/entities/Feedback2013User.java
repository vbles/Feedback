/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package entities;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Ashish Mathew
 */
@Entity
@Table(name = "feedback2013_user")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Feedback2013User.findAll", query = "SELECT f FROM Feedback2013User f"),
    @NamedQuery(name = "Feedback2013User.findByUid", query = "SELECT f FROM Feedback2013User f WHERE f.uid = :uid"),
    @NamedQuery(name = "Feedback2013User.findByPwd", query = "SELECT f FROM Feedback2013User f WHERE f.pwd = :pwd")})
public class Feedback2013User implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "uid")
    private Integer uid;
    @Size(max = 255)
    @Column(name = "pwd")
    private String pwd;

    public Feedback2013User() {
    }

    public Feedback2013User(Integer uid) {
        this.uid = uid;
    }

    public Integer getUid() {
        return uid;
    }

    public void setUid(Integer uid) {
        this.uid = uid;
    }

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (uid != null ? uid.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Feedback2013User)) {
            return false;
        }
        Feedback2013User other = (Feedback2013User) object;
        if ((this.uid == null && other.uid != null) || (this.uid != null && !this.uid.equals(other.uid))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entities.Feedback2013User[ uid=" + uid + " ]";
    }
    
}
