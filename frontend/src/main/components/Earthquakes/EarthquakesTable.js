import React from "react";
import OurTable from "main/components/OurTable";

export default function EarthquakesTable({ earthquakes, currentUser }) {

    const columns = [
        {
            Header: 'id',
            accessor: 'id', // accessor is the "key" in the data
        },
        {
            Header: 'Title',
            accessor: 'title',
        },
        {
            Header: 'Magnitude',
            accessor: 'mag',
        },
        {
            Header: 'Place',
            accessor: 'place',
        },
        {
            Header: 'Time',
            accessor: 'time',
        },

    ];

    // Stryker disable ArrayDeclaration : [columns] and [earthquakes] are performance optimization; mutation preserves correctness
    const memoizedColumns = React.useMemo(() => columns, [columns]);
    const memoizedDates = React.useMemo(() => earthquakes, [earthquakes]);
    // Stryker enable ArrayDeclaration

    return <OurTable
        data={memoizedDates}
        columns={memoizedColumns}
        testid={"EarthquakesTable"}
    />;
};