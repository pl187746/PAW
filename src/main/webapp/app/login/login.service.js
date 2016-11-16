(function() {
    'use strict';

    angular
        .module('trello')
        .factory('LoginService', LoginService);

    LoginService.$inject = ['$rootScope', '$q', '$http', 'Subject'];

    function LoginService ($rootScope, $q, $http, Subject) {
        var service = {
            getToken: getToken,
            hasValidToken: hasValidToken,
            login: login,
            logout: logout,
            isAuthenticated: isAuthenticated,
			identity: identity,
            register: register
        };

        return service;

        function getToken () {
            var token = localStorage.getItem('authenticationToken');
            return token;
        }

        function hasValidToken () {
            var token = this.getToken();
            return !!token;
        }

        function login (credentials) {
            var deferred = $q.defer();

            var data = 'login=' + encodeURIComponent(credentials.login) +
                '&password=' + encodeURIComponent(credentials.password) +
                '&remember-me=' + credentials.rememberMe + '&submit=Login';

            return $http.post('/authentication', data, {
                headers: {
                    'Content-Type': 'application/x-www-form-urlencoded'
                }
            }).success(function (response) {
                Subject.identity(true).then(function(account) {
                    deferred.resolve(data);
                });
            });
        }

        function logout () {
            $http.post('/logout').success(function (response) {
                localStorage.removeItem('authenticationToken');
                return response;
            });
        }

        function register(credentials) {
            return $http({
                method: 'POST',
                url: '/register',
                data: credentials
            })
        }

        function isAuthenticated() {
            return Subject.isAuthenticated();
        }

		function identity(refresh) {
			return Subject.identity(refresh);
		}
    }
})();
