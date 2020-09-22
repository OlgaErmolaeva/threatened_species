package io.threatened.species.analyse.model;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.Objects;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class Species {

    private int taxonid;
    private String kingdom_name;
    private String phylum_name;
    private String class_name;
    private String order_name;
    private String family_name;
    private String genus_name;
    private String scientific_name;
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

    public String getGenus_name() {
        return genus_name;
    }

    public void setGenus_name(String genus_name) {
        this.genus_name = genus_name;
    }

    public String getScientific_name() {
        return scientific_name;
    }

    public void setScientific_name(String scientific_name) {
        this.scientific_name = scientific_name;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Species species = (Species) o;
        return taxonid == species.taxonid &&
                Objects.equals(kingdom_name, species.kingdom_name) &&
                Objects.equals(phylum_name, species.phylum_name) &&
                Objects.equals(class_name, species.class_name) &&
                Objects.equals(order_name, species.order_name) &&
                Objects.equals(family_name, species.family_name) &&
                Objects.equals(genus_name, species.genus_name) &&
                Objects.equals(scientific_name, species.scientific_name) &&
                Objects.equals(category, species.category) &&
                Objects.equals(conservationMeasures, species.conservationMeasures);
    }

    @Override
    public int hashCode() {
        return Objects.hash(taxonid, kingdom_name, phylum_name, class_name, order_name, family_name, genus_name, scientific_name, category, conservationMeasures);
    }

    @Override
    public String toString() {
        return "Species{" +
                "taxonid=" + taxonid +
                ", kingdom_name='" + kingdom_name + '\'' +
                ", phylum_name='" + phylum_name + '\'' +
                ", class_name='" + class_name + '\'' +
                ", order_name='" + order_name + '\'' +
                ", family_name='" + family_name + '\'' +
                ", genus_name='" + genus_name + '\'' +
                ", scientific_name='" + scientific_name + '\'' +
                ", category='" + category + '\'' +
                ", conservationMeasures='" + conservationMeasures + '\'' +
                '}';
    }
}
