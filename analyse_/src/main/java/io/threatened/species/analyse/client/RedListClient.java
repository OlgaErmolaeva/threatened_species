package io.threatened.species.analyse.client;

import io.threatened.species.analyse.model.ConservationMeasure;
import io.threatened.species.analyse.model.ConservationMeasureWrapper;
import io.threatened.species.analyse.model.Region;
import io.threatened.species.analyse.model.RegionWrapper;
import io.threatened.species.analyse.model.Species;
import io.threatened.species.analyse.model.SpeciesWrapper;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.List;

import static org.slf4j.LoggerFactory.getLogger;

@Component
public class RedListClient {

    private static Logger logger = getLogger(RedListClient.class);

    @Value("${client.token}")
    private String token;


    @Autowired
    private RestTemplate restTemplate;

    public List<Species> getSpeciesByRegion(Integer page, Region randomRegion) {

        if (randomRegion != null) {
            String regionId = randomRegion.getIdentifier();
            String speciesURL = String.format("http://apiv3.iucnredlist.org/api/v3/species/region/%s/page/%s?token=%s", regionId, page, token);
            SpeciesWrapper speciesWrapper;

            try {
                speciesWrapper = restTemplate.getForObject(speciesURL, SpeciesWrapper.class);
            } catch (HttpServerErrorException e) {
                logger.error("Error retrieving Species by region for {}.", regionId);
                return Collections.emptyList();
            }
            if (speciesWrapper != null && speciesWrapper.getResult() != null) {
                return speciesWrapper.getResult();
            }
        }
        return Collections.emptyList();
    }

    public Region getRandomRegion() {
        String countryURL = String.format("http://apiv3.iucnredlist.org/api/v3/region/list?token=%s", token);
        Region randomRegion = null;
        RegionWrapper regionWrapper;

        try {
            regionWrapper = restTemplate.getForObject(countryURL, RegionWrapper.class);
        } catch (HttpServerErrorException e) {
            logger.error("Error retrieving region.");
            return randomRegion;
        }

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
            ConservationMeasureWrapper measuresWrapper;
            try {
                measuresWrapper = restTemplate.getForObject(speciesURL, ConservationMeasureWrapper.class);
            } catch (HttpServerErrorException e) {
                logger.error("Error retrieving ConservationMeasures for {}", id);
                return Collections.emptyList();
            }
            if (measuresWrapper != null && measuresWrapper.getResult() != null) {
                return measuresWrapper.getResult();
            }
        }
        return Collections.emptyList();
    }
}
