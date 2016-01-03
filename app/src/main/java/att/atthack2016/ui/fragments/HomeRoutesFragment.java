package att.atthack2016.ui.fragments;


import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolygonOptions;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import att.atthack2016.R;
import att.atthack2016.io.ApiClient;
import att.atthack2016.models.StationModel;
import att.atthack2016.models.StationModelResponse;
import att.atthack2016.ui.activities.RouteActivity;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.greenrobot.event.EventBus;
import de.hdodenhof.circleimageview.CircleImageView;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class HomeRoutesFragment extends Fragment implements OnMapReadyCallback, GoogleMap.OnMarkerClickListener {

    private GoogleMap mMap;

    @Bind(R.id.sliding_layout)
    SlidingUpPanelLayout slidingUpPanelLayout;
    @Bind(R.id.floatingButtonStartRoute)
    FloatingActionButton floatingButtonStartRoute;

    @Bind(R.id.imageViewStation)
    CircleImageView circleImageView;

    @Bind(R.id.textViewStation)
    TextView textViewStation;

    @Bind(R.id.textViewTime)
    TextView textViewTime;

    private boolean isWaitingNFC;
    private ProgressDialog progressDialog;

    private List<StationModel> resultsStations;
    private ArrayList<Marker> markers;
    private Marker concurrentMarker;

    private int concurrentStation=0;

    public HomeRoutesFragment() {
        // Required empty public constructor
        isWaitingNFC = false;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        setHasOptionsMenu(true);
        EventBus.getDefault().register(this);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home_routes, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        ButterKnife.bind(this, view);
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        setupSlidingUpPanelLayout();

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        searchStation();
    }

    private void searchStation() {
        final ProgressDialog progressDialog = new ProgressDialog(getContext());
        progressDialog.setCancelable(false);
        progressDialog.setMessage(getContext().getString(R.string.loading));

        ApiClient.getApiService().getStations(new Callback<StationModelResponse>() {
            @Override
            public void success(StationModelResponse stationModelResponse, Response response) {
                resultsStations = stationModelResponse.getResults();
                progressDialog.dismiss();
                displayStations();
            }

            @Override
            public void failure(RetrofitError error) {
                progressDialog.dismiss();
            }
        });

    }

    public void displayStations() {

        PolygonOptions polygonOptions = new PolygonOptions();

        markers = new ArrayList<>();

        for (StationModel stationModel : resultsStations) {
            Marker marker = mMap.addMarker(new MarkerOptions().icon(BitmapDescriptorFactory.fromResource(R.mipmap.marker_station))
                    .title(stationModel.getName())
                    .snippet(stationModel.getName())
                    .position(new LatLng(stationModel.getLatitude(), stationModel.getLongitude())));

            markers.add(marker);

            polygonOptions.add(new LatLng(stationModel.getLatitude(), stationModel.getLongitude()));

        }
        polygonOptions.fillColor(getContext().getResources().getColor(R.color.colorAccent));

//        mMap.addPolyline(new PolylineOptions().ad);
    }


    private void setupSlidingUpPanelLayout() {
        slidingUpPanelLayout.setPanelSlideListener(new SlidingUpPanelLayout.PanelSlideListener() {
            @Override
            public void onPanelSlide(View panel, float slideOffset) {

            }

            @Override
            public void onPanelCollapsed(View panel) {

            }

            @Override
            public void onPanelExpanded(View panel) {
                slidingUpPanelLayout.setEnabled(false);

            }

            @Override
            public void onPanelAnchored(View panel) {

            }

            @Override
            public void onPanelHidden(View panel) {

            }
        });
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setMyLocationEnabled(true);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(20.654089, -103.391776), 15));
        mMap.setOnMarkerClickListener(this);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_search_bus, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == R.id.action_search) {
            displaySearchPlaceFragment();

        }

        return super.onOptionsItemSelected(item);


    }

    private void displaySearchPlaceFragment() {

        SearchPlaceFragment searchPlaceFragment = new SearchPlaceFragment();
        searchPlaceFragment.setStationModels(resultsStations);
        getFragmentManager()
                .beginTransaction()
                .setCustomAnimations(android.support.v7.appcompat.R.anim.abc_fade_in,
                        android.support.v7.appcompat.R.anim.abc_slide_out_top,
                        android.support.v7.appcompat.R.anim.abc_slide_out_top,
                        android.support.v7.appcompat.R.anim.abc_fade_out)
                .add(R.id.contentFragment, searchPlaceFragment)
                .addToBackStack(null)
                .commit();
    }

    @OnClick(R.id.floatingButtonStartRoute)
    public void onClickStarRoute() {
        isWaitingNFC = true;

        progressDialog = new ProgressDialog(getContext());
        progressDialog.setTitle(R.string.app_name);
        progressDialog.setMessage(getString(R.string.message_check_nfc));
        progressDialog.setButton(DialogInterface.BUTTON_NEGATIVE, getString(R.string.cancel), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                isWaitingNFC = false;
                progressDialog.dismiss();
            }
        });
        progressDialog.show();
    }


    public void receiveIdBus(String dataId) {
        if (isWaitingNFC) {
            progressDialog.dismiss();

            EventBus.getDefault().postSticky(resultsStations);
            EventBus.getDefault().postSticky(resultsStations.get(concurrentStation));

            Intent intent = new Intent(getContext(), RouteActivity.class);
            startActivity(intent);
            getActivity().finish();
        }
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        Log.d("onMarkerClick", marker.getId());
        String id = marker.getId();
        concurrentStation = Integer.parseInt(id.substring(id.length() - 1));
        updateInfoStation(resultsStations.get(concurrentStation));
        concurrentMarker = marker;
        return false;
    }

    public void onEvent(StationModel stationModel) {
        concurrentStation = stationModel.getId() - 1;
        displayMarker(stationModel);
        updateInfoStation(stationModel);
    }

    private void displayMarker(StationModel stationModel) {
        concurrentMarker = markers.get(stationModel.getId());
        mMap.animateCamera(CameraUpdateFactory.newLatLng(concurrentMarker.getPosition()));
        concurrentMarker.showInfoWindow();

    }


    private void updateInfoStation(StationModel stationModel) {
        textViewStation.setText(stationModel.getName());
        Picasso.with(getContext()).load(stationModel.getImage_url()).placeholder(R.mipmap.ic_launcher).into(circleImageView);
        textViewTime.setText("3:00 PM");

        displaySlidingPanel();
    }

    private void displaySlidingPanel() {

        slidingUpPanelLayout.setPanelState(SlidingUpPanelLayout.PanelState.EXPANDED);
        floatingButtonStartRoute.show();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    public boolean hideSlidePanel() {

        if (slidingUpPanelLayout.getPanelState() == SlidingUpPanelLayout.PanelState.EXPANDED) {
            slidingUpPanelLayout.setEnabled(true);
            slidingUpPanelLayout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
            floatingButtonStartRoute.hide();
            concurrentMarker.hideInfoWindow();
            return true;
        } else {
            return false;
        }

    }

}
