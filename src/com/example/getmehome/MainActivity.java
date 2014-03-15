package com.example.getmehome;

import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class MainActivity extends Activity implements SensorEventListener {
	private TextView startLatitudeField;
	private TextView startLongitudeField;
	private TextView endLatitudeField;
	private TextView endLongitudeField;
	private TextView bearingField;
	private ImageView arrowPoint;
	private LocationManager locationManager;
	private LocationListener locationListener;
	private String provider;
	private SensorManager mSensorManager;
	private float currentDegree = 0f;
	double startLat = 0;
	double startLng = 0;
	double endLat = 0;
	double endLng = 0;
	TextView tvHeading;
	


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

		//Setting TextViews for the coordinates
		startLatitudeField = (TextView) findViewById(R.id.TextView02);
		startLongitudeField = (TextView) findViewById(R.id.TextView04);
		endLatitudeField = (TextView) findViewById(R.id.textView06);
		endLongitudeField = (TextView) findViewById(R.id.textView08);
		bearingField = (TextView) findViewById(R.id.textView2);
		arrowPoint = (ImageView) findViewById(R.id.imageView1);
		tvHeading = (TextView) findViewById(R.id.tvHeading);
		mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
		

		//Button
		Button button = (Button) findViewById(R.id.button1);
		button.setOnClickListener(new View.OnClickListener()
		{

			@Override
			public void onClick(View v) {

				locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
				locationListener = new MyLocationListener(); 
				locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
				
				
				/*Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

				if (startLat != 0) {
					locationListener.onLocationChanged(location);
				} else {
					locationListener.onLocationChanged(location);
					Button button = (Button) findViewById(R.id.button1);
					button.setText("Get Me Home");
				}*/
				



			}	
		}); 
	};


	private class MyLocationListener implements LocationListener
	{
		@Override
		public void onLocationChanged(Location location) 
		{

			if (startLat == 0) {
				startLat = (double) (location.getLatitude());
				startLng = (double) (location.getLongitude());
				startLatitudeField.setText(String.valueOf(startLat));
				startLongitudeField.setText(String.valueOf(startLng));
				Button button = (Button) findViewById(R.id.button1);
				button.setText("Get Me Home");
				locationManager.removeUpdates(locationListener);
			} else {
				endLat = (double) (location.getLatitude());
				endLng = (double) (location.getLongitude());
				endLatitudeField.setText(String.valueOf(endLat));
				endLongitudeField.setText(String.valueOf(endLng));
				bearingField.setText("" + bearing(startLat, startLng, endLat, endLng));
				
				
			}
		}

		@Override
		public void onProviderDisabled(String provider) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onProviderEnabled(String provider) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onStatusChanged(String provider, int status, Bundle extras) {
			// TODO Auto-generated method stub

		}
	}

	public double bearing(double startLat, double startLng, double endLat, double endLng){
			double longitude1 = startLng;
			double longitude2 = endLng;
			double latitude1 = Math.toRadians(startLat);
			double latitude2 = Math.toRadians(endLat);
			double longDiff= Math.toRadians(longitude2-longitude1);
			double y= Math.sin(longDiff)*Math.cos(latitude2);
			double x=Math.cos(latitude1)*Math.sin(latitude2)
					-Math.sin(latitude1)*Math.cos(latitude2)*Math.cos(longDiff);
			
			return (Math.toDegrees(Math.atan2(y, x))+360)%360;


	}
	
	
	@Override
	public void onResume() {
		super.onResume();
		
			mSensorManager.registerListener(this, 
					mSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD), 
						(SensorManager.SENSOR_DELAY_GAME));
			
	}
	
	@Override
	public void onPause() {
		super.onPause();
			// save battery
			mSensorManager.unregisterListener(this);
	}


	@Override
	public void onSensorChanged(SensorEvent event) {
		
			// get the angle around the z-axis rotated
			float degree = Math.round(event.values[0]);
			tvHeading.setText("Heading: " + Float.toString(degree) + " degrees");
			
			// create a rotation animation (reverse turn degree degrees)
			RotateAnimation ra = new RotateAnimation(
					currentDegree,
					-degree,
					Animation.RELATIVE_TO_SELF, 
					0.5f,
					Animation.RELATIVE_TO_SELF,
					0.5f);
			
			// how long the animation will take place
			ra.setDuration(210);
			
			// set the animation after the end of the reservation status
			ra.setFillAfter(true);
			
			// start the animation
			arrowPoint.startAnimation(ra);
			currentDegree = -degree;
	
	}
	
	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {
		// not in use
		
	}
}