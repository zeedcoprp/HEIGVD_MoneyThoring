package dal.entities.pgsql;

import dal.ientites.IDALCategoryEntity;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "category", schema = "moneythoring", catalog = "moneythoring")
public class CategoryPgEntity implements IDALCategoryEntity {
    private int id;
    private String name;
    private String colour;
    private boolean isdefault;
    private int clientId;

    @Id
    @Column(name = "id", nullable = false)
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Basic
    @Column(name = "name", nullable = false, length = 50)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Basic
    @Column(name = "colour", nullable = false, length = 50)
    public String getColour() {
        return colour;
    }

    public void setColour(String colour) {
        this.colour = colour;
    }

    @Basic
    @Column(name = "isdefault", nullable = false)
    public boolean isIsdefault() {
        return isdefault;
    }

    public void setIsdefault(boolean isdefault) {
        this.isdefault = isdefault;
    }

    @Basic
    @Column(name = "client_id", nullable = false)
    public int getClientId() {
        return clientId;
    }

    public void setClientId(int clientId) {
        this.clientId = clientId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CategoryPgEntity that = (CategoryPgEntity) o;
        return id == that.id &&
                isdefault == that.isdefault &&
                clientId == that.clientId &&
                Objects.equals(name, that.name) &&
                Objects.equals(colour, that.colour);
    }

    @Override
    public int hashCode() {

        return Objects.hash(id, name, colour, isdefault, clientId);
    }
}