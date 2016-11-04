(function () {
    'use strict';

    angular
        .module('trello')
        .factory('Record', Record);

    Record.$inject = ['$resource'];

    function Record ($resource) {
        var service = $resource('/records', {}, {
            'get': {
                method: 'GET',
				isArray: true,
                transformResponse: function (data) {
                    data = angular.fromJson(data);
                    return data;
                }
            },
        });

        return service;
    }
})();