package com.web.warehorses_secure_persistence.domain.warehouse.dto;

/**
 * Inbound payload for creating a warehouse.
 *
 * <p>The company is referenced by its NIT, NOT sent as a nested entity. A record
 * component typed as the JPA Company forced the client to post a whole Company
 * object, and posting a plain id string instead failed with
 * "No converter found capable of converting from type [java.lang.String] to
 * type [Company]". Accepting the id keeps the wire format flat and lets the
 * server resolve the entity itself.
 */
public record WarehouseRequestDto(String address, String companyNit) {}
