package com.epam.rd.java.basic.taxiservice.service;

import com.epam.rd.java.basic.taxiservice.config.PropertiesUtil;
import com.epam.rd.java.basic.taxiservice.model.Address;
import com.epam.rd.java.basic.taxiservice.model.Trip;
import com.fasterxml.jackson.databind.util.JSONPObject;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.servlet.ServletContext;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class DistanceService {
    private static final String BASIC_REQUEST_STRING =
            "http://dev.virtualearth.net/REST/V1/Routes/Driving?wp.0=%s&wp.1=%s&avoid=minimizeTolls&key=%s";
    private final HttpClient CLIENT = HttpClient.newHttpClient();

    public Double getDistance(Trip trip) {

        String departureAddressString = addressToString(trip.getDepartureAddress());
        String destinationAddressString = addressToString(trip.getDestinationAddress());
        String requestString = prepareRequest(departureAddressString, destinationAddressString);
        HttpResponse<String> response = getJSONResponse(requestString);
        Double distance = 0.0;
        if (response.statusCode() == 200) {
            JSONObject jsonObject = new JSONObject(response.body());
            JSONArray resourseSets = jsonObject.getJSONArray("resourceSets");
            JSONObject resourseSet = resourseSets.getJSONObject(0);
            JSONObject route = resourseSet.getJSONArray("resources").getJSONObject(0);
            distance = route.getDouble("travelDistance");
            JSONArray routeLegs = route.getJSONArray("routeLegs");
            JSONObject routeLeg = routeLegs.getJSONObject(0);
            JSONObject startLocation = routeLeg.getJSONObject("startLocation");
            JSONObject startLocationAddress = startLocation.getJSONObject("address");
            String startLocationFormattedAddress = startLocationAddress.getString("formattedAddress");
            JSONObject endLocation = routeLeg.getJSONObject("endLocation");
            JSONObject endLocationAddress = endLocation.getJSONObject("address");
            String endLocationFormattedAddress = endLocationAddress.getString("formattedAddress");
            System.out.println(distance);
            System.out.println("Start: " + startLocationFormattedAddress);
            System.out.println("End: " + endLocationFormattedAddress);
        }
        return distance;
    }

    private HttpResponse<String> getJSONResponse(String requestString) {
        URI uri = URI.create(requestString);
        HttpRequest request = HttpRequest.newBuilder()
                .header("accept", "application/json")
                .header("Accept-Charset", "utf-8")
                .uri(uri)
                .GET()
                .build();
        HttpResponse<String> response = null;
        try {
            response = CLIENT.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return response;
    }

    private String addressToString (Address address) {
        return String.format("%s %s %s Kyiv",
                address.getStreet().getTitle(),
                address.getStreet().getStreetType(),
                address.getBuilding());
    }

    private String prepareRequest(String departureAddressString, String destinationAddressString) {
        String msKey = System.getenv("MS_KEY");
        return String.format(BASIC_REQUEST_STRING, departureAddressString, destinationAddressString, msKey).replaceAll(" ", "%20");
    }
}
