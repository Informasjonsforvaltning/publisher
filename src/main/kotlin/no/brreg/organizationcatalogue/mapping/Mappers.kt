package no.brreg.organizationcatalogue.mapping

import no.brreg.organizationcatalogue.adapter.enhetsregisteretUrl
import no.brreg.organizationcatalogue.generated.model.PrefLabel
import no.brreg.organizationcatalogue.generated.model.Organization
import no.brreg.organizationcatalogue.model.EnhetsregisteretOrganization
import no.brreg.organizationcatalogue.model.OrganizationDB
import java.time.LocalDate

fun OrganizationDB.mapToGenerated(): Organization {
    val mapped = Organization()

    mapped.id = id.toHexString()
    mapped.name = name
    mapped.norwegianRegistry = enhetsregisteretUrl + organizationId
    mapped.internationalRegistry = internationalRegistry
    mapped.organizationId = organizationId
    mapped.orgType = orgType
    mapped.orgPath = orgPath
    mapped.subOrganizationOf = subOrganizationOf
    mapped.issued = issued
    mapped.municipalityNumber = municipalityNumber
    mapped.industryCode = industryCode
    mapped.sectorCode = sectorCode
    mapped.prefLabel = prefLabel

    return mapped
}

fun EnhetsregisteretOrganization.mapForCreation(): OrganizationDB {
    val mapped = OrganizationDB()

    mapped.name = navn
    mapped.organizationId = organisasjonsnummer
    mapped.orgType = organisasjonsform?.kode
    mapped.orgPath = orgPath
    mapped.subOrganizationOf = overordnetEnhet
    mapped.municipalityNumber = forretningsadresse?.kommunenummer ?: postadresse?.kommunenummer
    mapped.issued = LocalDate.parse(registreringsdatoEnhetsregisteret)
    mapped.industryCode = naeringskode1?.kode
    mapped.sectorCode = institusjonellSektorkode?.kode

    return mapped
}

fun OrganizationDB.updateValues(org: Organization): OrganizationDB =
    apply {
        name = org.name ?: name
        internationalRegistry = org.internationalRegistry ?: internationalRegistry
        orgType = org.orgType ?: orgType
        orgPath = org.orgPath ?: orgPath
        subOrganizationOf = org.subOrganizationOf ?: subOrganizationOf
        municipalityNumber = org.municipalityNumber ?: municipalityNumber
        issued = org.issued ?: issued
        industryCode = org.industryCode ?: industryCode
        sectorCode = org.sectorCode ?: sectorCode
        prefLabel = prefLabel?.update(org.prefLabel) ?: PrefLabel().update(org.prefLabel)
    }

private fun PrefLabel.update(newValues: PrefLabel?): PrefLabel {
    nb = newValues?.nb ?: nb
    nn = newValues?.nn ?: nn
    en = newValues?.en ?: en

    return this
}

private const val orgUrl = "https://publishers-api.ut1.fellesdatakatalog.brreg.no/organizations/"
private const val municipalityUrl = "https://data.geonorge.no/administrativeEnheter/kommune/id/"

fun organizationIdUrl(id: String): String? =
    orgUrl + id

fun organizationUrl(organizationId: String?): String? =
    if(organizationId == null) null
    else orgUrl + "orgnr/$organizationId"

fun municipalityUrl(municipalityNumber: String?): String? {
    if (municipalityNumber == null) return null
    else {
        val municipalityId = municipalityNumberToId(municipalityNumber)
        return municipalityUrl + municipalityId
    }
}