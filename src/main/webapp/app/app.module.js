(function() {
    'use strict';

    angular
        .module('trello', ['ngResource', 'ui.router', 'ui.bootstrap', 'ngFileUpload','pascalprecht.translate', 'ui.bootstrap.datetimepicker'])
        .run(run);

    run.$inject = ['stateHandler'];

    function run(stateHandler) {
        stateHandler.initialize();
    }
})();
