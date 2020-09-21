package io.threatened.species.analyse.service;

import io.threatened.species.analyse.client.RedListClient;
import io.threatened.species.analyse.model.ConservationMeasure;
import io.threatened.species.analyse.model.Region;
import io.threatened.species.analyse.model.Species;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class SpeciesFilteringService {

    private static final String CRITICALLY_ENDANGERED = "CR";

    @Value("${service.thread.pool.size}")
    private Integer threadPoolSize;

    @Autowired
    private RedListClient redListClient;

    public Set<Species> getSpeciesOfType(String type, Integer page) {
        if (type == null) {
            return Collections.emptySet();
        }

        Region randomCountry = redListClient.getRandomCountry();

        if (randomCountry != null) {
            List<Species> speciesByCountry = redListClient.getSpeciesByCountry(page, randomCountry);
            return speciesByCountry.stream()
                    .filter(species -> type.equalsIgnoreCase(species.getClass_name()))
                    .collect(Collectors.toSet());
        }

        return Collections.emptySet();
    }

    public Set<Species> getCrSpeciesWithConservationMeasures(Integer page) {
        Region randomCountry = redListClient.getRandomCountry();
        List<Species> speciesByCountry = redListClient.getSpeciesByCountry(page, randomCountry);

        Set<Species> crSpecies = speciesByCountry.stream()
                .filter(sp -> CRITICALLY_ENDANGERED.equalsIgnoreCase(sp.getCategory()))
                .collect(Collectors.toSet());


        if (!crSpecies.isEmpty()) {
            for (Species species : crSpecies) {
                // Enrich species object with conservation measures
                List<ConservationMeasure> measures = redListClient.getConservationMeasuresById(species.getTaxonid());

                if (!measures.isEmpty()) {
                    List<String> titleList = measures.stream()
                            .map(ConservationMeasure::getTitle)
                            .collect(Collectors.toList());

                    String titles = String.join(", ", titleList);
                    species.setConservationMeasures(titles);
                }
            }
        }

        return crSpecies;
    }
}
