(function() {
    'use strict';

    angular
        .module('trello')
        .factory('Account', Account);

    Account.$inject = ['$resource'];

    function Account ($resource) {
        var service = $resource('account', {}, {
            'get': { method: 'GET', params: {}, isArray: false,
                interceptor: {
                    response: function(response) {
                        return response;
                    }
                }
            }
        });

        return service;
    }
})();