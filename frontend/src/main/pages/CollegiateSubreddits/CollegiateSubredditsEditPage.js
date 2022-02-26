import BasicLayout from "main/layouts/BasicLayout/BasicLayout";
import { useParams } from "react-router-dom";
import CollegiateSubredditForm from "main/components/CollegiateSubreddits/CollegiateSubredditForm";
import { Navigate } from 'react-router-dom'
import { useBackend, useBackendMutation } from "main/utils/useBackend";
import { toast } from "react-toastify";

export default function CollegiateSubredditsEditPage() {
  let { id } = useParams();

  const { data: collegiateSubreddit, error: error, status: status } =
    useBackend(
      // Stryker disable next-line all : don't test internal caching of React Query
      [`/api/collegiateSubreddits?id=${id}`],
      {  // Stryker disable next-line all : GET is the default, so changing this to "" doesn't introduce a bug
        method: "GET",
        url: `/api/collegiateSubreddits`,
        params: {
          id
        }
      }
    );


  const objectToAxiosPutParams = (collegiateSubreddit) => ({
    url: "/api/collegiateSubreddits",
    method: "PUT",
    params: {
      id: collegiateSubreddit.id,
    },
    data: {
      name: collegiateSubreddit.name,
      location: collegiateSubreddit.location,
      subreddit: collegiateSubreddit.subreddit,
    }
  });

  const onSuccess = (collegiateSubreddit) => {
    toast(`Collegiate Subreddit Updated - id: ${collegiateSubreddit.id} name: ${collegiateSubreddit.name}`);
  }

  const mutation = useBackendMutation(
    objectToAxiosPutParams,
    { onSuccess },
    // Stryker disable next-line all : hard to set up test for caching
    [`/api/collegiateSubreddits?id=${id}`]
  );

  const { isSuccess } = mutation

  const onSubmit = async (data) => {
    mutation.mutate(data);
  }

  if (isSuccess) {
    return <Navigate to="/collegiateSubreddits/list" />
  }

  return (
    <BasicLayout>
      <div className="pt-2">
        <h1>Edit CollegiateSubreddit</h1>
        {collegiateSubreddit &&
          <CollegiateSubredditForm initialCollegiateSubreddit={collegiateSubreddit} submitAction={onSubmit} buttonLabel="Update" />
        }
      </div>
    </BasicLayout>
  )
}