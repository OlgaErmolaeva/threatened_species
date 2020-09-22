package io.threatened.species.analyse;

import io.threatened.species.analyse.model.Species;
import io.threatened.species.analyse.service.SpeciesFilteringService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.cloud.contract.wiremock.AutoConfigureWireMock;

import java.util.Set;

import static com.github.tomakehurst.wiremock.client.WireMock.*;

@SpringBootTest()
@AutoConfigureWireMock(port = 0)
class AnalyseThreatenedSpeciesApplicationTests {

    @Autowired
    private SpeciesFilteringService serviceUnderTest;

    @Value("${client.token}")
    private String token;

    @BeforeEach
    public void setUp() {
        stubFor(get(urlPathEqualTo("/api/v3/region/list"))
                .withQueryParam("token", equalTo(token))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBodyFile("regions.json")));

        stubFor(get(urlPathEqualTo("/api/v3/species/region/europe/page/0")) // europe is a region coming from getRandomRegion response from regions.json
                .withQueryParam("token", equalTo(token))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBodyFile("speciesByRegion.json")));
    }

    @Test
    public void getMammalsTest() {
        // GIVEN
        Species mammalia_NT = new Species();
        mammalia_NT.setKingdom_name("ANIMALIA");
        mammalia_NT.setTaxonid(857);
        mammalia_NT.setPhylum_name("CHORDATA");
        mammalia_NT.setClass_name("MAMMALIA");
        mammalia_NT.setFamily_name("DIPODIDAE");
        mammalia_NT.setOrder_name("RODENTIA");
        mammalia_NT.setGenus_name("Allactaga");
        mammalia_NT.setScientific_name("Allactaga major");
        mammalia_NT.setCategory("NT");

        Species mammalia_CR = new Species();
        mammalia_CR.setKingdom_name("ANIMALIA");
        mammalia_CR.setTaxonid(875);
        mammalia_CR.setPhylum_name("CHORDATA");
        mammalia_CR.setClass_name("MAMMALIA");
        mammalia_CR.setFamily_name("CRICETIDAE");
        mammalia_CR.setOrder_name("RODENTIA");
        mammalia_CR.setGenus_name("Allocricetulus");
        mammalia_CR.setScientific_name("Allocricetulus eversmanni");
        mammalia_CR.setCategory("CR");

        Set<Species> expected = Set.of(mammalia_NT, mammalia_CR);

        // WHEN
        Set<Species> mammalia = serviceUnderTest.getSpeciesOfType("MAMMALIA", 0);

        // THEN
        Assertions.assertEquals(expected, mammalia, "Mammals species are not equls");

    }

    @Test
    public void getCrWithMeasuresTest() {
        // GIVEN
        stubFor(get(urlPathEqualTo("/api/v3/measures/species/id/875")) // 875 is a taxon_id
                .withQueryParam("token", equalTo("9bb4facb6d23f48efbf424bb05c0c1ef1cf6f468393bc745d42179ac4aca5fee"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBodyFile("measures.json")));

        Species mammalia_CR = new Species();
        mammalia_CR.setKingdom_name("ANIMALIA");
        mammalia_CR.setTaxonid(875);
        mammalia_CR.setPhylum_name("CHORDATA");
        mammalia_CR.setClass_name("MAMMALIA");
        mammalia_CR.setFamily_name("CRICETIDAE");
        mammalia_CR.setOrder_name("RODENTIA");
        mammalia_CR.setGenus_name("Allocricetulus");
        mammalia_CR.setScientific_name("Allocricetulus eversmanni");
        mammalia_CR.setCategory("CR");

        String conservationMeasures = "Site/area protection, Species management, Harvest management";
        mammalia_CR.setConservationMeasures(conservationMeasures);

        Set<Species> expected = Set.of(mammalia_CR);

        // WHEN
        Set<Species> mammalia = serviceUnderTest.getCrSpeciesWithConservationMeasures(0);

        // THEN
        Assertions.assertEquals(expected, mammalia, "Mammals species are not equls");

    }

}
