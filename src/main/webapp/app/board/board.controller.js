(function() {
    'use strict';

    angular
        .module('trello')
        .controller('BoardsController', BoardsController);

    BoardsController.$inject = ['$rootScope', '$scope', 'Board'];

    function BoardsController ($rootScope, $scope, Board) {
        $scope.setTab = setTab;
        $scope.isSet = isSet;
        $scope.addList = addList;
        $scope.getBoards = loadAll;

        $scope.tab = 1;
        $scope.boards = [];

        loadAll();

        function setTab(newTab) {
            $scope.tab = newTab;
        };

        function isSet(tabNum) {
            return $scope.tab === tabNum;
        };

        function addList () {
            // TODO implement
        };

        function loadAll() {
            Board.query(onSuccess, onError);
            
            function onSuccess(data) {
                $scope.boards = data;
            }
            
            function onError() {
                console.log('Error while loading boards');
            }
        };
    }
})();

