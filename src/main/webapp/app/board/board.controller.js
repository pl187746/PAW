(function() {
    'use strict';

    angular
        .module('trello')
        .controller('BoardsController', BoardsController);

    BoardsController.$inject = ['$rootScope', '$scope', 'Board', 'Card'];

    function BoardsController ($rootScope, $scope, Board, Card) {
        $scope.setTab = setTab;
        $scope.isSet = isSet;
        $scope.addList = addList;
        $scope.getBoards = loadAll;

        // Cards
        $scope.updateCard = updateCard;
        $scope.removeCard = removeCard;

        $scope.tab = 1;
        $scope.boards = [];

        loadAll();

        function setTab(newTab) {
            $scope.tab = newTab;
        }

        function isSet(tabNum) {
            return $scope.tab === tabNum;
        }

        function addList () {
            // TODO implement
        }

        function loadAll() {
            Board.query(onSuccess, onError);
            
            function onSuccess(data) {
                console.log('Loaded boards of size: ' + data.length)
                $scope.boards = data;
            }
            
            function onError() {
                console.log('Error while loading boards');
            }
        }

        function updateCard(card, boardIndex, listIndex, cardIndex) {
            console.log('Change card request for boardIndex: ' + boardIndex +
                ", listIndex: " + listIndex + ", cardIndex: " + cardIndex);
            Card.update(card, onSuccess, onError);

            function onSuccess() {
                console.log('Updated card with index ' + card.id);
            }

            function onError() {
                console.log('Error while updating card with index ' + card.id);
            }
        }

        function removeCard(card, boardIndex, listIndex, cardIndex) {
            console.log('Remove card request for boardIndex: ' + boardIndex +
                ", listIndex: " + listIndex + ", cardIndex: " + cardIndex);
            var cards = getCards(boardIndex, listIndex);
            cards.splice(cardIndex, 1);

            Card.delete({id: card.id}, onSuccess, onError);

            function onSuccess() {
                console.log('Deleted card with index ' + card.id);
            }

            function onError() {
                console.log('Error while removing card');
            }
        }

        function getCards(boardIndex, listIndex) {
            var board = $scope.boards[boardIndex];
            return board.lists[listIndex].cards;
        }
    }
})();

