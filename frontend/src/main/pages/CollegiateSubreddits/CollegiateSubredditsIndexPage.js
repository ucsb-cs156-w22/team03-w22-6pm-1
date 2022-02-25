import React from 'react'
import { useBackend } from 'main/utils/useBackend';

import BasicLayout from "main/layouts/BasicLayout/BasicLayout";
import CollegiateSubredditsTable from 'main/components/CollegiateSubreddits/CollegiateSubredditsTable';
import { useCurrentUser } from 'main/utils/currentUser'

export default function CollegiateSubredditsIndexPage() {

  const currentUser = useCurrentUser();

  const { data: subreddits, error: _error, status: _status } =
    useBackend(
      // Stryker disable next-line all : don't test internal caching of React Query
      ["/api/collegiateSubreddits/all"],
      { method: "GET", url: "/api/collegiateSubreddits/all" },
      []
    );

  return (
    <BasicLayout>
      <div className="pt-2">
        <h1>CollegiateSubreddits</h1>
        <CollegiateSubredditsTable subreddits={subreddits} currentUser={currentUser} />
      </div>
    </BasicLayout>
  )
}