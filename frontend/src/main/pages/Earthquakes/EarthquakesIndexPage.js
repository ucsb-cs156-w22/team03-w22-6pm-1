import React from 'react'
import { useBackend } from 'main/utils/useBackend';

import BasicLayout from "main/layouts/BasicLayout/BasicLayout";
import EarthquakesTable from 'main/components/Earthquakes/EarthquakesTable';
import { useCurrentUser } from 'main/utils/currentUser'

export default function EarthquakesIndexPage() {

  const currentUser = useCurrentUser();

  const { data: earthquakes, error: _error, status: _status } =
    useBackend(
      // Stryker disable next-line all : don't test internal caching of React Query
      ["/api/earthquakes/all"],
      { method: "GET", url: "/api/earthquakes/all" },
      []
    );
    
    console.log(earthquakes)

    let obj = earthquakes.map(earthquake => {
      let json = earthquake.properties
      json.id = earthquake.id
      json.title = earthquake.properties.title
      return json
    })

  return (
    <BasicLayout>
      <div className="pt-2">
        <h1>Earthquakes</h1>
        <EarthquakesTable earthquakes={obj} currentUser={currentUser} />
      </div>
    </BasicLayout>
  )
}