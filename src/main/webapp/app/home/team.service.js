(function () {
    'use strict';

    angular
        .module('trello')
        .factory('Team', Team);

    Team.$inject = ['$resource'];

    function Team ($resource) {
        var service = $resource('/teams/:id', {}, {
            'query': {method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    data = angular.fromJson(data);
                    return data;
                }
            },
            'save': { method:'POST' },
            'update': { method:'PUT' },
            'delete':{ method:'DELETE'}
        });

        return service;
    }
})();