package com.cms.batchjob;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "endpoint_reference")
@Getter
@Setter
public class Endpoint_Reference {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id;
    Integer npi;
    String endpointType;
    String endpointTypeDescription;
    String endpoint;
    String affiliation;
    String endpointDescription;
    String affiliationLegalBusinessName;
    String useCode;
    String useDescription;
    String otherUseDescription;
    String contentType;
    String contentDescription;
    String otherContentDescription;
    String affiliationAddressLineOne;
    String affiliationAddressLineTwo;
    String affiliationAddressCity;
    String affiliationAddressState;
    String affiliationAddressCountry;
    String affiliationAddressLinePostalCode;
}
