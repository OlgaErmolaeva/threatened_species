package io.threatened.species.analyse.model;

import java.util.List;

public class SpeciesWrapper {
    private int count;
    private String region_identifier;
    private List<Species> result;

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public String getRegion_identifier() {
        return region_identifier;
    }

    public void setRegion_identifier(String region_identifier) {
        this.region_identifier = region_identifier;
    }

    public List<Species> getResult() {
        return result;
    }

    public void setResult(List<Species> result) {
        this.result = result;
    }
}
