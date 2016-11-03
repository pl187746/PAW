(function() {
    'use strict';

    angular
        .module('trello')
        .controller('NavbarController', NavbarController);

    NavbarController.$inject = ['$rootScope', '$scope', '$state', 'Board', 'LoginService'];

    function NavbarController ($rootScope, $scope, $state, Board, LoginService) {
        $scope.logout = logout;
        $scope.isAuthenticated = isAuthenticated;

        $scope.$state = $state;
        $rootScope.boards = null;
        $rootScope.user = null;

        loadBoards();

        loadLoggedUser();

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

        function loadLoggedUser() {
            LoginService.loadUser();
        }

        function logout() {
            LoginService.logout();
        }

        function isAuthenticated() {
            return LoginService.isAuthenticated();
        }
    }
})();