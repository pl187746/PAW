(function() {
    'use strict';

    angular
        .module('trello')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider.state('register', {
            parent: 'app',
            url: '/register',
            data: {
                authorities: [],
                pageTitle: 'Registration'
            },
            views: {
                'content@': {
                    templateUrl: 'app/register/register.html',
                    controller: 'RegisterController',
                }
            }
        });
    }
})();