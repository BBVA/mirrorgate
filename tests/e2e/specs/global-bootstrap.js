var api = require('../support/backend-api');


beforeEach(function () {
    return api.restore();
});

afterAll(function () {
    return api.restore();
});
