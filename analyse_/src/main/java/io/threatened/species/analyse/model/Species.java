package io.threatened.species.analyse.model;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class Species {

    private int taxonid;
    private String kingdom_name;
    private String phylum_name;
    private String class_name;
    private String order_name;
    private String family_name;
    private String category;

    private String conservationMeasures;

    public int getTaxonid() {
        return taxonid;
    }

    public void setTaxonid(int taxonid) {
        this.taxonid = taxonid;
    }

    public String getKingdom_name() {
        return kingdom_name;
    }

    public void setKingdom_name(String kingdom_name) {
        this.kingdom_name = kingdom_name;
    }

    public String getPhylum_name() {
        return phylum_name;
    }

    public void setPhylum_name(String phylum_name) {
        this.phylum_name = phylum_name;
    }

    public String getClass_name() {
        return class_name;
    }

    public void setClass_name(String class_name) {
        this.class_name = class_name;
    }

    public String getOrder_name() {
        return order_name;
    }

    public void setOrder_name(String order_name) {
        this.order_name = order_name;
    }

    public String getFamily_name() {
        return family_name;
    }

    public void setFamily_name(String family_name) {
        this.family_name = family_name;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getConservationMeasures() {
        return conservationMeasures;
    }

    public void setConservationMeasures(String conservationMeasures) {
        this.conservationMeasures = conservationMeasures;
    }
}
