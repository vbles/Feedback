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
@Table(name = "f360_quetion_types")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "F360QuetionTypes.findAll", query = "SELECT f FROM F360QuetionTypes f"),
    @NamedQuery(name = "F360QuetionTypes.findByIdType", query = "SELECT f FROM F360QuetionTypes f WHERE f.idType = :idType"),
    @NamedQuery(name = "F360QuetionTypes.findByTypeText", query = "SELECT f FROM F360QuetionTypes f WHERE f.typeText = :typeText")})
public class F360QuetionTypes implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "id_type")
    private Short idType;
    @Size(max = 100)
    @Column(name = "type_text")
    private String typeText;

    public F360QuetionTypes() {
    }

    public F360QuetionTypes(Short idType) {
        this.idType = idType;
    }

    public Short getIdType() {
        return idType;
    }

    public void setIdType(Short idType) {
        this.idType = idType;
    }

    public String getTypeText() {
        return typeText;
    }

    public void setTypeText(String typeText) {
        this.typeText = typeText;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idType != null ? idType.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof F360QuetionTypes)) {
            return false;
        }
        F360QuetionTypes other = (F360QuetionTypes) object;
        if ((this.idType == null && other.idType != null) || (this.idType != null && !this.idType.equals(other.idType))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entities.F360QuetionTypes[ idType=" + idType + " ]";
    }
    
}
