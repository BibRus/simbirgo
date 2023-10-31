package ru.bibrus.simbirgo.rent;

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
import ru.bibrus.simbirgo.transport.TransportType;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "rents")
public class Rent {

    @Id
    @GeneratedValue
    private long id;

    private double latitude;

    private double longitude;

    private double radius;

    @Enumerated(EnumType.STRING)
    private TransportType transportType;

}