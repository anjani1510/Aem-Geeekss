package com.aem.geeks.core.models;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.OSGiService;
import org.json.JSONException;
import org.json.JSONObject;

import com.aem.geeks.core.services.ExternalAPIService;

import javax.annotation.PostConstruct;

@Model(adaptables = { Resource.class })
public class ExternalAPIModel {

    @OSGiService
    private ExternalAPIService externalAPIService;

    // Declare all the fields present in the JSON
    private int id;
    private String name;
    private String username;
    private String email;
    private String phone;
    private String website;
    private String companyName;
    private String companyCatchPhrase;
    private String companyBs;
    private String addressStreet;
    private String addressSuite;
    private String addressCity;
    private String addressZipcode;
    private String geoLat;
    private String geoLng;

    @PostConstruct
    protected void init() throws JSONException {
        JSONObject data = externalAPIService.fetchData();
        if (data != null) {
            // Fetch top-level fields
            this.id = data.getInt("id");
            this.name = data.getString("name");
            this.username = data.getString("username");
            this.email = data.getString("email");
            this.phone = data.getString("phone");
            this.website = data.getString("website");

            // Fetch nested fields: company
            JSONObject company = data.getJSONObject("company");
            this.companyName = company.getString("name");
            this.companyCatchPhrase = company.getString("catchPhrase");
            this.companyBs = company.getString("bs");

            // Fetch nested fields: address
            JSONObject address = data.getJSONObject("address");
            this.addressStreet = address.getString("street");
            this.addressSuite = address.getString("suite");
            this.addressCity = address.getString("city");
            this.addressZipcode = address.getString("zipcode");

            // Fetch nested fields: geo (inside address)
            JSONObject geo = address.getJSONObject("geo");
            this.geoLat = geo.getString("lat");
            this.geoLng = geo.getString("lng");
        }
    }

    // Getters for all fields

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public String getPhone() {
        return phone;
    }

    public String getWebsite() {
        return website;
    }

    public String getCompanyName() {
        return companyName;
    }

    public String getCompanyCatchPhrase() {
        return companyCatchPhrase;
    }

    public String getCompanyBs() {
        return companyBs;
    }

    public String getAddressStreet() {
        return addressStreet;
    }

    public String getAddressSuite() {
        return addressSuite;
    }

    public String getAddressCity() {
        return addressCity;
    }

    public String getAddressZipcode() {
        return addressZipcode;
    }

    public String getGeoLat() {
        return geoLat;
    }

    public String getGeoLng() {
        return geoLng;
    }
}
