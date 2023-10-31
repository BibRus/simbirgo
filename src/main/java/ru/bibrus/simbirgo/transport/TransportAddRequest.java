package ru.bibrus.simbirgo.transport;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TransportAddRequest {

    private boolean canBeRented;

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
