package com.facturation.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

/**
 * A Resultat.
 */
@Document(collection = "resultat")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Resultat implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private String id;

    @Field("value")
    private byte[] value;

    @Field("value_content_type")
    private String valueContentType;

    @DBRef
    private Request request;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public String getId() {
        return this.id;
    }

    public Resultat id(String id) {
        this.setId(id);
        return this;
    }

    public void setId(String id) {
        this.id = id;
    }

    public byte[] getValue() {
        return this.value;
    }

    public Resultat value(byte[] value) {
        this.setValue(value);
        return this;
    }

    public void setValue(byte[] value) {
        this.value = value;
    }

    public String getValueContentType() {
        return this.valueContentType;
    }

    public Resultat valueContentType(String valueContentType) {
        this.valueContentType = valueContentType;
        return this;
    }

    public void setValueContentType(String valueContentType) {
        this.valueContentType = valueContentType;
    }

    public Request getRequest() {
        return this.request;
    }

    public void setRequest(Request request) {
        if (this.request != null) {
            this.request.setResultat(null);
        }
        if (request != null) {
            request.setResultat(this);
        }
        this.request = request;
    }

    public Resultat request(Request request) {
        this.setRequest(request);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Resultat)) {
            return false;
        }
        return id != null && id.equals(((Resultat) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Resultat{" +
            "id=" + getId() +
            ", value='" + getValue() + "'" +
            ", valueContentType='" + getValueContentType() + "'" +
            "}";
    }
}
