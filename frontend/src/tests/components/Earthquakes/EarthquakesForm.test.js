import { render, waitFor, fireEvent } from "@testing-library/react";
import EarthquakesForm from "main/components/Earthquakes/EarthquakesForm";
import { BrowserRouter as Router } from "react-router-dom";

const mockedNavigate = jest.fn();

jest.mock('react-router-dom', () => ({
    ...jest.requireActual('react-router-dom'),
    useNavigate: () => mockedNavigate
}));


describe("EarthquakesForm tests", () => {

    test("renders correctly ", async () => {

        const { getByText } = render(
            <Router  >
                <EarthquakesForm />
            </Router>
        );
        await waitFor(() => expect(getByText(/Distance in km from Storke Tower/)).toBeInTheDocument());
        await waitFor(() => expect(getByText(/Retrieve/)).toBeInTheDocument());
    });


    test("Correct Error messsages on bad input", async () => {

        const { getByTestId, getByText } = render(
            <Router  >
                <EarthquakesForm />
            </Router>
        );
        await waitFor(() => expect(getByTestId("EarthquakesForm-distance")).toBeInTheDocument());
        const distanceField = getByTestId("EarthquakesForm-distance");
        const magField = getByTestId("EarthquakesForm-mag");
        const retrieveButton = getByTestId("EarthquakesForm-retrieve");

        // fireEvent.change(distanceField, { target: { value: null } });
        // fireEvent.change(magField, { target: { value: null } });
        fireEvent.click(retrieveButton);

        await waitFor(() => expect(getByText(/Distance is required./)).toBeInTheDocument());
        expect(getByText(/Minimum Magnitude is required./)).toBeInTheDocument();
    });

    test("No Error messsages on good input", async () => {

        const mockSubmitAction = jest.fn();


        const { getByTestId, queryByText } = render(
            <Router  >
                <EarthquakesForm submitAction={mockSubmitAction} />
            </Router>
        );
        await waitFor(() => expect(getByTestId("EarthquakesForm-distance")).toBeInTheDocument());
        const distanceField = getByTestId("EarthquakesForm-distance");
        const magField = getByTestId("EarthquakesForm-mag");
        const retrieveButton = getByTestId("EarthquakesForm-retrieve");

        fireEvent.change(distanceField, { target: { value: '200' } });
        fireEvent.change(magField, { target: { value: '1' } });
        fireEvent.click(retrieveButton);

        await waitFor(() => expect(mockSubmitAction).toHaveBeenCalled());

        expect(queryByText(/Distance is required./)).not.toBeInTheDocument();
        expect(queryByText(/Minimum Magnitude is required./)).not.toBeInTheDocument();

    });


    test("Test that navigate(-1) is called when Cancel is clicked", async () => {

        const { getByTestId } = render(
            <Router  >
                <EarthquakesForm />
            </Router>
        );
        await waitFor(() => expect(getByTestId("EarthquakesForm-cancel")).toBeInTheDocument());
        const cancelButton = getByTestId("EarthquakesForm-cancel");

        fireEvent.click(cancelButton);

        await waitFor(() => expect(mockedNavigate).toHaveBeenCalledWith(-1));

    });

});


