package com.example.go4lunch.service;

import com.example.go4lunch.BuildConfig;
import com.example.go4lunch.model.AppModel.GooglePlacesApiHolder;
import com.example.go4lunch.model.GooglePlacesModel.NearbyResponseModel;
import com.example.go4lunch.model.GooglePlacesModel.PlaceModel;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.List;

import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import retrofit2.Response;
import static org.junit.Assert.*;

public class GooglePlacesServiceTest {


    public MockWebServer mockBackEnd;
    public static final String EXPECTED_LATLNG = "48.7531814,1.9167294";
    private static final String DEFAULT_TYPE_SEARCH = "restaurant";
    private static final int DEFAULT_RADIUS_SEARCH_FOR_TEST = 1000;
    private static final int EXPECTED_RESPONSE_LIST_SIZE = 7;
    private static final String EXPECTED_FIRST_RESTAURANT_NAME = "Marché d'à Côté";
    public GooglePlacesService SUT;

    @Before
    public void setUp() {
        mockBackEnd = new MockWebServer();

        SUT = GooglePlacesApiHolder.getInstance(mockBackEnd.url("/"));

    }

    @After
    public void tearDown() throws IOException {
        mockBackEnd.shutdown();
    }

    @Test
    public void nominal_case() throws IOException {
        String json = "{ \"html_attributions\" : [], \"results\" : [ { \"business_status\" : \"OPERATIONAL\", \"geometry\" : { \"location\" : { \"lat\" : 48.750583, \"lng\" : 1.919044 }, \"viewport\" : { \"northeast\" : { \"lat\" : 48.75195528029149, \"lng\" : 1.920366280291502 }, \"southwest\" : { \"lat\" : 48.74925731970849, \"lng\" : 1.917668319708498 } } }, \"icon\" : \"https://maps.gstatic.com/mapfiles/place_api/icons/v1/png_71/shopping-71.png\", \"icon_background_color\" : \"#4B96F3\", \"icon_mask_base_uri\" : \"https://maps.gstatic.com/mapfiles/place_api/icons/v2/shoppingcart_pinlet\", \"name\" : \"Marché d'à Côté\", \"opening_hours\" : { \"open_now\" : true }, \"photos\" : [ { \"height\" : 527, \"html_attributions\" : [ \"\\u003ca href=\\\"https://maps.google.com/maps/contrib/112744307110013792601\\\"\\u003eMarché d'à Côté\\u003c/a\\u003e\" ], \"photo_reference\" : \"Aap_uEAWMj5bbOgqa36bsWz275pCN-9E3FPCooQe5XShUlzlXraceX8YtribWgFOgoo857p7alTLSO4eEORMWvcVFhgWE9YA55XXy-27wMf_dSiEFpF20bXJTWc-K0WlWe9QEkAmjEn9Sljtxp14QxD_4ZpsVDteb6k7rK5AAK0laA1NsCvs\", \"width\" : 937 } ], \"place_id\" : \"ChIJvQPE4MGD5kcRXfcSAcdpUzI\", \"plus_code\" : { \"compound_code\" : \"QW29+6J Coignières, France\", \"global_code\" : \"8FW3QW29+6J\" }, \"reference\" : \"ChIJvQPE4MGD5kcRXfcSAcdpUzI\", \"scope\" : \"GOOGLE\", \"types\" : [ \"supermarket\", \"convenience_store\", \"grocery_or_supermarket\", \"liquor_store\", \"restaurant\", \"food\", \"point_of_interest\", \"store\", \"establishment\" ], \"vicinity\" : \"1 Passage du Commerce, Coignières\" }, { \"business_status\" : \"OPERATIONAL\", \"geometry\" : { \"location\" : { \"lat\" : 48.7501356, \"lng\" : 1.919644 }, \"viewport\" : { \"northeast\" : { \"lat\" : 48.75154293029149, \"lng\" : 1.921102830291501 }, \"southwest\" : { \"lat\" : 48.74884496970849, \"lng\" : 1.918404869708498 } } }, \"icon\" : \"https://maps.gstatic.com/mapfiles/place_api/icons/v1/png_71/restaurant-71.png\", \"icon_background_color\" : \"#FF9E67\", \"icon_mask_base_uri\" : \"https://maps.gstatic.com/mapfiles/place_api/icons/v2/restaurant_pinlet\", \"name\" : \"POP'S CHICKEN'S\", \"opening_hours\" : { \"open_now\" : true }, \"photos\" : [ { \"height\" : 3024, \"html_attributions\" : [ \"\\u003ca href=\\\"https://maps.google.com/maps/contrib/116254471418930501142\\\"\\u003eAinkaran Kunapalasingam\\u003c/a\\u003e\" ], \"photo_reference\" : \"Aap_uEDgpzdnFLkid973tiTGqEu_zBsXYqZ-mmwTxmLem96MncwS2SlXrKvtgIjVIpIOaqo20RmYgOEGzRDUz0kf2rP3EWsqnrD6l3YDBDaVa62uQqo-4YxtWRkvdwc7BRiJQ-fm7EyZohz5KPWjN304PB5s72gHB-VKiGq3_2__mgxMQOvi\", \"width\" : 4032 } ], \"place_id\" : \"ChIJW8roo6GD5kcRvzLb-xR2UqM\", \"plus_code\" : { \"compound_code\" : \"QW29+3V Coignières, France\", \"global_code\" : \"8FW3QW29+3V\" }, \"rating\" : 3.8, \"reference\" : \"ChIJW8roo6GD5kcRvzLb-xR2UqM\", \"scope\" : \"GOOGLE\", \"types\" : [ \"restaurant\", \"food\", \"point_of_interest\", \"establishment\" ], \"user_ratings_total\" : 4, \"vicinity\" : \"1 Passage du Commerce, Coignières\" }, { \"business_status\" : \"OPERATIONAL\", \"geometry\" : { \"location\" : { \"lat\" : 48.7501337, \"lng\" : 1.9196464 }, \"viewport\" : { \"northeast\" : { \"lat\" : 48.7515424802915, \"lng\" : 1.921104380291502 }, \"southwest\" : { \"lat\" : 48.7488445197085, \"lng\" : 1.918406419708498 } } }, \"icon\" : \"https://maps.gstatic.com/mapfiles/place_api/icons/v1/png_71/restaurant-71.png\", \"icon_background_color\" : \"#FF9E67\", \"icon_mask_base_uri\" : \"https://maps.gstatic.com/mapfiles/place_api/icons/v2/restaurant_pinlet\", \"name\" : \"PIZZA LA ROMA Coignières\", \"opening_hours\" : { \"open_now\" : false }, \"photos\" : [ { \"height\" : 608, \"html_attributions\" : [ \"\\u003ca href=\\\"https://maps.google.com/maps/contrib/117325949980874517616\\\"\\u003ePIZZA LA ROMA Coignières\\u003c/a\\u003e\" ], \"photo_reference\" : \"Aap_uED8Y2XSaFzmBOmq4hJvDP9YwUlrkwesx9E3hJE8RAvMZsHmhDSSsHQ141oCgbcApcu5aR__o_vZY_JQNxsDQ9ooEeyiat38w2POhOV75Pagr8hc2l7gMKyLifrv52Lg6kYkJvZqn68l3hWAOmQgJyZ9Hs4FV8Bmc29bDGo9D4k691ux\", \"width\" : 1080 } ], \"place_id\" : \"ChIJZT4A4k2D5kcRTallvpHMgSg\", \"plus_code\" : { \"compound_code\" : \"QW29+3V Coignières, France\", \"global_code\" : \"8FW3QW29+3V\" }, \"rating\" : 4.7, \"reference\" : \"ChIJZT4A4k2D5kcRTallvpHMgSg\", \"scope\" : \"GOOGLE\", \"types\" : [ \"restaurant\", \"food\", \"point_of_interest\", \"establishment\" ], \"user_ratings_total\" : 14, \"vicinity\" : \"1 Passage du Commerce, Coignières\" }, { \"business_status\" : \"OPERATIONAL\", \"geometry\" : { \"location\" : { \"lat\" : 48.7501256, \"lng\" : 1.9196462 }, \"viewport\" : { \"northeast\" : { \"lat\" : 48.7515418302915, \"lng\" : 1.921106580291502 }, \"southwest\" : { \"lat\" : 48.7488438697085, \"lng\" : 1.918408619708498 } } }, \"icon\" : \"https://maps.gstatic.com/mapfiles/place_api/icons/v1/png_71/generic_business-71.png\", \"icon_background_color\" : \"#7B9EB0\", \"icon_mask_base_uri\" : \"https://maps.gstatic.com/mapfiles/place_api/icons/v2/generic_pinlet\", \"name\" : \"Casa Di Roma\", \"opening_hours\" : { \"open_now\" : false }, \"photos\" : [ { \"height\" : 1080, \"html_attributions\" : [ \"\\u003ca href=\\\"https://maps.google.com/maps/contrib/113327419282280018634\\\"\\u003eAnaïs Louveau\\u003c/a\\u003e\" ], \"photo_reference\" : \"Aap_uEC7IZ9fDIfnEDDHkOF-gcNjSPATYw80eRxCmKri4rf1ljU-bL9j5VXRz7GwYYFwtX3rINGjks4UgrFB2NOEThGEqYVA23_w6xCWflKqHiMuFnzLMz4DbFQczQyyD30UB4CsLDIbEi-x_9Dof2kI067iSFwJzUkdWgX2e7oONyptjPaQ\", \"width\" : 1920 } ], \"place_id\" : \"ChIJARzo7S6D5kcRvHmCaojWquo\", \"plus_code\" : { \"compound_code\" : \"QW29+3V Coignières, France\", \"global_code\" : \"8FW3QW29+3V\" }, \"rating\" : 3.3, \"reference\" : \"ChIJARzo7S6D5kcRvHmCaojWquo\", \"scope\" : \"GOOGLE\", \"types\" : [ \"meal_delivery\", \"restaurant\", \"food\", \"point_of_interest\", \"establishment\" ], \"user_ratings_total\" : 16, \"vicinity\" : \"3 Passage du Commerce, Coignières\" }, { \"business_status\" : \"OPERATIONAL\", \"geometry\" : { \"location\" : { \"lat\" : 48.7500483, \"lng\" : 1.919624400000001 }, \"viewport\" : { \"northeast\" : { \"lat\" : 48.75141448029151, \"lng\" : 1.921166230291502 }, \"southwest\" : { \"lat\" : 48.74871651970851, \"lng\" : 1.918468269708498 } } }, \"icon\" : \"https://maps.gstatic.com/mapfiles/place_api/icons/v1/png_71/restaurant-71.png\", \"icon_background_color\" : \"#FF9E67\", \"icon_mask_base_uri\" : \"https://maps.gstatic.com/mapfiles/place_api/icons/v2/restaurant_pinlet\", \"name\" : \"O'five\", \"opening_hours\" : { \"open_now\" : true }, \"photos\" : [ { \"height\" : 673, \"html_attributions\" : [ \"\\u003ca href=\\\"https://maps.google.com/maps/contrib/111377986975917172134\\\"\\u003eO'five\\u003c/a\\u003e\" ], \"photo_reference\" : \"Aap_uEAyY8XU_MdC2OX2uXMrk_Be_PXnm9NLe_X8tjqErH1zBZB_TzI5ZyZBznJd8H6I2Osmqi1Go2jlcttbtEZnpyeSORHRCms91SQCcw0pmuIr24HYMDYuOC81g7EHM0XDTTq-vELPdXpAaM5CLnV0pd2YH4PqrHwsBxdj88502L_jCeAN\", \"width\" : 1200 } ], \"place_id\" : \"ChIJ47CXqs-D5kcRpEom5Jckqr8\", \"plus_code\" : { \"compound_code\" : \"QW29+2R Coignières, France\", \"global_code\" : \"8FW3QW29+2R\" }, \"rating\" : 5, \"reference\" : \"ChIJ47CXqs-D5kcRpEom5Jckqr8\", \"scope\" : \"GOOGLE\", \"types\" : [ \"restaurant\", \"food\", \"point_of_interest\", \"store\", \"establishment\" ], \"user_ratings_total\" : 2, \"vicinity\" : \"5 Passage du Commerce, Coignières\" }, { \"business_status\" : \"OPERATIONAL\", \"geometry\" : { \"location\" : { \"lat\" : 48.7499374, \"lng\" : 1.9197547 }, \"viewport\" : { \"northeast\" : { \"lat\" : 48.75128703029149, \"lng\" : 1.921236780291502 }, \"southwest\" : { \"lat\" : 48.7485890697085, \"lng\" : 1.918538819708498 } } }, \"icon\" : \"https://maps.gstatic.com/mapfiles/place_api/icons/v1/png_71/restaurant-71.png\", \"icon_background_color\" : \"#FF9E67\", \"icon_mask_base_uri\" : \"https://maps.gstatic.com/mapfiles/place_api/icons/v2/restaurant_pinlet\", \"name\" : \"JODAS DELICES\", \"opening_hours\" : { \"open_now\" : true }, \"photos\" : [ { \"height\" : 3024, \"html_attributions\" : [ \"\\u003ca href=\\\"https://maps.google.com/maps/contrib/114993693885614022008\\\"\\u003eJODAS DELICES\\u003c/a\\u003e\" ], \"photo_reference\" : \"Aap_uED_iygKjoJXnM7_fAEl4w2SApRj_n27qx1Z91BRudD9olWjxXkbKKvwVtOzLXm1U7J8M2GacRoQyJk5sGaL3aIDMbhvpo2Oh--yjbicEmZa_QYzI_LWL_Ky0tHWxPkpYF-MyeJfW0q_M9J6GI4nNm0IW9qyoifvAe_x8Qmkqq4atl0P\", \"width\" : 4032 } ], \"place_id\" : \"ChIJ7df9dd2D5kcRN2oTIMKyCnc\", \"plus_code\" : { \"compound_code\" : \"PWX9+XW Coignières, France\", \"global_code\" : \"8FW3PWX9+XW\" }, \"rating\" : 2.8, \"reference\" : \"ChIJ7df9dd2D5kcRN2oTIMKyCnc\", \"scope\" : \"GOOGLE\", \"types\" : [ \"bakery\", \"restaurant\", \"food\", \"point_of_interest\", \"store\", \"establishment\" ], \"user_ratings_total\" : 4, \"vicinity\" : \"5 Centre Commercial le Village, Coignières\" }, { \"business_status\" : \"OPERATIONAL\", \"geometry\" : { \"location\" : { \"lat\" : 48.7499374, \"lng\" : 1.9197547 }, \"viewport\" : { \"northeast\" : { \"lat\" : 48.75128703029149, \"lng\" : 1.921236780291502 }, \"southwest\" : { \"lat\" : 48.7485890697085, \"lng\" : 1.918538819708498 } } }, \"icon\" : \"https://maps.gstatic.com/mapfiles/place_api/icons/v1/png_71/restaurant-71.png\", \"icon_background_color\" : \"#FF9E67\", \"icon_mask_base_uri\" : \"https://maps.gstatic.com/mapfiles/place_api/icons/v2/restaurant_pinlet\", \"name\" : \"Le Best\", \"opening_hours\" : { \"open_now\" : true }, \"place_id\" : \"ChIJqW3SrbaD5kcRgLroppMJ2xM\", \"plus_code\" : { \"compound_code\" : \"PWX9+XW Coignières, France\", \"global_code\" : \"8FW3PWX9+XW\" }, \"rating\" : 4.6, \"reference\" : \"ChIJqW3SrbaD5kcRgLroppMJ2xM\", \"scope\" : \"GOOGLE\", \"types\" : [ \"restaurant\", \"bakery\", \"cafe\", \"food\", \"point_of_interest\", \"store\", \"establishment\" ], \"user_ratings_total\" : 19, \"vicinity\" : \"5 Centre Commercial le Village, Coignières\" } ], \"status\" : \"OK\" }";

        MockResponse response = new MockResponse().setResponseCode(200).setBody(json);

        mockBackEnd.enqueue(response);

        Response<NearbyResponseModel> result = SUT.searchRestaurants(
                EXPECTED_LATLNG,
                DEFAULT_TYPE_SEARCH,
                DEFAULT_RADIUS_SEARCH_FOR_TEST,
                BuildConfig.API_KEY)
                .execute();

        assert result.body() != null;
        List<PlaceModel> resultList = result.body().getResults();
        PlaceModel placeModel = resultList.get(0);


        assertEquals(EXPECTED_RESPONSE_LIST_SIZE, resultList.size());
        assertEquals(EXPECTED_FIRST_RESTAURANT_NAME, placeModel.getName());

    }


}