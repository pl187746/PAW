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

        function register() {
            LoginService.register($scope.credentials)
                .then(function () {
                    $scope.authenticationError = false;
                    $scope.authenticationSuccess = true;
                    console.log($state.current.name);
                    if ($state.current.name === 'register' || $state.current.name === 'login') {
                        $state.go('home');
                    }
                })
                .catch(function (response) {
                    if (response.status >= 400) {
                        $scope.authenticationError = true;
                    }
                });
        }

        function register () {
            if (!passwordsMatches()) {
                $scope.passwordDoesntMatch = true;
                return;
            }

            resetStates();

            var credentials = {
                login: $scope.login,
                email: $scope.email,
                password: $scope.password
            };

            LoginService.register(credentials)
                .then(function () {
                    console.log('User registered');
                    $scope.success = true;
                })
                .catch(function (response) {
                    console.log('Error while registering user');
                    $scope.success = false;

                    if (response.status === 409) { // Conflict code
                        var errorMessage = response.data.message;

                        if (errorMessage === 'login exists') {
                            $scope.loginTaken = true;
                        } if (errorMessage === 'email exists') {
                            $scope.emailTaken = true;
                        } else if (errorMessage === 'login and email exists') {
                            $scope.loginTaken = true;
                            $scope.emailTaken = true;
                        }
                    } else {
                        $scope.error = true;
                    }
                });
            }

            function passwordsMatches() {
                return $scope.password === $scope.confirmPassword;
            }

            function resetStates() {
                $scope.passwordDoesntMatch = $scope.loginTaken = $scope.emailTaken = $scope.success = $scope.error = false;
            }
    }
})();