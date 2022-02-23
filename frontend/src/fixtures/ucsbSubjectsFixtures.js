const ucsbSubjectsFixtures = {
    oneSubject: {
        "id": 1,
        "subjectCode": "111",
        "subjectTranslation": "Subject 1",
        "deptCode": "Dept 1",
        "collegeCode": "College 1",
        "relatedDeptCode": "Related Dept 1",
        "inactive": false
    },
    threeSubjects: [
        {
            "id": 1,
            "subjectCode": "111",
            "subjectTranslation": "Subject 1",
            "deptCode": "Dept 1",
            "collegeCode": "College 1",
            "relatedDeptCode": "Related Dept 1",
            "inactive": false
        },
        {
            "id": 2,
            "subjectCode": "222",
            "subjectTranslation": "Subject 2",
            "deptCode": "Dept 2",
            "collegeCode": "College 2",
            "relatedDeptCode": "Related Dept 2",
            "inactive": true
        },
        {
            "id": 3,
            "subjectCode": "333",
            "subjectTranslation": "Subject 3",
            "deptCode": "Dept 3",
            "collegeCode": "College 3",
            "relatedDeptCode": "Related Dept 3",
            "inactive": false
        }
    ]
};


export { ucsbSubjectsFixtures };