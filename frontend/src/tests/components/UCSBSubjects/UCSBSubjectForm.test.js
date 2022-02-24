import { render, waitFor, fireEvent } from "@testing-library/react";
import UCSBSubjectForm from "main/components/UCSBSubjects/UCSBSubjectForm";
import { ucsbSubjectsFixtures } from "fixtures/ucsbSubjectsFixtures";
import { BrowserRouter as Router } from "react-router-dom";

const mockedNavigate = jest.fn();

jest.mock('react-router-dom', () => ({
    ...jest.requireActual('react-router-dom'),
    useNavigate: () => mockedNavigate
}));


describe("UCSBSubjectForm tests", () => {

    test("renders correctly ", async () => {

        const { getByText } = render(
            <Router  >
                <UCSBSubjectForm />
            </Router>
        );
        await waitFor(() => expect(getByText(/Id/)).toBeInTheDocument());
        await waitFor(() => expect(getByText(/Subject code/)).toBeInTheDocument());
        await waitFor(() => expect(getByText(/Subject translation/)).toBeInTheDocument());
        await waitFor(() => expect(getByText(/Dept code/)).toBeInTheDocument());
        await waitFor(() => expect(getByText(/College code/)).toBeInTheDocument());
        await waitFor(() => expect(getByText(/Related dept code/)).toBeInTheDocument());
        await waitFor(() => expect(getByText(/Inactive/)).toBeInTheDocument());
        await waitFor(() => expect(getByText(/Create/)).toBeInTheDocument());
    });


    test("renders correctly when passing in a UCSBSubject ", async () => {

        const { getByText, getByTestId } = render(
            <Router  >
                <UCSBSubjectForm initialUCSBSubject={ucsbSubjectsFixtures.oneSubject} />
            </Router>
        );
        await waitFor(() => expect(getByTestId(/UCSBSubjectForm-id/)).toBeInTheDocument());
        expect(getByText(/Id/)).toBeInTheDocument();
        expect(getByTestId(/UCSBSubjectForm-id/)).toHaveValue("1");
    });

    test("Correct Error messsages on bad input", async () => {

        const { getByTestId, getByText } = render(
            <Router  >
                <UCSBSubjectForm />
            </Router>
        );
        await waitFor(() => expect(getByTestId("UCSBSubjectForm-subjectCode")).toBeInTheDocument());
        await waitFor(() => expect(getByTestId("UCSBSubjectForm-subjectTranslation")).toBeInTheDocument());
        await waitFor(() => expect(getByTestId("UCSBSubjectForm-deptCode")).toBeInTheDocument());
        await waitFor(() => expect(getByTestId("UCSBSubjectForm-collegeCode")).toBeInTheDocument());
        await waitFor(() => expect(getByTestId("UCSBSubjectForm-relatedDeptCode")).toBeInTheDocument());
        await waitFor(() => expect(getByTestId("UCSBSubjectForm-inactive")).toBeInTheDocument());

        const inactiveField = getByTestId("UCSBSubjectForm-inactive");
        const submitButton = getByTestId("UCSBSubjectForm-submit");

        fireEvent.change(inactiveField, { target: { value: 'bad-input' } });
        fireEvent.click(submitButton);

        await waitFor(() => expect(getByText(/Inactive status must be a boolean/)).toBeInTheDocument());
    });

    test("Correct Error messsages on missing input", async () => {

        const { getByTestId, getByText } = render(
            <Router  >
                <UCSBSubjectForm />
            </Router>
        );
        await waitFor(() => expect(getByTestId("UCSBSubjectForm-submit")).toBeInTheDocument());
        const submitButton = getByTestId("UCSBSubjectForm-submit");

        fireEvent.click(submitButton);

        await waitFor(() => expect(getByText(/Subject code is required./)).toBeInTheDocument());
        expect(getByText(/Subject translation is required./)).toBeInTheDocument();
        expect(getByText(/Dept code is required./)).toBeInTheDocument();
        expect(getByText(/College code is required./)).toBeInTheDocument();
        expect(getByText(/Related dept code is required./)).toBeInTheDocument();
        expect(getByText(/Inactive status is required./)).toBeInTheDocument();

    });

    test("No Error messsages on good input", async () => {

        const mockSubmitAction = jest.fn();


        const { getByTestId, queryByText } = render(
            <Router  >
                <UCSBSubjectForm submitAction={mockSubmitAction} />
            </Router>
        );
        await waitFor(() => expect(getByTestId("UCSBSubjectForm-subjectCode")).toBeInTheDocument());
        await waitFor(() => expect(getByTestId("UCSBSubjectForm-subjectTranslation")).toBeInTheDocument());
        await waitFor(() => expect(getByTestId("UCSBSubjectForm-deptCode")).toBeInTheDocument());
        await waitFor(() => expect(getByTestId("UCSBSubjectForm-collegeCode")).toBeInTheDocument());
        await waitFor(() => expect(getByTestId("UCSBSubjectForm-relatedDeptCode")).toBeInTheDocument());
        await waitFor(() => expect(getByTestId("UCSBSubjectForm-inactive")).toBeInTheDocument());

        const subjectCodeField = getByTestId("UCSBSubjectForm-subjectCode");
        const subjectTranslationField = getByTestId("UCSBSubjectForm-subjectTranslation");
        const deptCodeField = getByTestId("UCSBSubjectForm-deptCode");
        const collegeCodeField = getByTestId("UCSBSubjectForm-collegeCode");
        const relatedDeptCodeField = getByTestId("UCSBSubjectForm-relatedDeptCode");
        const inactiveField = getByTestId("UCSBSubjectForm-inactive");
        const submitButton = getByTestId("UCSBSubjectForm-submit");

        fireEvent.change(subjectCodeField, { target: { value: 'subject code 1' } });
        fireEvent.change(subjectTranslationField, { target: { value: 'subject translation 1' } });
        fireEvent.change(deptCodeField, { target: { value: 'dept code 1' } });
        fireEvent.change(collegeCodeField, { target: { value: 'college code 1' } });
        fireEvent.change(relatedDeptCodeField, { target: { value: 'related dept code 1' } });
        fireEvent.change(inactiveField, { target: { value: 'true' } });
        fireEvent.click(submitButton);

        await waitFor(() => expect(mockSubmitAction).toHaveBeenCalled());

        await waitFor(() => expect(queryByText(/Subject code is required./)).not.toBeInTheDocument());
        expect(queryByText(/Subject translation is required./)).not.toBeInTheDocument();
        expect(queryByText(/Dept code is required./)).not.toBeInTheDocument();
        expect(queryByText(/College code is required./)).not.toBeInTheDocument();
        expect(queryByText(/Related dept code is required./)).not.toBeInTheDocument();
        expect(queryByText(/Inactive status is required./)).not.toBeInTheDocument();
    });


    test("Test that navigate(-1) is called when Cancel is clicked", async () => {

        const { getByTestId } = render(
            <Router  >
                <UCSBSubjectForm />
            </Router>
        );
        await waitFor(() => expect(getByTestId("UCSBSubjectForm-cancel")).toBeInTheDocument());
        const cancelButton = getByTestId("UCSBSubjectForm-cancel");

        fireEvent.click(cancelButton);

        await waitFor(() => expect(mockedNavigate).toHaveBeenCalledWith(-1));

    });

});


