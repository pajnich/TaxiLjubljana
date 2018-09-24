package com.pajchi.taxiljubljana;

import com.google.android.gms.location.places.Place;

import java.io.IOException;

class PriceCalculator {

    public static String getEstimatedPriceToDestinationByTaxiCompany(double distance, String taxiCompany) throws IOException {

        String startFee;
        String regularKm;
        switch (taxiCompany) {
            case "Cammeo":
                startFee = "0,85";
                regularKm = "0,85";
                break;
            case "Intereks":
                startFee = "1,00";
                regularKm = "0,85";
                break;
            case "Intertours":
                startFee = "1,00";
                regularKm = "1,29";
                break;
            case "Laguna":
                startFee = "0,93";
                regularKm = "0,89";
                break;
            case "Ljubljana":
                startFee = "1,50";
                regularKm = "1,10";
                break;
            case "Metro":
                startFee = "0,95";
                regularKm = "0,89";
                break;
            case "Rondo":
                startFee = "1,00";
                regularKm = "0,84";
                break;
                default:
                    throw new IOException("Incorrect taxi company name.");
        }

        String estimatedPrice = String.valueOf(
                Math.round(
                        MainActivity.priceStringToDouble(regularKm)
                                * (distance


                                / 1000)
                                + MainActivity.priceStringToDouble(startFee)
                )
        ) + " €";
        return estimatedPrice;
    }

}
