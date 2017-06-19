package com.angular.gerardosuarez.carpoolingapp.mvp.presenter;

import android.Manifest;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.pm.PackageManager;
import android.location.Location;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

import com.angular.gerardosuarez.carpoolingapp.R;
import com.angular.gerardosuarez.carpoolingapp.mvp.model.PassengerQuota;
import com.angular.gerardosuarez.carpoolingapp.mvp.view.DriverMapView;
import com.angular.gerardosuarez.carpoolingapp.service.DriverMapService;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class DriverMapPresenter implements GoogleMap.OnMarkerClickListener {

    private DriverMapView view;
    private DriverMapService service;

    private ChildEventListener chatsListRef;
    private DatabaseReference databaseRef;

    public DriverMapPresenter(DriverMapView view, DriverMapService service) {
        this.view = view;
        this.service = service;
        this.databaseRef = FirebaseDatabase.getInstance().getReference();
    }

    public void init() {
        Activity activity = view.getActivity();
        if (activity == null) {
            return;
        }
        if (activity.getFragmentManager() == null) {
            return;
        }
        if (view.getMap() == null) {
            return;
        }
        view.getMap().setOnMarkerClickListener(this);
        view.setListeners();
        requestPermissions(activity);
    }

    public void unsubscribe() {
        if (chatsListRef != null) {
            databaseRef.removeEventListener(chatsListRef);
        }
    }

    public void subscribe() {
        getQuotas("icesi", "from", "18062017", "1600");
    }

    //Services
    public void getQuotas(String comunity, String origin, String date, String hour) {
        chatsListRef = service.getQuotasPerCommunityOriginDateAndHour(comunity, origin, date, hour)
                .addChildEventListener(
                        new ChildEventListener() {
                            @Override
                            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                                PassengerQuota chat = dataSnapshot.getValue(PassengerQuota.class);
                                chat.getClass();
                                chat.toString();
                            }

                            @Override
                            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

                            }

                            @Override
                            public void onChildRemoved(DataSnapshot dataSnapshot) {

                            }

                            @Override
                            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });
    }

    public void setAutocompleteFragment() {
        view.setAutocompleteFragment();
    }

    private void setLocationManager() {
        view.setLocationManager();
    }

    private void requestPermissions(Activity activity) {
        if (ContextCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            view.getMap().setMyLocationEnabled(true);
            setLocationManager();
        } else {
            view.requestPermissionsActivity();
        }
    }

    public boolean googleServicesAvailable() {
        boolean isAvailable = false;
        if (view.getContext() == null) {
            return isAvailable;
        }
        GoogleApiAvailability api = GoogleApiAvailability.getInstance();
        int availableId = api.isGooglePlayServicesAvailable(view.getContext());
        if (availableId == ConnectionResult.SUCCESS) {
            isAvailable = true;
        } else if (api.isUserResolvableError(availableId)) {
            view.showErrorDialog(api, availableId);
        } else {
            view.showToast(R.string.error_google_services_conection);
        }
        return isAvailable;
    }

    public void initMap() {
        Activity activity = view.getActivity();
        if (activity == null) {
            return;
        }
        Fragment fragment = view.getFragment();
        if (fragment == null) {
            return;
        }
        FragmentManager fragmentManager = fragment.getChildFragmentManager();
        if (fragmentManager == null) {
            return;
        }
        view.initMap();
    }

    public void setMap(GoogleMap map) {
        view.setMap(map);
    }

    public void addLocationButton(String[] permissions, int[] grantResults) {
        Activity activity = view.getActivity();
        if (activity == null) {
            return;
        }
        if (permissions.length == 1 &&
                permissions[0].equalsIgnoreCase(Manifest.permission.ACCESS_FINE_LOCATION) &&
                grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                view.getMap().setMyLocationEnabled(true);
                setLocationManager();
            }
        } else {
            view.showToast(R.string.permission_denied);
        }
    }

    private void onLocaltionButtonListener() {
        view.getMap().setOnMyLocationButtonClickListener(new GoogleMap.OnMyLocationButtonClickListener() {
            @Override
            public boolean onMyLocationButtonClick() {
                view.showToast(R.string.app_name);
                return true;
            }
        });
    }

    public void addMockMarkers() {
        view.setMarker(new LatLng(3.4, -76.5), "Cupo 1", 1);
        view.setMarker(new LatLng(3.49, -76.54), "Cupo 2", 2);
        view.setMarker(new LatLng(3.50, -76.52), "Cupo 3", 3);
        view.setMarker(new LatLng(3.51, -76.5), "Cupo 4", 4);
        view.setMarker(new LatLng(3.52, -76.5), "Cupo 5", 5);
        view.setMarker(new LatLng(3.53, -76.523), "Cupo 6", 6);
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        if (marker == null) {
            return false;
        }
        view.showToast(marker.getTitle() + " id: " + marker.getTag());
        return true;
    }

    public void searchPlace(Place place) {
        Activity activity = view.getActivity();
        if (activity == null) {
            return;
        }
        LatLng placeLocation = place.getLatLng();
        String placeName = place.getName().toString();
        view.animateCamera(placeLocation);
        //view.setMarker(placeLocation, placeName);
    }

    public void onLocationChanged(Location location) {
        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
        view.goToCurrentLocation(latLng);
    }

    public void setAutocompleteFragmentText() {
        view.setTextAutocompleteFragmentWithCurrentCoord();
    }
}

