(function () {
    'use strict';

    angular
        .module('trello')
        .factory('Label', Label);

    Label.$inject = ['$resource'];

    function Label ($resource) {
        var service = $resource('/labels/:id', {}, {
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