(function() {
    'use strict';

    angular
        .module('trello')
        .controller('NavbarController', NavbarController);

    NavbarController.$inject = ['$rootScope', '$scope', '$state', 'User', 'Board'];

    function NavbarController ($rootScope, $scope, $state, User, Board) {
        $scope.login = login;
        $scope.logout = logout;
        $scope.isAuthenticated = isAuthenticated;

        $scope.$state = $state;
        $scope.user = null;
        $scope.boards = null;

        loadBoards();

        function loadBoards() {
            Board.query(onSuccess, onError);

            function onSuccess(data) {
                $scope.boards = data;
                console.log('Loaded boards of size: ' + $scope.boards.length);
            }

            function onError() {
                console.log('Error while loading boards');
            }
        }

        function login() {
            User.get({id : "1"}, onSuccess, onError);

            function onSuccess(data) {
                console.log('User ' + data.login + ' logged in');
                $scope.user = data;
                localStorage.setItem('user', data);
				$rootScope.$emit('loginEvent', data);
            }

            function onError() {
                console.log('Error while loading user');
            }
        }

        function logout() {
            console.log('User ' + $scope.user.login + ' logged out');
            $scope.user = null;
            localStorage.removeItem('user');
			$rootScope.$emit('loginEvent', null);
        }

        function isAuthenticated() {
            return $scope.user != null;
        }
    }
})();