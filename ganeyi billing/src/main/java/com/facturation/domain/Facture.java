package com.facturation.domain;

import com.facturation.domain.enumeration.FactureStatus;
import com.facturation.domain.enumeration.TypeFacturation;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.LocalDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

/**
 * A Facture.
 */
@Document(collection = "facture")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Facture implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private String id;

    @Field("rabais")
    private Double rabais;

    @Field("tva")
    private Double tva;

    @Field("sous_total")
    private Double sousTotal;

    @Field("total")
    private Double total;

    @Field("type_facturation")
    private TypeFacturation typeFacturation;

    @Field("status")
    private FactureStatus status;

    @Field("reference")
    private String reference;

    @Field("date")
    private LocalDate date;

    @DBRef
    @Field("forfait")
    private Forfait forfait;

    @DBRef
    private Payment payment;

    @DBRef
    @Field("client")
    @JsonIgnoreProperties(value = { "factures", "products", "forfaits" }, allowSetters = true)
    private Client client;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public String getId() {
        return this.id;
    }

    public Facture id(String id) {
        this.setId(id);
        return this;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Double getRabais() {
        return this.rabais;
    }

    public Facture rabais(Double rabais) {
        this.setRabais(rabais);
        return this;
    }

    public void setRabais(Double rabais) {
        this.rabais = rabais;
    }

    public Double getTva() {
        return this.tva;
    }

    public Facture tva(Double tva) {
        this.setTva(tva);
        return this;
    }

    public void setTva(Double tva) {
        this.tva = tva;
    }

    public Double getSousTotal() {
        return this.sousTotal;
    }

    public Facture sousTotal(Double sousTotal) {
        this.setSousTotal(sousTotal);
        return this;
    }

    public void setSousTotal(Double sousTotal) {
        this.sousTotal = sousTotal;
    }

    public Double getTotal() {
        return this.total;
    }

    public Facture total(Double total) {
        this.setTotal(total);
        return this;
    }

    public void setTotal(Double total) {
        this.total = total;
    }

    public TypeFacturation getTypeFacturation() {
        return this.typeFacturation;
    }

    public Facture typeFacturation(TypeFacturation typeFacturation) {
        this.setTypeFacturation(typeFacturation);
        return this;
    }

    public void setTypeFacturation(TypeFacturation typeFacturation) {
        this.typeFacturation = typeFacturation;
    }

    public FactureStatus getStatus() {
        return this.status;
    }

    public Facture status(FactureStatus status) {
        this.setStatus(status);
        return this;
    }

    public void setStatus(FactureStatus status) {
        this.status = status;
    }

    public String getReference() {
        return this.reference;
    }

    public Facture reference(String reference) {
        this.setReference(reference);
        return this;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public LocalDate getDate() {
        return this.date;
    }

    public Facture date(LocalDate date) {
        this.setDate(date);
        return this;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public Forfait getForfait() {
        return this.forfait;
    }

    public void setForfait(Forfait forfait) {
        this.forfait = forfait;
    }

    public Facture forfait(Forfait forfait) {
        this.setForfait(forfait);
        return this;
    }

    public Payment getPayment() {
        return this.payment;
    }

    public void setPayment(Payment payment) {
        if (this.payment != null) {
            this.payment.setFacture(null);
        }
        if (payment != null) {
            payment.setFacture(this);
        }
        this.payment = payment;
    }

    public Facture payment(Payment payment) {
        this.setPayment(payment);
        return this;
    }

    public Client getClient() {
        return this.client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public Facture client(Client client) {
        this.setClient(client);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Facture)) {
            return false;
        }
        return id != null && id.equals(((Facture) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Facture{" +
            "id=" + getId() +
            ", rabais=" + getRabais() +
            ", tva=" + getTva() +
            ", sousTotal=" + getSousTotal() +
            ", total=" + getTotal() +
            ", typeFacturation='" + getTypeFacturation() + "'" +
            ", status='" + getStatus() + "'" +
            ", reference='" + getReference() + "'" +
            ", date='" + getDate() + "'" +
            "}";
    }
}
