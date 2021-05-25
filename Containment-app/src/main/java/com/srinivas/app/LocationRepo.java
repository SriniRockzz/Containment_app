package com.srinivas.app;
import com.google.firebase.cloud.FirestoreClient;

import java.util.concurrent.ExecutionException;

import org.springframework.stereotype.Service;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.WriteResult;


@Service 
public class LocationRepo {
	 
	
	 public static final String COL_NAME="latlong";  
	 public String save(Location loc) throws InterruptedException, ExecutionException {  
	 Firestore dbFirestore = FirestoreClient.getFirestore();  
	 ApiFuture<WriteResult> collectionsApiFuture = dbFirestore.collection(COL_NAME).document((loc.getLatitude()+","+loc.getLongitude()).toString()).set(loc);  
	 return collectionsApiFuture.get().getUpdateTime().toString();  
	 }  
	 public Location getPatientDetails(String name) throws InterruptedException, ExecutionException {  
	 Firestore dbFirestore = FirestoreClient.getFirestore();  
	 DocumentReference documentReference = dbFirestore.collection(COL_NAME).document(name);  
	 ApiFuture<DocumentSnapshot> future = documentReference.get();  
	 DocumentSnapshot document = future.get();  
	Location loc= null;  
	 if(document.exists()) {  
	 loc= document.toObject(Location.class);  
	 return loc;  
	 }else {  
	 return null;  
	 }  
	 }  
	
	 
	
}
