import BasicLayout from "main/layouts/BasicLayout/BasicLayout";
import { useParams } from "react-router-dom";
import UCSBSubjectForm from "main/components/UCSBSubjects/UCSBSubjectForm";
import { Navigate } from 'react-router-dom'
import { useBackend, useBackendMutation } from "main/utils/useBackend";
import { toast } from "react-toastify";

export default function UCSBSubjectsEditPage() {
  let { id } = useParams();

  const { data: ucsbSubject, error: error, status: status } =
    useBackend(
      // Stryker disable next-line all : don't test internal caching of React Query
      [`/api/ucsbsubjects?id=${id}`],
      {  // Stryker disable next-line all : GET is the default, so changing this to "" doesn't introduce a bug
        method: "GET",
        url: `/api/ucsbsubjects`,
        params: {
          id
        }
      }
    );


  const objectToAxiosPutParams = (ucsbSubject) => ({
    url: "/api/ucsbsubjects",
    method: "PUT",
    params: {
      id: ucsbSubject.id,
    },
    data: {
      subjectCode: ucsbSubject.subjectCode,
      subjectTranslation: ucsbSubject.subjectTranslation,
      deptCode: ucsbSubject.deptCode,
      collegeCode: ucsbSubject.collegeCode,
      relatedDeptCode: ucsbSubject.relatedDeptCode,
      inactive: ucsbSubject.inactive
    }
  });

  const onSuccess = (ucsbSubject) => {
    toast(`UCSBSubject Updated - id: ${ucsbSubject.id} subjectCode: ${ucsbSubject.subjectCode}`);
  }

  const mutation = useBackendMutation(
    objectToAxiosPutParams,
    { onSuccess },
    // Stryker disable next-line all : hard to set up test for caching
    [`/api/ucsbsubjects?id=${id}`]
  );

  const { isSuccess } = mutation

  const onSubmit = async (data) => {
    mutation.mutate(data);
  }

  if (isSuccess) {
    return <Navigate to="/ucsbsubjects/list" />
  }

  return (
    <BasicLayout>
      <div className="pt-2">
        <h1>Edit UCSBSubject</h1>
        {ucsbSubject &&
          <UCSBSubjectForm initialUCSBSubject={ucsbSubject} submitAction={onSubmit} buttonLabel="Update" />
        }
      </div>
    </BasicLayout>
  )
}

