const earthquakesFixtures = {
  twoEarthquakes: [
    {
      "properties": {
        "mag": 0.84,
        "place": "5km NNW of Azusa, CA",
        "time": 1645822915360,
        "updated": 1645823782607,
        "tz": null,
        "url": "https://earthquake.usgs.gov/earthquakes/eventpage/ci40193712",
        "detail": "https://earthquake.usgs.gov/fdsnws/event/1/query?eventid=ci40193712&format=geojson",
        "felt": null,
        "cdi": null,
        "mmi": null,
        "alert": null,
        "status": "reviewed",
        "tsunami": 0,
        "sig": 11,
        "net": "ci",
        "code": "40193712",
        "ids": ",ci40193712,",
        "sources": ",ci,",
        "types": ",nearby-cities,origin,phase-data,scitech-link,",
        "nst": 7,
        "dmin": 0.04919,
        "rms": 0.17,
        "gap": 122,
        "magType": "ml",
        "type": "quarry blast",
        "title": "M 0.8 Quarry Blast - 5km NNW of Azusa, CA"
      },
      "geometry": {
        "coordinates": [-117.9221667,
          34.1805,
        -1.07
        ]
      },
      "id": "ci40193712"
    },
    {
      "properties": {
        "mag": 1.22,
        "place": "12km SE of Bodfish, CA",
        "time": 1645806051530,
        "updated": 1645823784568,
        "tz": null,
        "url": "https://earthquake.usgs.gov/earthquakes/eventpage/ci40193472",
        "detail": "https://earthquake.usgs.gov/fdsnws/event/1/query?eventid=ci40193472&format=geojson",
        "felt": null,
        "cdi": null,
        "mmi": null,
        "alert": null,
        "status": "reviewed",
        "tsunami": 0,
        "sig": 23,
        "net": "ci",
        "code": "40193472",
        "ids": ",ci40193472,",
        "sources": ",ci,",
        "types": ",nearby-cities,origin,phase-data,scitech-link,",
        "nst": 26,
        "dmin": 0.1862,
        "rms": 0.12,
        "gap": 65,
        "magType": "ml",
        "type": "earthquake",
        "title": "M 1.2 - 12km SE of Bodfish, CA"
      },
      "geometry": {
        "coordinates": [-118.3915,
          35.5113333,
          4.42
        ]
      },
      "id": "ci40193472"
    }
  ]
}

export { earthquakesFixtures };