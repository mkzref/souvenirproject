package com.example.souvenir.Interface;

import com.example.souvenir.NotificationsOfMsgs.MyResponse;
import com.example.souvenir.NotificationsOfMsgs.Sender;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface APIservice {//api of notification
    @Headers(
            {
                    "Content-Type:application/json",
                    "Authorization:Key=AAAAMDeyldU:APA91bGMDYnKCuodye4MosfLZYYo817_-B82FWCXpqR9CGyXufM0IA2jLsegbPul9cig55C542218tIJvhJgsF7ivVIeR0MUZjybogn_x2i4Qcm-aMJTIaLwBTWgoRM0mE8JmZqTUmHb"
            //key is from firebase , from project settings then cloud messaging then the server key
            }


    )

    @POST("fcm/send")
    Call<MyResponse> sendNotification(@Body Sender body);
}
