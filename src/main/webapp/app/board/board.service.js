(function () {
    'use strict';

    angular
        .module('trello')
        .factory('Board', Board);

    Board.$inject = ['$resource'];

    function Board ($resource) {
        var service = $resource('/boards/', {}, {
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