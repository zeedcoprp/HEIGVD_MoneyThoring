package dal.entities.pgsql;

import dal.ientites.IDALDebtEntity;

import javax.persistence.*;
import java.sql.Date;
import java.util.Objects;

@Entity
@Table(name = "debt", schema = "moneythoring", catalog = "moneythoring")
public class DebtPgEntity implements IDALDebtEntity {
    private int id;
    private String name;
    private String description;
    private double amount;
    private boolean isincome;
    private Date expirationdate;
    private int clientId;
    private Integer clientId1;

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
    @Column(name = "description", nullable = true, length = 255)
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Basic
    @Column(name = "amount", nullable = false, precision = 0)
    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    @Basic
    @Column(name = "isincome", nullable = false)
    public boolean isIsincome() {
        return isincome;
    }

    public void setIsincome(boolean isincome) {
        this.isincome = isincome;
    }

    @Basic
    @Column(name = "expirationdate", nullable = false)
    public Date getExpirationdate() {
        return expirationdate;
    }

    public void setExpirationdate(Date expirationdate) {
        this.expirationdate = expirationdate;
    }

    @Basic
    @Column(name = "client_id", nullable = false)
    public int getClientId() {
        return clientId;
    }

    public void setClientId(int clientId) {
        this.clientId = clientId;
    }

    @Basic
    @Column(name = "client_id1", nullable = true)
    public Integer getClientId1() {
        return clientId1;
    }

    public void setClientId1(Integer clientId1) {
        this.clientId1 = clientId1;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DebtPgEntity that = (DebtPgEntity) o;
        return id == that.id &&
                Double.compare(that.amount, amount) == 0 &&
                isincome == that.isincome &&
                clientId == that.clientId &&
                Objects.equals(name, that.name) &&
                Objects.equals(description, that.description) &&
                Objects.equals(expirationdate, that.expirationdate) &&
                Objects.equals(clientId1, that.clientId1);
    }

    @Override
    public int hashCode() {

        return Objects.hash(id, name, description, amount, isincome, expirationdate, clientId, clientId1);
    }
}
