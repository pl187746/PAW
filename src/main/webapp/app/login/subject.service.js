(function() {
    'use strict';

    angular
        .module('trello')
        .factory('Subject', Subject);

    Subject.$inject = ['$q', 'Account'];

    function Subject ($q, Account) {
        var _identity,
            _authenticated = false;

        var service = {
            authenticate: authenticate,
            identity: identity,
            isAuthenticated: isAuthenticated,
            isIdentityResolved: isIdentityResolved
        };

        return service;

        function authenticate (identity) {
            _identity = identity;
            _authenticated = identity !== null;
        }

        function identity (refresh) {
            var deferred = $q.defer();

            if (refresh === true) {
                _identity = undefined;
            }

            if (angular.isDefined(_identity)) {
                deferred.resolve(_identity);

                return deferred.promise;
            }

            Account.get().$promise
                .then(onSuccess)
                .catch(onError);

            return deferred.promise;

            function onSuccess (account) {
                _identity = account.data;
                _authenticated = true;
                deferred.resolve(_identity);
            }

            function onError () {
                _identity = null;
                _authenticated = false;
                deferred.resolve(_identity);
            }
        }

        function isAuthenticated () {
            return _authenticated;
        }

        function isIdentityResolved () {
            return angular.isDefined(_identity);
        }
    }
})();
