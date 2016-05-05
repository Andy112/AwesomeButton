package mad.acai.com.awesomebutton.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import mad.acai.com.awesomebutton.MyApplication;
import mad.acai.com.awesomebutton.R;
import mad.acai.com.awesomebutton.adapters.BoxOfficeAdapter;
import mad.acai.com.awesomebutton.extras.BookingSorter;
import mad.acai.com.awesomebutton.extras.SortListener;
import mad.acai.com.awesomebutton.logging.L;
import mad.acai.com.awesomebutton.network.VolleySingleton;
import mad.acai.com.awesomebutton.objects.Booking;

import static mad.acai.com.awesomebutton.extras.Keys.EndPointBox.KEY_BOOKING;
import static mad.acai.com.awesomebutton.extras.Keys.EndPointBox.KEY_ID;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link BoxOfficeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class BoxOfficeFragment extends Fragment implements SortListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    public static final String MY_WEBSITE = "http://acaiii.com";
    private static final String BOOKING_STATE = "booking_state";
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private VolleySingleton volleySingleton;
    private RequestQueue requestQueue;
    private ImageLoader imageLoader;
    private BookingSorter bookingSorter;

    private ArrayList<Booking> bookingList = new ArrayList<>();
    private DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    private RecyclerView listBookings;
    private BoxOfficeAdapter adapter;
    private static ImageView iconMyBooking;


    public BoxOfficeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment BoxOfficeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static BoxOfficeFragment newInstance(ImageView iconMyBooking, String param1, String param2) {
        BoxOfficeFragment.iconMyBooking = iconMyBooking;
        BoxOfficeFragment fragment = new BoxOfficeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public static String getRequestUrl(int limit) {
        return MY_WEBSITE + "apikey" + MyApplication.API_KEY + "&limit = " + limit;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        volleySingleton = VolleySingleton.getInstance();
        requestQueue = volleySingleton.getRequestQueue();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putParcelableArrayList(BOOKING_STATE, bookingList);
    }

    private void sendJsonRequest() {

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET,
                getRequestUrl(10),
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        parseJSONResponse(response);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                DecimalFormat df = new DecimalFormat("#.##");
               for (int i = 1; i < 11; i++) {
                   Booking booking = new Booking();
                   booking.setId(i);
                   booking.setBookingStatus(i%2 == 0? "Pending": "Completed");
                   booking.setPickUpAdd("Airport");
                   booking.setDropOffAdd("Home");
                   try {
                       booking.setPickUpTime(dateFormat.parse("2016-04-28"));
                       booking.setPickUpDate(dateFormat.parse("2016-04-28"));
                   } catch (ParseException e) {
                       e.printStackTrace();
                   }
                   booking.setFare(Double.valueOf(df.format(50 * Math.random())));

                   if (booking.getBookingStatus().equals("Pending")) {
                       bookingList.add(booking);
                   }

                   adapter.setListBookings(bookingList);
               }

                //L.t(getActivity(),bookingList.toString() );
            }
        });
        requestQueue.add(request);
    }

    private void parseJSONResponse(JSONObject response) {

        List<Booking> listBook = new ArrayList<>();
        if (response != null || response.length() > 0) {
            try {
                StringBuilder data = new StringBuilder();
                JSONArray jsonArray = response.getJSONArray(KEY_BOOKING);
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject currentObject = jsonArray.getJSONObject(i);
                    long id = currentObject.getLong(KEY_ID);
                    data.append(id + "\n");
                    L.t(getActivity(), data.toString());
                }
            } catch (JSONException e) {

            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_box_office, container, false);
        listBookings = (RecyclerView)view.findViewById(R.id.list_bookings);
        listBookings.setLayoutManager(new LinearLayoutManager(getActivity()));

        adapter = new BoxOfficeAdapter(getActivity());
        listBookings.setAdapter(adapter);

        if (savedInstanceState != null) {
            bookingList = savedInstanceState.getParcelableArrayList(BOOKING_STATE);
            adapter.setListBookings(bookingList);
        } else {
            sendJsonRequest();
        }
        return view;
    }

    @Override
    public void onSortById() {
        L.t(getActivity(), "Sort Id by Advance Bookings");
        BookingSorter.getInstance().sortBookingById(bookingList);
        adapter.notifyDataSetChanged();

    }

    @Override
    public void onSortByBookingStatus() {
        BookingSorter.getInstance().sortBookingByStatus(bookingList);
        adapter.notifyDataSetChanged();

    }

    @Override
    public void onSortByFare() {
        BookingSorter.getInstance().sortBookingByFare(bookingList);
        adapter.notifyDataSetChanged();
    }
}
