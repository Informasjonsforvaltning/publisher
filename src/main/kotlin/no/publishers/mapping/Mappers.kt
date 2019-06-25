package no.publishers.mapping

import no.publishers.generated.model.PrefLabel
import no.publishers.generated.model.Publisher
import no.publishers.graphql.CreatePublisher
import no.publishers.model.PublisherDB

fun PublisherDB.mapToGenerated(): Publisher {
    val mapped = Publisher()

    mapped.id = id.toHexString()
    mapped.name = name
    mapped.orgPath = orgPath
    mapped.uri = uri
    mapped.organizationId = organizationId

    val prefLabel = PrefLabel()
    prefLabel.nb = nbPrefLabel
    prefLabel.nn = nnPrefLabel
    prefLabel.en = enPrefLabel
    mapped.prefLabel = prefLabel

    return mapped
}

fun CreatePublisher.mapForPersistence(): PublisherDB {
    val mapped = PublisherDB()

    mapped.name = name
    mapped.orgPath = orgPath
    mapped.uri = uri
    mapped.organizationId = organizationId
    mapped.nbPrefLabel = nbPrefLabel
    mapped.nnPrefLabel = nnPrefLabel
    mapped.enPrefLabel = enPrefLabel

    return mapped
}