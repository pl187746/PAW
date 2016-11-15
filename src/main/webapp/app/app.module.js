(function() {
    'use strict';

    angular
        .module('trello', ['ngResource', 'ui.router', 'ui.bootstrap', 'ngFileUpload'])
        .run(run);

    run.$inject = ['stateHandler'];

    function run(stateHandler) {
        stateHandler.initialize();
    }
})();
