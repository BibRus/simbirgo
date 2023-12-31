package ru.bibrus.simbirgo.transport;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "transports")
public class Transport {

    @Id
    @GeneratedValue
    private long id;

    private boolean canBeRented;

    @Enumerated(EnumType.STRING)
    private TransportType transportType;

    private String model;

    private String color;

    private String identifier;

    private String description;

    private double latitude;

    private double longitude;

    private double minutePrice;

    private double dayPrice;

}