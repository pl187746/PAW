(function() {
    'use strict';

    angular
        .module('trello')
        .controller('NavbarController', NavbarController);

    NavbarController.$inject = ['$rootScope', '$scope', '$state','$translate', 'Board', 'LoginService', 'Subject'];

    function NavbarController ($rootScope, $scope, $state, $translate, Board, LoginService, Subject) {
        $scope.logout = logout;
        $scope.isAuthenticated = isAuthenticated;
        $scope.changeLanguage = changeLanguage;

        $scope.$state = $state;
        $rootScope.boards = null;

        $scope.user = null;
        $scope.isAuthenticated = null;

        $scope.$on('authenticationSuccess', function() {
            getAccount();
        });

        getAccount();
        loadBoards();

        function getAccount() {
            Subject.identity().then(function(account) {
                if (account !== null) {
                    console.log('Loaded user: ' + account.login);
                }
                $scope.user = account;
                $scope.isAuthenticated = Subject.isAuthenticated;
            });
        }

        function loadBoards() {
            Board.query(onSuccess, onError);

            function onSuccess(data) {
                $rootScope.boards = data;
                console.log('Loaded boards of size: ' + $rootScope.boards.length);
            }

            function onError() {
                console.log('Error while loading boards');
            }
        }

        function logout() {
            LoginService.logout();
            Subject.authenticate(null);
        }

        function isAuthenticated() {
            return LoginService.isAuthenticated();
        }

        function changeLanguage(langKey) {
            $translate.use(langKey);
        };
    }
})();