(function() {
    'use strict';

    angular
        .module('trello')
        .controller('LoginController', LoginController);

    LoginController.$inject = ['$rootScope', '$scope', '$state', 'LoginService'];

    function LoginController ($rootScope, $scope, $state, LoginService) {
        $scope.login = login;

        $scope.authenticationError = false;
        $scope.authenticationSuccess = false;

        $scope.credentials = {
            login: '',
            password: '',
            rememberMe: false
        };

        if (LoginService.isAuthenticated()) {
            $state.go('home');
        }

        function login() {
            LoginService.login($scope.credentials)
                .then(function () {
                    $scope.authenticationError = false;
                    $scope.authenticationSuccess = true;
                    $rootScope.$broadcast('authenticationSuccess');
                    if ($state.current.name === 'register' || $state.current.name === 'login') {
                        $state.go('home');
                    }
                })
                .catch(function () {
                    if (response.status >= 400) {
                        $scope.authenticationError = true
                    }}
                );
        }
    }
})();