package no.brreg.organizationcatalogue.jena

import no.brreg.organizationcatalogue.generated.model.Organization
import no.brreg.organizationcatalogue.generated.model.PrefLabel
import org.apache.jena.rdf.model.Model
import org.apache.jena.rdf.model.ModelFactory
import org.apache.jena.rdf.model.Property
import org.apache.jena.rdf.model.Resource
import org.apache.jena.sparql.vocabulary.FOAF
import org.apache.jena.vocabulary.DCTerms
import org.apache.jena.vocabulary.ORG
import org.apache.jena.vocabulary.RDF
import org.apache.jena.vocabulary.ROV
import org.apache.jena.vocabulary.SKOS
import java.io.ByteArrayOutputStream

fun Organization.jenaResponse(responseType: JenaType): String =
    listOf(this).jenaResponse(responseType)

fun List<Organization>.jenaResponse(responseType: JenaType): String =
    createModel().createResponseString(responseType)

private fun List<Organization>.createModel(): Model {
    val model = ModelFactory.createDefaultModel()
    model.setNsPrefix("dct", DCTerms.getURI())
    model.setNsPrefix("skos", SKOS.uri)
    model.setNsPrefix("foaf", FOAF.getURI())
    model.setNsPrefix("org", ORG.getURI())
    model.setNsPrefix("rov", ROV.getURI())
    model.setNsPrefix("adms", ADMS.uri)
    model.setNsPrefix("br", BR.uri)

    forEach {
        model.createResource("https://publishers-api.ut1.fellesdatakatalog.brreg.no/${it.organizationId}")
            .addProperty(RDF.type, FOAF.Organization)
            .safeAddProperty(ROV.legalName, it.name)
            .addRegistration(it)
            .safeAddProperty(ROV.orgType, it.orgType)
            .safeAddProperty(BR.orgPath, it.orgPath)
            .safeAddProperty(ORG.subOrganizationOf, it.subOrganizationOf)
            .safeAddProperty(BR.municipalityNumber, it.municipalityNumber)
            .safeAddProperty(BR.industryCode, it.industryCode)
            .safeAddProperty(BR.sectorCode, it.sectorCode)
            .addPreferredNames(it.prefLabel)
    }

    return model
}

private fun Resource.addRegistration(org: Organization): Resource =
    addProperty(
        ROV.registration,
        model.createResource(ADMS.Identifier)
            .safeAddProperty(DCTerms.issued, org.issued?.toString())
            .addProperty(SKOS.notation, org.organizationId)
            .addProperty(ADMS.schemaAgency, "Brønnøysundregistrene"))

private fun Resource.safeAddProperty(property: Property, value: String?): Resource =
    if(value == null) this
    else addProperty(property, value)

private fun Resource.safeAddLinkedProperty(property: Property, value: String?): Resource =
    if(value == null) this
    else addProperty(property, model.createResource(value))

private fun Resource.addPreferredNames(preferredNames: PrefLabel): Resource {
    if (preferredNames.nb != null) addProperty(FOAF.name, preferredNames.nb, "nb")
    if (preferredNames.nn != null) addProperty(FOAF.name, preferredNames.nn, "nn")
    if (preferredNames.en != null) addProperty(FOAF.name, preferredNames.en, "en")
    return this
}
private fun Model.createResponseString(responseType: JenaType):String =
    ByteArrayOutputStream().use{ out ->
        write(out, responseType.value)
        out.flush()
        out.toString("UTF-8")
    }

fun acceptHeaderToJenaType(accept: String?): JenaType =
    when (accept) {
        "text/turtle" -> JenaType.TURTLE
        "application/rdf+xml" -> JenaType.RDF_XML
        "application/rdf+json" -> JenaType.RDF_JSON
        "application/ld+json" -> JenaType.JSON_LD
        "application/xml" -> JenaType.NOT_JENA
        "application/json" -> JenaType.NOT_JENA
        else -> JenaType.NOT_ACCEPTABLE
    }

enum class JenaType(val value: String){
    TURTLE("TURTLE"),
    RDF_XML("RDF/XML"),
    RDF_JSON("RDF/JSON"),
    JSON_LD("JSON-LD"),
    NOT_JENA("NOT-JENA"),
    NOT_ACCEPTABLE("")
}