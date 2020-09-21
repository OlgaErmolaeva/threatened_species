package io.threatened.species.analyse.rest;

import io.threatened.species.analyse.model.Species;
import io.threatened.species.analyse.service.SpeciesFilteringService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Set;

@RestController
public class SpeciesAnalyseController {

    @Value("${client.page}")
    private Integer defaultPage;

    @Autowired
    private SpeciesFilteringService filteringService;

    // Parameters to methods added to make endpoints possible to test

    @GetMapping("/cr/conservation-measures")
    public Set<Species> getConservationMeasuresForCR(@RequestParam(required = false) Integer page) {
        page = page == null ? defaultPage : page;
        return filteringService.getCrSpeciesWithConservationMeasures(page);
    }

    @GetMapping("/mammals")
    public Set<Species> getMammals(@RequestParam(required = false) Integer page, @RequestParam(required = false) String type) {
        // Type added as parameter since MAMMALIA does not present in some regions at all or not on the first page
        page = page == null ? defaultPage : page;
        String filteringType = type == null ? "MAMMALIA" : type;

        return filteringService.getSpeciesOfType(filteringType, page);
    }
}
