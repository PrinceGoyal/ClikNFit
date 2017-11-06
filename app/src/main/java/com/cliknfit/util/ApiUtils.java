package com.cliknfit.util;

import com.cliknfit.interfaces.SOService;
import com.cliknfit.remote.RetrofitClient;

/**
 * Created by Prince on 3/3/2017.
 */

public class ApiUtils {
    public static final String BASE_URL = Constants.BASE_URL;

    public static SOService getSOService() {

        return RetrofitClient.getClient(BASE_URL).create(SOService.class);
    }
}
