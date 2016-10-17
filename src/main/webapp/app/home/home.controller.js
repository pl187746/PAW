(function() {
    'use strict';

    angular
        .module('trello')
        .controller('HomeController', HomeController);

    HomeController.$inject = ['$scope', '$state', 'Board'];

    function HomeController ($scope, $state, Board) {
        $scope.$state = $state;
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
    }
})();