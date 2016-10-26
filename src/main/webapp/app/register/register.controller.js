(function() {
    'use strict';

    angular
        .module('trello')
        .controller('RegisterController', RegisterController);

    RegisterController.$inject = ['$scope', '$state', '$http', 'LoginService'];

    function RegisterController ($scope, $state, $http, LoginService) {
        $scope.register = register;

        $scope.passwordDoesntMatch = false;
        $scope.loginTaken = false;
        $scope.emailTaken = false;
        $scope.success = false;
        $scope.error = false;

        $scope.confirmPassword = '';

        // User
        $scope.login = '';
        $scope.email = '';
        $scope.password = '';

        if (LoginService.isAuthenticated()) {
            $state.go('home');
        }

        function register () {
            if ($scope.password !== $scope.confirmPassword) {
                $scope.passwordDoesntMatch = true;
            } else {
                $scope.passwordDoesntMatch = $scope.loginTaken = $scope.emailTaken = $scope.success = $scope.error = false;

                $http({
                    method: 'POST',
                    url: '/register',
                    data: {
                        login: $scope.login,
                        email: $scope.email,
                        password: $scope.password
                    }
                }).then(function successCallback(response) {
                    console.log('User registered');
                    $scope.success = true;
                }, function errorCallback(response) {
                    console.log('Error while registering user');
                    $scope.success = false;
                    if (response.status === 409) {
                        var errorMessage = response.data.message;
                        if (errorMessage === 'login exists') {
                            console.log('Login is already in use');
                            $scope.loginTaken = true;
                        } if (errorMessage === 'email exists') {
                            console.log('Email is already in use');
                            $scope.emailTaken = true;
                        } else if (errorMessage === 'login and email exists') {
                            console.log('Login and email are already in use');
                            $scope.loginTaken = true;
                            $scope.emailTaken = true;
                        }
                    } else {
                        $scope.error = true;
                    }
                });
            }
        }
    }
})();