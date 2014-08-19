/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package entities;

import java.io.Serializable;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Ashish Mathew
 */
@Entity
@Table(name = "fuser_group")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "FuserGroup.findAll", query = "SELECT f FROM FuserGroup f"),
    @NamedQuery(name = "FuserGroup.findByUserName", query = "SELECT f FROM FuserGroup f WHERE f.fuserGroupPK.userName = :userName"),
    @NamedQuery(name = "FuserGroup.findByRoleName", query = "SELECT f FROM FuserGroup f WHERE f.fuserGroupPK.roleName = :roleName")})
public class FuserGroup implements Serializable {
    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected FuserGroupPK fuserGroupPK;

    public FuserGroup() {
    }

    public FuserGroup(FuserGroupPK fuserGroupPK) {
        this.fuserGroupPK = fuserGroupPK;
    }

    public FuserGroup(String userName, String roleName) {
        this.fuserGroupPK = new FuserGroupPK(userName, roleName);
    }

    public FuserGroupPK getFuserGroupPK() {
        return fuserGroupPK;
    }

    public void setFuserGroupPK(FuserGroupPK fuserGroupPK) {
        this.fuserGroupPK = fuserGroupPK;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (fuserGroupPK != null ? fuserGroupPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof FuserGroup)) {
            return false;
        }
        FuserGroup other = (FuserGroup) object;
        if ((this.fuserGroupPK == null && other.fuserGroupPK != null) || (this.fuserGroupPK != null && !this.fuserGroupPK.equals(other.fuserGroupPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entities.FuserGroup[ fuserGroupPK=" + fuserGroupPK + " ]";
    }
    
}
