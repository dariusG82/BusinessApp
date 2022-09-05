package eu.dariusgovedas.businessapp.clients.entities;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ClientDTO {

    private Long businessID;
    private String businessName;
    private String country;
    private String city;
    private String street;
    private String houseNumber;
    private String flatNumber;
    private String phoneNumber;
    private String emailAddress;

    private String streetAddress;

    public String getStreetAddress() {
        String houseAddress = street + " " + houseNumber;
        String flatAddress = houseAddress + "-" + flatNumber;

        return flatNumber == null || flatNumber.equals("") ? houseAddress : flatAddress;
    }
}
