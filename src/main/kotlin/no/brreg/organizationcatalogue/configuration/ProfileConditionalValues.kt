package no.brreg.organizationcatalogue.configuration

import org.springframework.context.annotation.Profile
import org.springframework.stereotype.Service

interface ProfileConditionalValues {
    fun enhetsregisteretUrl(): String
    fun organizationCatalogueUrl(): String
    fun municipalityUrl(): String
}

@Service
@Profile("test")
class TestValues : ProfileConditionalValues {
    private val ENHETSREGISTERET_URL = "https://invalid.org/enhetsregisteret/api/enheter/"
    private val ORGANIZATION_CATALOGUE_URL = "https://invalid.org/organizations/"
    private val MUNICIPALITY_URL = "https://invalid.org/administrativeEnheter/kommune/id/"

    override fun enhetsregisteretUrl(): String {
        return ENHETSREGISTERET_URL
    }

    override fun organizationCatalogueUrl(): String {
        return ORGANIZATION_CATALOGUE_URL
    }

    override fun municipalityUrl(): String {
        return MUNICIPALITY_URL
    }
}

@Service
@Profile("ut1", "default")
class Ut1Values : ProfileConditionalValues {
    private val ENHETSREGISTERET_URL = "https://data.brreg.no/enhetsregisteret/api/enheter/"
    private val ORGANIZATION_CATALOGUE_URL = "https://publishers-api.ut1.fellesdatakatalog.brreg.no/organizations/"
    private val MUNICIPALITY_URL = "https://data.geonorge.no/administrativeEnheter/kommune/id/"

    override fun enhetsregisteretUrl(): String {
        return ENHETSREGISTERET_URL
    }

    override fun organizationCatalogueUrl(): String {
        return ORGANIZATION_CATALOGUE_URL
    }

    override fun municipalityUrl(): String {
        return MUNICIPALITY_URL
    }
}

@Service
@Profile("prod")
class ProdValues : ProfileConditionalValues {
    private val ENHETSREGISTERET_URL = "https://data.brreg.no/enhetsregisteret/api/enheter/"
    private val ORGANIZATION_CATALOGUE_URL = "https://publishers-api.ut1.fellesdatakatalog.brreg.no/organizations/" // TODO
    private val MUNICIPALITY_URL = "https://data.geonorge.no/administrativeEnheter/kommune/id/"

    override fun enhetsregisteretUrl(): String {
        return ENHETSREGISTERET_URL
    }

    override fun organizationCatalogueUrl(): String {
        return ORGANIZATION_CATALOGUE_URL
    }

    override fun municipalityUrl(): String {
        return MUNICIPALITY_URL
    }
}