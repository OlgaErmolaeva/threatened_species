package io.threatened.species.analyse.service;

import io.threatened.species.analyse.client.RedListClient;
import io.threatened.species.analyse.model.ConservationMeasure;
import io.threatened.species.analyse.model.Region;
import io.threatened.species.analyse.model.Species;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.stream.Collectors;

import static org.slf4j.LoggerFactory.getLogger;

@Service
public class SpeciesFilteringService {

    private static final String CRITICALLY_ENDANGERED = "CR";
    private static Logger logger = getLogger(SpeciesFilteringService.class);
    private ExecutorService executorService;

    @Value("${service.thread.pool.size}")
    private Integer threadPoolSize;

    @PostConstruct
    public void setUpThreadPool() {
        executorService = Executors.newFixedThreadPool(threadPoolSize);
    }

    @Autowired
    private RedListClient redListClient;

    public Set<Species> getSpeciesOfType(String type, Integer page) {
        if (type == null) {
            return Collections.emptySet();
        }

        Region randomCountry = redListClient.getRandomRegion();

        if (randomCountry != null) {
            List<Species> speciesByCountry = redListClient.getSpeciesByRegion(page, randomCountry);
            return speciesByCountry.stream()
                    .filter(species -> type.equalsIgnoreCase(species.getClass_name()))
                    .collect(Collectors.toSet());
        }

        return Collections.emptySet();
    }

    public Set<Species> getCrSpeciesWithConservationMeasures(Integer page) {
        Region randomCountry = redListClient.getRandomRegion();
        List<Species> speciesByCountry = redListClient.getSpeciesByRegion(page, randomCountry);

        Set<Species> crSpecies = speciesByCountry.stream()
                .filter(sp -> CRITICALLY_ENDANGERED.equalsIgnoreCase(sp.getCategory()))
                .collect(Collectors.toSet());

        List<Future<Species>> futures = new ArrayList<>();

        if (!crSpecies.isEmpty()) {
            for (Species species : crSpecies) {
                futures.add(executorService.submit(() -> enrichSpeciesWithMeasures(species)));
            }
        }

        futures.forEach(future -> {
            try {
                future.get();
            } catch (InterruptedException | ExecutionException e) {
                logger.error("Error retrieving conservation measures.",e );
            }
        });

        return crSpecies;
    }

    private Species enrichSpeciesWithMeasures(Species species) {
        // Enrich species object with conservation measures
        List<ConservationMeasure> measures = redListClient.getConservationMeasuresById(species.getTaxonid());

        if (!measures.isEmpty()) {
            List<String> titleList = measures.stream()
                    .map(ConservationMeasure::getTitle)
                    .collect(Collectors.toList());

            String titles = String.join(", ", titleList);
            species.setConservationMeasures(titles);
        }
        return species;
    }
}
