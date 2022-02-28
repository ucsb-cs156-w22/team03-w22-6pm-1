import { render, waitFor, fireEvent } from "@testing-library/react";
import EarthquakesRetrievePage from "main/pages/Earthquakes/EarthquakesRetrievePage";
import { QueryClient, QueryClientProvider } from "react-query";
import { MemoryRouter } from "react-router-dom";

import { apiCurrentUserFixtures } from "fixtures/currentUserFixtures";
import { systemInfoFixtures } from "fixtures/systemInfoFixtures";
import axios from "axios";
import AxiosMockAdapter from "axios-mock-adapter";
import { useBackendMutation } from "main/utils/useBackend";
import { earthquakesFixtures } from "fixtures/earthquakesFixtures";

const mockToast = jest.fn();
jest.mock('react-toastify', () => {
    const originalModule = jest.requireActual('react-toastify');
    return {
        __esModule: true,
        ...originalModule,
        toast: (x) => mockToast(x)
    };
});

const mockNavigate = jest.fn();
jest.mock('react-router-dom', () => {
    const originalModule = jest.requireActual('react-router-dom');
    return {
        __esModule: true,
        ...originalModule,
        Navigate: (x) => { mockNavigate(x); return null; }
    };
});

describe("EarthquakesRetrievePage tests", () => {

    const axiosMock = new AxiosMockAdapter(axios);

    beforeEach(() => {
        axiosMock.reset();
        axiosMock.resetHistory();
        axiosMock.onGet("/api/currentUser").reply(200, apiCurrentUserFixtures.userOnly);
        axiosMock.onGet("/api/systemInfo").reply(200, systemInfoFixtures.showingNeither);
    });

    test("renders without crashing", () => {
        const queryClient = new QueryClient();
        render(
            <QueryClientProvider client={queryClient}>
                <MemoryRouter>
                    <EarthquakesRetrievePage />
                </MemoryRouter>
            </QueryClientProvider>
        );
    });

    test("when you fill in the form and hit submit, it makes a request to the backend", async () => {

        const queryClient = new QueryClient();
        const earthquake = {
            distance: "1",
            mag: ".1"
        };

        axiosMock.onGet("/api/earthquakes/retrieve").reply(200, {"type":"FeatureCollection","metadata":{"generated":1646006038000,"url":"https://earthquake.usgs.gov/fdsnws/event/1/query?format=geojson&minmagnitude=.1&maxradiuskm=1&latitude=34.4140&longitude=-119.8489","title":"USGS Earthquakes","status":200,"api":"1.13.3","count":0},"features":[]});

        const { getByTestId } = render(
            <QueryClientProvider client={queryClient}>
                <MemoryRouter>
                    <EarthquakesRetrievePage />
                </MemoryRouter>
            </QueryClientProvider>
        );

        await waitFor(() => {
            expect(getByTestId("EarthquakesForm-distance")).toBeInTheDocument();
        });

        const distanceField = getByTestId("EarthquakesForm-distance");
        const magField = getByTestId("EarthquakesForm-mag");
        const retrieveButton = getByTestId("EarthquakesForm-retrieve");

        fireEvent.change(distanceField, { target: { value: '1' } });
        fireEvent.change(magField, { target: { value: '.1' } });

        expect(retrieveButton).toBeInTheDocument();

        fireEvent.click(retrieveButton);

        await waitFor(() => expect(axiosMock.history.get.length).toBe(3));

        expect(axiosMock.history.get[2].params).toEqual(
            {
                "distanceKm": "1",
                "minMagnitude": ".1",
            });


        expect(mockToast).toBeCalledWith("0 Earthquakes retrieved");
        expect(mockNavigate).toBeCalledWith({ "to": "/earthquakes/list" });
    });


});