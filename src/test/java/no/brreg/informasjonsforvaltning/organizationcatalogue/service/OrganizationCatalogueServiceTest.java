package no.brreg.informasjonsforvaltning.organizationcatalogue.service;

import no.brreg.informasjonsforvaltning.organizationcatalogue.adapter.EnhetsregisteretAdapter;
import no.brreg.informasjonsforvaltning.organizationcatalogue.configuration.ProfileConditionalValues;
import no.brreg.informasjonsforvaltning.organizationcatalogue.model.OrganizationDB;
import no.brreg.informasjonsforvaltning.organizationcatalogue.generated.model.Organization;
import no.brreg.informasjonsforvaltning.organizationcatalogue.repository.OrganizationCatalogueRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.platform.runner.JUnitPlatform;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static no.brreg.informasjonsforvaltning.organizationcatalogue.TestDataKt.getORG_0;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static no.brreg.informasjonsforvaltning.organizationcatalogue.TestDataKt.getORG_DB_0;
import static org.junit.jupiter.api.Assertions.assertNull;

@ExtendWith(MockitoExtension.class)
@RunWith(JUnitPlatform.class)
@Tag("unit")
public class OrganizationCatalogueServiceTest {

    @Mock
    private OrganizationCatalogueRepository repository;

    @Mock
    private EnhetsregisteretAdapter adapter;

    @Mock
    private ProfileConditionalValues valuesMock;

    @InjectMocks
    private OrganizationCatalogueService catalogueService;

    @BeforeEach
    public void resetMocks() {
        Mockito.reset(repository, adapter, valuesMock);
    }

    @Test
    public void getByIdNotFound() {
        Mockito
            .when(repository.findById("123ID"))
            .thenReturn(Optional.empty());

        Organization publisher = catalogueService.getByOrgnr("123ID");

        assertNull(publisher);
    }

    @Test
    public void getById() {
        OrganizationDB persisted = getORG_DB_0();
        Mockito
            .when(repository.findById("123ID"))
            .thenReturn(Optional.of(persisted));
        Mockito
            .when(valuesMock.enhetsregisteretUrl())
            .thenReturn(no.brreg.informasjonsforvaltning.organizationcatalogue.TestDataKt.ENHETSREGISTERET_URL);

        Organization publisher = catalogueService.getByOrgnr("123ID");

        assertEquals(persisted.getName(), publisher.getName());
        assertEquals(persisted.getOrganizationId(), publisher.getOrganizationId());
        assertEquals(persisted.getOrgPath(), publisher.getOrgPath());
        assertEquals(persisted.getPrefLabel(), publisher.getPrefLabel());
        assertEquals(no.brreg.informasjonsforvaltning.organizationcatalogue.TestDataKt.ENHETSREGISTERET_URL + persisted.getOrganizationId(), publisher.getNorwegianRegistry());
    }

    @Test
    public void getAll() {
        List<OrganizationDB> persistedList = Collections.singletonList(getORG_DB_0());
        Mockito
            .when(repository.findAll())
            .thenReturn(persistedList);
        Mockito
            .when(valuesMock.enhetsregisteretUrl())
            .thenReturn(no.brreg.informasjonsforvaltning.organizationcatalogue.TestDataKt.ENHETSREGISTERET_URL);

        List<Organization> publisherList = catalogueService.getOrganizations(null, null);

        assertEquals(persistedList.get(0).getName(), publisherList.get(0).getName());
        assertEquals(persistedList.get(0).getOrganizationId(), publisherList.get(0).getOrganizationId());
        assertEquals(persistedList.get(0).getOrgPath(), publisherList.get(0).getOrgPath());
        assertEquals(persistedList.get(0).getPrefLabel(), publisherList.get(0).getPrefLabel());
        assertEquals(no.brreg.informasjonsforvaltning.organizationcatalogue.TestDataKt.ENHETSREGISTERET_URL + persistedList.get(0).getOrganizationId(), publisherList.get(0).getNorwegianRegistry());
    }

    @Test
    public void getByOrgIdIsPrioritized() {
        List<OrganizationDB> persistedList = Collections.singletonList(getORG_DB_0());
        Mockito
            .when(repository.findByOrganizationIdLike("OrgId"))
            .thenReturn(persistedList);
        Mockito
            .when(valuesMock.enhetsregisteretUrl())
            .thenReturn(no.brreg.informasjonsforvaltning.organizationcatalogue.TestDataKt.ENHETSREGISTERET_URL);

        List<Organization> publisherList = catalogueService.getOrganizations("Name", "OrgId");

        assertEquals(persistedList.get(0).getName(), publisherList.get(0).getName());
        assertEquals(persistedList.get(0).getOrganizationId(), publisherList.get(0).getOrganizationId());
        assertEquals(persistedList.get(0).getOrgPath(), publisherList.get(0).getOrgPath());
        assertEquals(persistedList.get(0).getPrefLabel(), publisherList.get(0).getPrefLabel());
        assertEquals(no.brreg.informasjonsforvaltning.organizationcatalogue.TestDataKt.ENHETSREGISTERET_URL + persistedList.get(0).getOrganizationId(), publisherList.get(0).getNorwegianRegistry());
    }

    @Test
    public void getByName() {
        List<OrganizationDB> persistedList = Collections.singletonList(getORG_DB_0());
        Mockito
            .when(repository.findByNameLike("Name"))
            .thenReturn(persistedList);
        Mockito
            .when(valuesMock.enhetsregisteretUrl())
            .thenReturn(no.brreg.informasjonsforvaltning.organizationcatalogue.TestDataKt.ENHETSREGISTERET_URL);

        List<Organization> publisherList = catalogueService.getOrganizations("Name", null);

        assertEquals(persistedList.get(0).getName(), publisherList.get(0).getName());
        assertEquals(persistedList.get(0).getOrganizationId(), publisherList.get(0).getOrganizationId());
        assertEquals(persistedList.get(0).getOrgPath(), publisherList.get(0).getOrgPath());
        assertEquals(persistedList.get(0).getPrefLabel(), publisherList.get(0).getPrefLabel());
        assertEquals(no.brreg.informasjonsforvaltning.organizationcatalogue.TestDataKt.ENHETSREGISTERET_URL + persistedList.get(0).getOrganizationId(), publisherList.get(0).getNorwegianRegistry());
    }

    @Test
    public void updateNotFound() {
        Mockito
            .when(repository.findById("123ID"))
            .thenReturn(Optional.empty());

        Organization publisher = catalogueService.updateEntry("123ID", getORG_0());

        assertNull(publisher);
    }
}