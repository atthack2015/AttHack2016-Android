package att.atthack2016.ui.fragments;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;

import att.atthack2016.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class HomeRoutesFragment extends Fragment implements OnMapReadyCallback {

    private GoogleMap mMap;

    public HomeRoutesFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
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

        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setMyLocationEnabled(true);
        mMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(20.654089, -103.391776)));


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
        getFragmentManager()
                .beginTransaction()
                .setCustomAnimations(android.support.v7.appcompat.R.anim.abc_fade_in,
                        android.support.v7.appcompat.R.anim.abc_slide_out_top,
                        android.support.v7.appcompat.R.anim.abc_slide_out_top,
                        android.support.v7.appcompat.R.anim.abc_fade_out)
                .add(R.id.contentFragment, new SearchPlaceFragment())
                .addToBackStack(null)
                .commit();
    }

}
