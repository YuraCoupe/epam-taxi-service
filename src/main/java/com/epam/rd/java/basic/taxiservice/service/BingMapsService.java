package com.epam.rd.java.basic.taxiservice.service;

import com.epam.rd.java.basic.taxiservice.model.BingRoute;
import com.epam.rd.java.basic.taxiservice.model.ErrorMessage;
import com.epam.rd.java.basic.taxiservice.model.Trip;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;

public class BingMapsService {
    private static final String BASIC_REQUEST_STRING =
            "http://dev.virtualearth.net/REST/V1/Routes/Driving?wp.0=%s&wp.1=%s&avoid=minimizeTolls&key=%s";
    private final HttpClient CLIENT = HttpClient.newHttpClient();

    public BingRoute getRoute(String departureAddress, String destinationAddress) {
        BingRoute bingRoute = new BingRoute();

        String requestString = prepareRequest(departureAddress, destinationAddress);
        HttpResponse<String> response = getJSONResponse(requestString);
        JSONObject jsonObject = new JSONObject(response.body());
        bingRoute.setStatusCode(jsonObject.getInt("statusCode"));
        if (response.statusCode() == 200) {
            JSONArray resourseSets = jsonObject.getJSONArray("resourceSets");
            JSONObject resourseSet = resourseSets.getJSONObject(0);
            JSONObject route = resourseSet.getJSONArray("resources").getJSONObject(0);
            bingRoute.setTravelDistance(route.getDouble("travelDistance"));
            bingRoute.setTravelDuration(route.getLong("travelDuration"));
            JSONArray routeLegs = route.getJSONArray("routeLegs");
            JSONObject routeLeg = routeLegs.getJSONObject(0);
            JSONObject startLocation = routeLeg.getJSONObject("startLocation");
            JSONObject startLocationAddress = startLocation.getJSONObject("address");
            bingRoute.setStartLocation(startLocationAddress.getString("formattedAddress"));
            JSONObject endLocation = routeLeg.getJSONObject("endLocation");
            JSONObject endLocationAddress = endLocation.getJSONObject("address");
            bingRoute.setEndLocation(endLocationAddress.getString("formattedAddress"));
        }
        if (response.statusCode() == 404) {
            JSONArray errorDetails = jsonObject.getJSONArray("errorDetails");
            ErrorMessage errorMessage = new ErrorMessage();
            List<String> errors = new ArrayList<>();
            errorMessage.setErrors(errors);

            for (int i = 0; i < errorDetails.length(); i++) {
                errorMessage.getErrors().add(errorDetails.getString(i));
            }
            bingRoute.setErrorMessage(errorMessage);
        }
        return bingRoute;
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
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
        return response;
    }

    private String prepareRequest(String departureAddressString, String destinationAddressString) {
        String msKey = System.getenv("MS_KEY");
        return String.format(BASIC_REQUEST_STRING, departureAddressString, destinationAddressString, msKey).replaceAll(" ", "%20");
    }
}
