# Backend request manual
How to use backend API :wink:

## Contents
- [**PEAK**](#peak)
	- [search](#peak-search)
	- [creation](#peak-creation)
- [**HIKE**](#hike)
	- [search](#hike-search)


## Peak

### Peak search
- **request**: POST to `/api/peak/search`
	```
	{
	  "id" : 1,                     // optional - response will contail at most 1 peak
	  "nameSearch" : "omnic",       // optional - search for substring in name
	  "minHeight" : 200,            // optional - metres
	  "maxHeight" : 2000,           // optional - metres
	  "minLatitude" : "49:09:51",   // optional     
	  "maxLatitude" : "50:09:51",   // optional
	  "minLongitude" : "20:08:09",  // optional
	  "maxLongitude" : "21:08:09"   // optional
	}
	```
- **response**: JSON array of objects - peaks
	```
	[
	  {
	    "id" : 1,
	    "height" : 2655.
	    "name" : "Gerlachovský štít",
	    "latitude" : "49:09:51",
	    "longitude" : "20:08:09"
	  },
	  {
	    ...
	  },
	  
	  ...
	  
	]
	```

### Peak creation
- **request**: POST to `/api/peak/create`
	```
	{
	  "name" : "Highest mountain ever",
	  "height" : 20000,
	  "latitude" : "20:20:20",
	  "longitude" : "30:30:30",
	}
	```
- **response on success**: TEXT
	```
	integer - id of the newly created peak
	```
- **response on failure**: TEXT
	```
	FAIL
	```

## Hike

### Hike search
- **request**: POST to `/api/hike/search`
	```
	{
	  "id" : 1,                   // optional - response will contail at most 1 hike
	  "nameSearch" : "omnic",     // optional - search for substring in hike name
	  "minDate" : "2022-01-30",   // optional
	  "maxDate" : "2022-02-29",   // optional
	  "minDifficulty" : 0,        // optional     
	  "maxDifficulty" : 10,       // optional
	  "authorId" : 1,             // optional
	  "peakId" : 1                // optional
	}
	```
- **response**: JSON array of objects - hikes
	```
	[
	  {
	    "id" : 1,
	    "peak_id" : 2.
	    "difficulty" : 5,
	    "author_id" : 1,
	    "name" : "Name of the hike",
		"date" : "2022-01-30"
	  },
	  {
	    ...
	  },
	  
	  ...
	  
	]
	```
