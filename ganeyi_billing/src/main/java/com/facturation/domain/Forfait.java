package com.facturation.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

/**
 * A Forfait.
 */
@Document(collection = "forfait")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Forfait implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private String id;

    @Field("nom")
    private String nom;

    @Field("description")
    private String description;

    @Field("number_of_queries")
    private Double numberOfQueries;

    @Field("price")
    private Double price;

    @Field("periode")
    private String periode;

    @Field("actif")
    private Boolean actif;

    @DBRef
    @Field("request")
    @JsonIgnoreProperties(value = { "resultat", "products", "forfait" }, allowSetters = true)
    private Set<Request> requests = new HashSet<>();

    @DBRef
    private Facture facture;

    @DBRef
    @Field("clients")
    @JsonIgnoreProperties(value = { "factures", "products", "forfaits" }, allowSetters = true)
    private Set<Client> clients = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public String getId() {
        return this.id;
    }

    public Forfait id(String id) {
        this.setId(id);
        return this;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNom() {
        return this.nom;
    }

    public Forfait nom(String nom) {
        this.setNom(nom);
        return this;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getDescription() {
        return this.description;
    }

    public Forfait description(String description) {
        this.setDescription(description);
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Double getNumberOfQueries() {
        return this.numberOfQueries;
    }

    public Forfait numberOfQueries(Double numberOfQueries) {
        this.setNumberOfQueries(numberOfQueries);
        return this;
    }

    public void setNumberOfQueries(Double numberOfQueries) {
        this.numberOfQueries = numberOfQueries;
    }

    public Double getPrice() {
        return this.price;
    }

    public Forfait price(Double price) {
        this.setPrice(price);
        return this;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public String getPeriode() {
        return this.periode;
    }

    public Forfait periode(String periode) {
        this.setPeriode(periode);
        return this;
    }

    public void setPeriode(String periode) {
        this.periode = periode;
    }

    public Boolean getActif() {
        return this.actif;
    }

    public Forfait actif(Boolean actif) {
        this.setActif(actif);
        return this;
    }

    public void setActif(Boolean actif) {
        this.actif = actif;
    }

    public Set<Request> getRequests() {
        return this.requests;
    }

    public void setRequests(Set<Request> requests) {
        if (this.requests != null) {
            this.requests.forEach(i -> i.setForfait(null));
        }
        if (requests != null) {
            requests.forEach(i -> i.setForfait(this));
        }
        this.requests = requests;
    }

    public Forfait requests(Set<Request> requests) {
        this.setRequests(requests);
        return this;
    }

    public Forfait addRequest(Request request) {
        this.requests.add(request);
        request.setForfait(this);
        return this;
    }

    public Forfait removeRequest(Request request) {
        this.requests.remove(request);
        request.setForfait(null);
        return this;
    }

    public Facture getFacture() {
        return this.facture;
    }

    public void setFacture(Facture facture) {
        if (this.facture != null) {
            this.facture.setForfait(null);
        }
        if (facture != null) {
            facture.setForfait(this);
        }
        this.facture = facture;
    }

    public Forfait facture(Facture facture) {
        this.setFacture(facture);
        return this;
    }

    public Set<Client> getClients() {
        return this.clients;
    }

    public void setClients(Set<Client> clients) {
        if (this.clients != null) {
            this.clients.forEach(i -> i.removeForfait(this));
        }
        if (clients != null) {
            clients.forEach(i -> i.addForfait(this));
        }
        this.clients = clients;
    }

    public Forfait clients(Set<Client> clients) {
        this.setClients(clients);
        return this;
    }

    public Forfait addClient(Client client) {
        this.clients.add(client);
        client.getForfaits().add(this);
        return this;
    }

    public Forfait removeClient(Client client) {
        this.clients.remove(client);
        client.getForfaits().remove(this);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Forfait)) {
            return false;
        }
        return id != null && id.equals(((Forfait) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Forfait{" +
            "id=" + getId() +
            ", nom='" + getNom() + "'" +
            ", description='" + getDescription() + "'" +
            ", numberOfQueries=" + getNumberOfQueries() +
            ", price=" + getPrice() +
            ", periode='" + getPeriode() + "'" +
            ", actif='" + getActif() + "'" +
            "}";
    }
}
