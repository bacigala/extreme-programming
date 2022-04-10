# Backend request manual
How to use backend API :wink:

## Contents
- [**PEAK**](#peak)
	- [search](#peak-search)


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
