(function () {
    'use strict';

    angular
        .module('trello')
        .factory('FavBoard', FavBoard);

    FavBoard.$inject = ['$resource'];

    function FavBoard ($resource) {
        var service = $resource('/favboards', {}, {
			'check': {
				method: 'GET',
				transformResonse: function(data) {
					return !!angular.fromJson(data).present;
				}
			},
            'save': { method:'POST' },
            'delete': { method:'DELETE' }
        });

        return service;
    }
})();
