(function() {
    'use strict';

    angular
        .module('trello', ['ngResource', 'ui.router'])
        .run(run);

    run.$inject = ['stateHandler'];

    function run(stateHandler) {
        stateHandler.initialize();
    }
})();
