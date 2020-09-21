package io.threatened.species.analyse.client;

import io.threatened.species.analyse.model.ConservationMeasure;
import io.threatened.species.analyse.model.ConservationMeasureWrapper;
import io.threatened.species.analyse.model.Region;
import io.threatened.species.analyse.model.RegionWrapper;
import io.threatened.species.analyse.model.Species;
import io.threatened.species.analyse.model.SpeciesWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.List;

@Component
public class RedListClient {

    @Value("${client.token}")
    private String token;


    @Autowired
    private RestTemplate restTemplate;

    public List<Species> getSpeciesByCountry(Integer page, Region randomRegion) {

        if (randomRegion != null) {
            String regionId = randomRegion.getIdentifier();
            String speciesURL = String.format("http://apiv3.iucnredlist.org/api/v3/species/region/%s/page/%s?token=%s", regionId, page, token);
            SpeciesWrapper speciesWrapper = restTemplate.getForObject(speciesURL, SpeciesWrapper.class);

            if (speciesWrapper != null && speciesWrapper.getResult() != null) {
                return speciesWrapper.getResult();
            }
        }
        return Collections.emptyList();
    }

    public Region getRandomCountry() {
        String countryURL = String.format("http://apiv3.iucnredlist.org/api/v3/region/list?token=%s", token);
        RegionWrapper regionWrapper = restTemplate.getForObject(countryURL, RegionWrapper.class);

        Region randomRegion = null;

        if (regionWrapper != null && regionWrapper.getResults() != null && !regionWrapper.getResults().isEmpty()) {
            List<Region> results = regionWrapper.getResults();
            // To take a random country
            Collections.shuffle(results);
            randomRegion = results.get(0);
        }
        return randomRegion;
    }

    public List<ConservationMeasure> getConservationMeasuresById(Integer id) {
        if (id != null) {
            String speciesURL = String.format("http://apiv3.iucnredlist.org/api/v3/measures/species/id/%s?token=%s", id, token);
            ConservationMeasureWrapper measuresWrapper = null;
            try {
                measuresWrapper = restTemplate.getForObject(speciesURL, ConservationMeasureWrapper.class);
            } catch (HttpServerErrorException e) {
                return Collections.emptyList();
            }
            if (measuresWrapper != null && measuresWrapper.getResult() != null) {
                return measuresWrapper.getResult();
            }
        }
        return Collections.emptyList();
    }
}
