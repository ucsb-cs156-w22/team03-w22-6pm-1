import React from 'react'
import { useBackend } from 'main/utils/useBackend';

import BasicLayout from "main/layouts/BasicLayout/BasicLayout";
import EarthquakesTable from 'main/components/Earthquakes/EarthquakesTable';
import { Button } from 'react-bootstrap';
import { useBackendMutation } from 'main/utils/useBackend';
import { toast } from 'react-toastify';
import { hasRole, useCurrentUser } from "main/utils/currentUser";

function Purge() {
  let purge = useBackendMutation(
    () => ({ url: "/api/earthquakes/purge", method: "POST" }),
    { onSuccess: () => { toast("Earthquakes were deleted"); } },
    "/api/earthquakes/all"
  );
  return (
    <Button variant="danger" onClick={() => purge.mutate()} data-testid="Earthquakes-purge-button">
      Purge
    </Button>
  );
}

export default function EarthquakesIndexPage() {

  const currentUser = useCurrentUser();


  const { data: earthquakes, error: _error, status: _status } =
    useBackend(
      // Stryker disable next-line all : don't test internal caching of React Query
      ["/api/earthquakes/all"],
      { method: "GET", url: "/api/earthquakes/all" },
      []
    );


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
        {(hasRole(currentUser, "ROLE_ADMIN")) ? <Purge /> : null}
        
      </div>
    </BasicLayout>
  )
}