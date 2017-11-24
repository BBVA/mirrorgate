const api = require('./backend-api');
const data = require('./fixtures');

api.stories.send(data.stories.backlog,{status:'IN_PROGRESS'})
.then(function () {
    setTimeout(function () {
        api.restore();
    }, 10000);
})
.catch(function (err) {
    console.error(JSON.stringify(err));
});


