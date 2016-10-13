(function () {
    'use strict';

    angular
        .module('trello')
        .factory('List', List);

    List.$inject = ['$resource'];

    function List ($resource) {
        var service = $resource('/lists/:id', {}, {
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