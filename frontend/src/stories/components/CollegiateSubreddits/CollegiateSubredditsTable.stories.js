import React from 'react';

import CollegiateSubredditsTable from "main/components/CollegiateSubreddits/CollegiateSubredditsTable";
import { collegiateSubredditsFixtures } from 'fixtures/collegiateSubredditsFixtures';

export default {
    title: 'components/CollegiateSubreddits/CollegiateSubredditsTable',
    component: CollegiateSubredditsTable
};

const Template = (args) => {
    return (
        <CollegiateSubredditsTable {...args} />
    )
};

export const Empty = Template.bind({});

Empty.args = {
    subreddits: []
};

export const ThreeSubreddits = Template.bind({});

ThreeSubreddits.args = {
    subreddits: collegiateSubredditsFixtures.threeSubreddits
};
