(function() {
    'use strict';

    angular
        .module('trello')
        .factory('LoginService', LoginService);

    LoginService.$inject = ['$rootScope', '$http'];

    function LoginService ($rootScope, $http) {
        var service = {
            login: login,
            logout: logout,
            isAuthenticated: isAuthenticated,
            register: register,
            loadUser: loadUser
        };

        return service;

        function login(credentials) {
            function onSuccess(response) {
                console.log(response);
                $rootScope.user = response.data;
                var userJSON = JSON.stringify(response.data);

                if (credentials.rememberMe) {
                    localStorage.setItem('user', userJSON);
                } else {
                    sessionStorage.setItem('user', userJSON);
                }

                console.log('Logged in: ' + $rootScope.user.login);
            }

            return $http({
                url: '/login',
                method: "POST",
                data: credentials}).then(onSuccess);
        }

        function logout() {
            console.log('User ' + $rootScope.user.login + ' logged out');
            $rootScope.user = null;
            sessionStorage.removeItem('user');
            localStorage.removeItem('user');
        }

        function isAuthenticated() {
            return $rootScope.user != null;
        }

        function register(credentials) {
            return $http({
                method: 'POST',
                url: '/register',
                data: credentials
            })
        }

        function loadUser() {
            var userAsJSON = sessionStorage.user || localStorage.user;
            $rootScope.user = angular.fromJson(userAsJSON);
        }
    }
})();
