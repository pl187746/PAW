/**
 * Created by Krystian on 2016-11-06.
 */
(function () {
    'use strict';

    angular
        .module('trello')
        .factory('Comment', Comment);

    Comment.$inject = ['$resource'];

    function Comment ($resource) {
        var service = $resource('/comments/:id', {}, {
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