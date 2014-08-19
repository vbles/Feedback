/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package entities;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author Ashish Mathew
 */
@Embeddable
public class FuserGroupPK implements Serializable {
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "user_name")
    private String userName;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "role_name")
    private String roleName;

    public FuserGroupPK() {
    }

    public FuserGroupPK(String userName, String roleName) {
        this.userName = userName;
        this.roleName = roleName;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (userName != null ? userName.hashCode() : 0);
        hash += (roleName != null ? roleName.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof FuserGroupPK)) {
            return false;
        }
        FuserGroupPK other = (FuserGroupPK) object;
        if ((this.userName == null && other.userName != null) || (this.userName != null && !this.userName.equals(other.userName))) {
            return false;
        }
        if ((this.roleName == null && other.roleName != null) || (this.roleName != null && !this.roleName.equals(other.roleName))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entities.FuserGroupPK[ userName=" + userName + ", roleName=" + roleName + " ]";
    }
    
}
