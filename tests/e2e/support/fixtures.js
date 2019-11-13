const start = new Date((new Date()).getTime() - (1000 * 60 * 60 * 24 * 7));
const end = new Date((new Date()).getTime() + (1000 * 60 * 60 * 24 * 11));

module.exports = {
    stories: {
        backlog: [{
            id: 430210,
            jiraKey: 'DOST05-47',
            name: '[ISW] Build failure rate',
            type: 'Story',
            status: 'BACKLOG',
            estimate: 3,
            sprint: {
                id: 1225,
                name: 'MIRRORGATE_SP4',
                status: 'ACTIVE',
                startDate: start,
                endDate: end,
            },
            project: {
                id: null,
                name: 'MirrorGate'
            },
            keywords: [
                'MirrorGate',
            ],
            collectorId: 'mirrorgate-collectors-jira'
        },
        {
            id: 46093,
            jiraKey: 'DOST05-90',
            name: 'SPIKE: Analyze the cost of migrating',
            type: 'Story',
            status: 'BACKLOG',
            estimate: 3,
            sprint: {
                id: 1225,
                name: 'MIRRORGATE_SP4',
                status: 'ACTIVE',
                startDate: start,
                endDate: end,
            },
            project: {
                id: null,
                name: 'MirrorGate'
            },
            keywords: [
                'MirrorGate',
            ],
            collectorId: 'mirrorgate-collectors-jira'
        },
        {
            id: 46095,
            jiraKey: 'DOST05-92',
            name: 'Login',
            type: 'Story',
            status: 'BACKLOG',
            estimate: 8,
            sprint: {
                id: 1225,
                name: 'MIRRORGATE_SP4',
                status: 'ACTIVE',
                startDate: start,
                endDate: end,
            },
            project: {
                id: null,
                name: 'MirrorGate'
            },
            keywords: [
                'MirrorGate',
            ],
            collectorId: 'mirrorgate-collectors-jira'
        },
        {
            id: 46094,
            jiraKey: 'DOST05-91',
            name: 'Distribute application',
            type: 'Story',
            status: 'BACKLOG',
            estimate: 5,
            sprint: {
                id: 1225,
                name: 'MIRRORGATE_SP4',
                status: 'ACTIVE',
                startDate: start,
                endDate: end,
            },
            project: {
                id: null,
                name: 'MirrorGate'
            },
            keywords: [
                'MirrorGate',
            ],
            collectorId: 'mirrorgate-collectors-jira'
        },
        {
            id: 49140,
            jiraKey: 'DOST05-99',
            name: 'Generate iOS application',
            type: 'Story',
            status: 'BACKLOG',
            estimate: 5,
            sprint: {
                id: 1225,
                name: 'MIRRORGATE_SP4',
                status: 'ACTIVE',
                startDate: start,
                endDate: end,
            },
            project: {
                id: null,
                name: 'MirrorGate'
            },
            keywords: [
                'MirrorGate',
            ],
            collectorId: 'mirrorgate-collectors-jira'
        }]
    }
};
