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
 * A Product.
 */
@Document(collection = "product")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Product implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private String id;

    @Field("name")
    private String name;

    @Field("description")
    private String description;

    @Field("logo")
    private String logo;

    @Field("is_active")
    private Boolean isActive;

    @DBRef
    @Field("api")
    @JsonIgnoreProperties(value = { "product" }, allowSetters = true)
    private Set<Api> apis = new HashSet<>();

    @DBRef
    @Field("request")
    @JsonIgnoreProperties(value = { "resultat", "products", "forfait" }, allowSetters = true)
    private Request request;

    @DBRef
    @Field("productLicense")
    @JsonIgnoreProperties(value = { "products", "pricings" }, allowSetters = true)
    private ProductLicense productLicense;

    @DBRef
    @Field("clients")
    @JsonIgnoreProperties(value = { "factures", "products", "forfaits" }, allowSetters = true)
    private Set<Client> clients = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public String getId() {
        return this.id;
    }

    public Product id(String id) {
        this.setId(id);
        return this;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public Product name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return this.description;
    }

    public Product description(String description) {
        this.setDescription(description);
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLogo() {
        return this.logo;
    }

    public Product logo(String logo) {
        this.setLogo(logo);
        return this;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public Boolean getIsActive() {
        return this.isActive;
    }

    public Product isActive(Boolean isActive) {
        this.setIsActive(isActive);
        return this;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    public Set<Api> getApis() {
        return this.apis;
    }

    public void setApis(Set<Api> apis) {
        if (this.apis != null) {
            this.apis.forEach(i -> i.setProduct(null));
        }
        if (apis != null) {
            apis.forEach(i -> i.setProduct(this));
        }
        this.apis = apis;
    }

    public Product apis(Set<Api> apis) {
        this.setApis(apis);
        return this;
    }

    public Product addApi(Api api) {
        this.apis.add(api);
        api.setProduct(this);
        return this;
    }

    public Product removeApi(Api api) {
        this.apis.remove(api);
        api.setProduct(null);
        return this;
    }

    public Request getRequest() {
        return this.request;
    }

    public void setRequest(Request request) {
        this.request = request;
    }

    public Product request(Request request) {
        this.setRequest(request);
        return this;
    }

    public ProductLicense getProductLicense() {
        return this.productLicense;
    }

    public void setProductLicense(ProductLicense productLicense) {
        this.productLicense = productLicense;
    }

    public Product productLicense(ProductLicense productLicense) {
        this.setProductLicense(productLicense);
        return this;
    }

    public Set<Client> getClients() {
        return this.clients;
    }

    public void setClients(Set<Client> clients) {
        if (this.clients != null) {
            this.clients.forEach(i -> i.removeProduct(this));
        }
        if (clients != null) {
            clients.forEach(i -> i.addProduct(this));
        }
        this.clients = clients;
    }

    public Product clients(Set<Client> clients) {
        this.setClients(clients);
        return this;
    }

    public Product addClient(Client client) {
        this.clients.add(client);
        client.getProducts().add(this);
        return this;
    }

    public Product removeClient(Client client) {
        this.clients.remove(client);
        client.getProducts().remove(this);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Product)) {
            return false;
        }
        return id != null && id.equals(((Product) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Product{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", description='" + getDescription() + "'" +
            ", logo='" + getLogo() + "'" +
            ", isActive='" + getIsActive() + "'" +
            "}";
    }
}
