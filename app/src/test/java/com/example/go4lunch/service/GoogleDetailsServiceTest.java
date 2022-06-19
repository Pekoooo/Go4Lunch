package com.example.go4lunch.service;

import com.example.go4lunch.BuildConfig;
import com.example.go4lunch.model.AppModel.GoogleDetailApiHolder;
import com.example.go4lunch.model.GooglePlacesModel.PlaceDetailResponseModel;
import com.example.go4lunch.model.GooglePlacesModel.PlaceModel;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import static org.junit.Assert.*;

public class GoogleDetailsServiceTest {

    private static final String TEST_PLACE_ID_SEARCH = "ChIJ3aygTC-D5kcRP4VOGK4rvCI";
    private static final String EXPECTED_PLACE_NAME_RESPONSE = "L'Orient Palace";
    public MockWebServer mockBackEnd;
    public GoogleDetailsService api;

    @Before
    public void setUp() {
        mockBackEnd = new MockWebServer();

        api = GoogleDetailApiHolder.getInstance(mockBackEnd.url("/"));

    }

    @After
    public void tearDown() throws IOException {
        mockBackEnd.shutdown();
    }

    @Test
    public void nominal_case() throws IOException {
        String json = "{ \"html_attributions\" : [], \"result\" : { \"address_components\" : [ { \"long_name\" : \"N10\", \"short_name\" : \"N10\", \"types\" : [ \"route\" ] }, { \"long_name\" : \"Coignières\", \"short_name\" : \"Coignières\", \"types\" : [ \"locality\", \"political\" ] }, { \"long_name\" : \"Yvelines\", \"short_name\" : \"Yvelines\", \"types\" : [ \"administrative_area_level_2\", \"political\" ] }, { \"long_name\" : \"Île-de-France\", \"short_name\" : \"IDF\", \"types\" : [ \"administrative_area_level_1\", \"political\" ] }, { \"long_name\" : \"France\", \"short_name\" : \"FR\", \"types\" : [ \"country\", \"political\" ] }, { \"long_name\" : \"78310\", \"short_name\" : \"78310\", \"types\" : [ \"postal_code\" ] } ], \"adr_address\" : \"134, \\u003cspan class=\\\"street-address\\\"\\u003eN10\\u003c/span\\u003e, \\u003cspan class=\\\"postal-code\\\"\\u003e78310\\u003c/span\\u003e \\u003cspan class=\\\"locality\\\"\\u003eCoignières\\u003c/span\\u003e, \\u003cspan class=\\\"country-name\\\"\\u003eFrance\\u003c/span\\u003e\", \"business_status\" : \"CLOSED_TEMPORARILY\", \"formatted_address\" : \"134, N10, 78310 Coignières, France\", \"formatted_phone_number\" : \"01 30 49 05 42\", \"geometry\" : { \"location\" : { \"lat\" : 48.7490519, \"lng\" : 1.9241118 }, \"viewport\" : { \"northeast\" : { \"lat\" : 48.75032058029149, \"lng\" : 1.925502230291502 }, \"southwest\" : { \"lat\" : 48.7476226197085, \"lng\" : 1.922804269708498 } } }, \"icon\" : \"https://maps.gstatic.com/mapfiles/place_api/icons/v1/png_71/restaurant-71.png\", \"icon_background_color\" : \"#FF9E67\", \"icon_mask_base_uri\" : \"https://maps.gstatic.com/mapfiles/place_api/icons/v2/restaurant_pinlet\", \"international_phone_number\" : \"+33 1 30 49 05 42\", \"name\" : \"L'Orient Palace\", \"permanently_closed\" : true, \"photos\" : [ { \"height\" : 2140, \"html_attributions\" : [ \"\\u003ca href=\\\"https://maps.google.com/maps/contrib/111068889140796642189\\\"\\u003eBenjamin HUET\\u003c/a\\u003e\" ], \"photo_reference\" : \"Aap_uEBpEB2cVJJl99RT7OcaKPvMFr-4AiuVtPEF5gzJT9NDKMdSwoOBF7kiAeGT6iSsD6lNUsO9lm3z2QmEjowx5n9TmkEdjmBnNptO4_WpYxlCFifzTX6IyB-RXlQwsD1zA5i9X_Ly-1QtIrydziRSAzTCQrDUygf-JoMLHdjbLLTin1FG\", \"width\" : 3582 }, { \"height\" : 1363, \"html_attributions\" : [ \"\\u003ca href=\\\"https://maps.google.com/maps/contrib/105434080693353103808\\\"\\u003eL'Orient Palace\\u003c/a\\u003e\" ], \"photo_reference\" : \"Aap_uEC0xVhhPmROhe_QIkDshtKKRfpOcJ47mzYIG2M0NaCF8HGn7_nMwZtmo002Y4VyqcOoCRbOh9HxvEmglpmAcu9FpZd_GFMH0IurhYo5ZrXMEFvzn_piDmquC2fcqMN2gYifOYz_tOV_U9BjdvUktCqrBRCX3PsuawXhGvCOHWMcN8L6\", \"width\" : 2048 }, { \"height\" : 2268, \"html_attributions\" : [ \"\\u003ca href=\\\"https://maps.google.com/maps/contrib/111417726601538800304\\\"\\u003eLionel Barbier\\u003c/a\\u003e\" ], \"photo_reference\" : \"Aap_uECPqHewVBAbyTVg9pQiKhmwrrjkfvMCWy2dUgciu-kTKVplHAfovaNMvv3cT8w84LF-XMO6q_O0tJOoI4AmkJm53Ggs3x6UcKOFX6BG4wktt23W-AtOfaBIbieXLCdKxg7ixpmVtF3WYhWzTiysVuRVDEhxtM4cHJYt107H9P6-Ht8\", \"width\" : 4032 }, { \"height\" : 4618, \"html_attributions\" : [ \"\\u003ca href=\\\"https://maps.google.com/maps/contrib/107716051398097575909\\\"\\u003eTiffany B.\\u003c/a\\u003e\" ], \"photo_reference\" : \"Aap_uEAJzax-1tpvhXIbVGu7JJ3-ndoKb9xlv9qbpAZ2VAQyYcUZquedMV-KZI6kVKb9Q0giybuwgKBz60FZFeHcPYZA35qR12tDhkOWuHesxZmqkCSShlcUSSulvU6_Y5lcy3tO1WCw8ipjP887lqewFEScq_znjNJTx6vzU6lgXDY2HZdq\", \"width\" : 3464 }, { \"height\" : 4618, \"html_attributions\" : [ \"\\u003ca href=\\\"https://maps.google.com/maps/contrib/112259077190960783534\\\"\\u003eRaphael Savinas\\u003c/a\\u003e\" ], \"photo_reference\" : \"Aap_uEAouwrEiuh84ppCYjqvcjJA3IeBZXl7wMgDntnzYxv00dLkqENERkZoM5FfPvhmzOE52w-3tc-wq8aHHK3kCMVFnqLTX5vF4KEocrLNklf1SJlWZpQCVJjGkfDr4rIweoDjIHBTk55d-G1xhqI4cCl1GfV03cJoehTGs-x9W6wcO6ki\", \"width\" : 3464 }, { \"height\" : 3968, \"html_attributions\" : [ \"\\u003ca href=\\\"https://maps.google.com/maps/contrib/111162979670940460024\\\"\\u003eFranck LAYDU\\u003c/a\\u003e\" ], \"photo_reference\" : \"Aap_uEBauXc-XFO1vMoNWo2euW8wq1Wlltzn22hQLvFajAHd64MiQzI5s5oJE2RpLEN7Forwah1EtOscpXC8yXKySVi3uxLFiN-UeFQQVBXLJPafphFDH0m4204vsFYzhbXA7gFTD7500dClYAiziaQkiYIMhaAjnKe1DYWLeZ5H0Y6IIqs7\", \"width\" : 2976 }, { \"height\" : 4128, \"html_attributions\" : [ \"\\u003ca href=\\\"https://maps.google.com/maps/contrib/101954245979019818122\\\"\\u003eNathalie d'Arundel de Condé\\u003c/a\\u003e\" ], \"photo_reference\" : \"Aap_uED6izBcPBWg1xgPKCuOnHtDjcQ2xQb34l3_udgj2gjTiNjbbzXO8Xygret4JC7d_0zXGghF1qIeBuTJRvTyrr9afLnlFLwcQfEDglsde-6-4GNvlqc73gTtaoaazQKQn2q75XvQGlYLjZ3bS4OGFJs3B8CwfDUZ0rGQHhbPa0rWmQJA\", \"width\" : 2322 }, { \"height\" : 4032, \"html_attributions\" : [ \"\\u003ca href=\\\"https://maps.google.com/maps/contrib/111417726601538800304\\\"\\u003eLionel Barbier\\u003c/a\\u003e\" ], \"photo_reference\" : \"Aap_uEAocqUsSMs333s_u4wdPLYUJoKNxtOHDXthPNonnry2sfbXaBNBTb1iag-vE2BPsNgm2dbqilJUtRPHmkuDwYEPjdmZJy59HjVe-3r0aXRdvohUBQO7VcFA1hix7B9nbmPP5IiqyPHrXaavYstyuq5UpOeT1_LfuJuooWDfVPOl6_Y\", \"width\" : 2268 }, { \"height\" : 3464, \"html_attributions\" : [ \"\\u003ca href=\\\"https://maps.google.com/maps/contrib/112259077190960783534\\\"\\u003eRaphael Savinas\\u003c/a\\u003e\" ], \"photo_reference\" : \"Aap_uEDVPJqSgflzJbjk8idiwIpzFJMRJ2h8Wtb_Sk6HvBZ4uquHuZPUyjjDIYqik90gEMdR8i2xwwsWAX61_X0aLo2tr5-gvsrTe7kGwDiCfwJY1JaQ4R-X3bFdGVdmePek-nliMWA70c-P2LfiPSI_uIcYUKKMllbJXqEmgHCx7GMTvidf\", \"width\" : 4618 }, { \"height\" : 4032, \"html_attributions\" : [ \"\\u003ca href=\\\"https://maps.google.com/maps/contrib/111417726601538800304\\\"\\u003eLionel Barbier\\u003c/a\\u003e\" ], \"photo_reference\" : \"Aap_uEDH5PYTzHhe1OgHQz2yCrYrAH8UOp_PKT4nh_6qvGN7K_p8iRpcum2Ep472JPxUNM0VXCfDAOnDpduDsERrKAebfoDWINrV2lqm7weQ1xcNEKjUC6iFZR1cs20_yl3O4dwkGoaIt9oG5w7qtDl8k6bNNapwKZIQcPgGyFnznT4nQss\", \"width\" : 2268 } ], \"place_id\" : \"ChIJ3aygTC-D5kcRP4VOGK4rvCI\", \"plus_code\" : { \"compound_code\" : \"PWXF+JJ Coignières, France\", \"global_code\" : \"8FW3PWXF+JJ\" }, \"price_level\" : 2, \"rating\" : 4, \"reference\" : \"ChIJ3aygTC-D5kcRP4VOGK4rvCI\", \"reviews\" : [ { \"author_name\" : \"Philippe Aubert\", \"author_url\" : \"https://www.google.com/maps/contrib/109993393527751756934/reviews\", \"language\" : \"en\", \"profile_photo_url\" : \"https://lh3.googleusercontent.com/a/AATXAJyvrSJvnfxZf-mBHrxOGKCjgyI8LjbDM9b_RgmQ=s128-c0x00000000-cc-rp-mo\", \"rating\" : 3, \"relative_time_description\" : \"3 years ago\", \"text\" : \"Correct\", \"time\" : 1559561583 }, { \"author_name\" : \"#HAGENCY Travels\", \"author_url\" : \"https://www.google.com/maps/contrib/111830126467695072616/reviews\", \"language\" : \"en\", \"profile_photo_url\" : \"https://lh3.googleusercontent.com/a-/AOh14Ghirv1fu5jVaBkkuJTKDeTLPAyHX4MDsUYLxNK9=s128-c0x00000000-cc-rp-mo-ba4\", \"rating\" : 5, \"relative_time_description\" : \"2 years ago\", \"text\" : \"Top.!!!!!!\", \"time\" : 1567254235 }, { \"author_name\" : \"Céline GOUBARD\", \"author_url\" : \"https://www.google.com/maps/contrib/101911818972336699903/reviews\", \"language\" : \"en-US\", \"profile_photo_url\" : \"https://lh3.googleusercontent.com/a-/AOh14GhefIdf044c5t9uJwrvbuOTumRQvzv7i6dQ2_-9cA=s128-c0x00000000-cc-rp-mo-ba4\", \"rating\" : 1, \"relative_time_description\" : \"11 months ago\", \"text\" : \"Closed in July, too bad\", \"time\" : 1626948052 }, { \"author_name\" : \"Marine Paris\", \"author_url\" : \"https://www.google.com/maps/contrib/117135341806571101729/reviews\", \"language\" : \"en-US\", \"profile_photo_url\" : \"https://lh3.googleusercontent.com/a-/AOh14GiUFGXCCmTPgsFVV6gmJE8_do7zR-nuzlpSU48BYQ=s128-c0x00000000-cc-rp-mo\", \"rating\" : 5, \"relative_time_description\" : \"a year ago\", \"text\" : \"Very friendly staff!\\nExcellent and hearty couscous.\\nThe meat tastes very good.\\nI recommend the assortment of pastry which is to die for!\\nVery good restaurant \uD83D\uDC4D\", \"time\" : 1603132246 }, { \"author_name\" : \"Muriel Bourg\", \"author_url\" : \"https://www.google.com/maps/contrib/106781787766245932818/reviews\", \"language\" : \"en-US\", \"profile_photo_url\" : \"https://lh3.googleusercontent.com/a/AATXAJyuiKAHUX_sVI6gaE0kX8aUw0SYSN2HW_CJDcOr=s128-c0x00000000-cc-rp-mo\", \"rating\" : 3, \"relative_time_description\" : \"a year ago\", \"text\" : \"Very good restaurant. Too bad the bosses do not enforce the wearing of masks when people are not at the table. \uD83D\uDE1E\", \"time\" : 1602357961 } ], \"types\" : [ \"restaurant\", \"food\", \"point_of_interest\", \"establishment\" ], \"url\" : \"https://maps.google.com/?cid=2502923519643256127\", \"user_ratings_total\" : 236, \"utc_offset\" : 120, \"vicinity\" : \"134, N10, Coignières\" }, \"status\" : \"OK\" }";

        MockResponse response = new MockResponse().setResponseCode(200).setBody(json);

        mockBackEnd.enqueue(response);

        Response<PlaceDetailResponseModel> result = api.getDetails(TEST_PLACE_ID_SEARCH, BuildConfig.API_KEY).execute();

        assert result.body() != null;
        PlaceModel resultPlaceModel = result.body().getResult();

        assertEquals(EXPECTED_PLACE_NAME_RESPONSE, resultPlaceModel.getName());

    }

}