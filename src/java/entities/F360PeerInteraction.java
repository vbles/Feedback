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
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Ashish Mathew
 */
@Entity
@Table(name = "f360_peer_interaction")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "F360PeerInteraction.findAll", query = "SELECT f FROM F360PeerInteraction f"),
    @NamedQuery(name = "F360PeerInteraction.findByUidIdFac", query = "SELECT f FROM F360PeerInteraction f WHERE f.idFaculty = :idFaculty AND f.uid = :uid"),
    @NamedQuery(name = "F360PeerInteraction.findByIdF360PeerInteraction", query = "SELECT f FROM F360PeerInteraction f WHERE f.idF360PeerInteraction = :idF360PeerInteraction")})
public class F360PeerInteraction implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id_f360_peer_interaction")
    private Integer idF360PeerInteraction;
    @JoinColumn(name = "id_faculty", referencedColumnName = "id_faculty")
    @ManyToOne
    private Faculty idFaculty;
    @JoinColumn(name = "uid", referencedColumnName = "uid")
    @ManyToOne
    private F360User uid;

    public F360PeerInteraction() {
    }

    public F360PeerInteraction(Integer idF360PeerInteraction) {
        this.idF360PeerInteraction = idF360PeerInteraction;
    }

    public Integer getIdF360PeerInteraction() {
        return idF360PeerInteraction;
    }

    public void setIdF360PeerInteraction(Integer idF360PeerInteraction) {
        this.idF360PeerInteraction = idF360PeerInteraction;
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
        hash += (idF360PeerInteraction != null ? idF360PeerInteraction.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof F360PeerInteraction)) {
            return false;
        }
        F360PeerInteraction other = (F360PeerInteraction) object;
        if ((this.idF360PeerInteraction == null && other.idF360PeerInteraction != null) || (this.idF360PeerInteraction != null && !this.idF360PeerInteraction.equals(other.idF360PeerInteraction))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entities.F360PeerInteraction[ idF360PeerInteraction=" + idF360PeerInteraction + " ]";
    }
}
