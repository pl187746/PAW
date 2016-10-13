(function() {
    'use strict';

    angular
        .module('trello')
        .controller('BoardsController', BoardsController);

    BoardsController.$inject = ['$rootScope', '$scope', 'Board', 'Card', 'List'];

    function BoardsController ($rootScope, $scope, Board, Card, List) {
        $scope.setTab = setTab;
        $scope.isSet = isSet;
        $scope.addList = addList;
        $scope.getBoards = loadAll;

        // Cards
        $scope.updateCard = updateCard;
        $scope.removeCard = removeCard;
        $scope.addCard = addCard;

        $scope.removeList = removeList;
        $scope.updateList = updateList;

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

        function removeList(list, boardIndex, listIndex) {
            console.log('Remove list request for boardIndex: ' + boardIndex + ", listIndex: " + listIndex);
            var lists = getLists(boardIndex);

            List.delete({id: list.id}, onSuccess, onError);

            function onSuccess() {
                console.log('Deleted list with index ' + list.id);
                lists.splice(listIndex, 1);
            }

            function onError() {
                console.log('Error while removing list');
            }
        }

        function updateList(list, boardIndex, listIndex) {
            console.log('Update list request for boardIndex: ' + boardIndex + ", listIndex: " + listIndex);
            List.update(list, onSuccess, onError);

            function onSuccess() {
                console.log('Updated list with index ' + list.id);
            }

            function onError() {
                console.log('Error while updating list with index ' + list.id);
            }
        }

        function updateCard(card, boardIndex, listIndex, cardIndex) {
            console.log('Update card request for boardIndex: ' + boardIndex +
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

        function addCard(boardIndex, listIndex) {
            var cards = getCards(boardIndex, listIndex);
            var listId = getList(boardIndex, listIndex).id;

            Card.save( {listId : listId, name : ''}, onSuccess, onError);

            function onSuccess(response) {
                console.log('Added new card to board with index ' + boardIndex + ' and list with index ' + listIndex);
                cards.push(response);
            }

            function onError() {
                console.log('Error while adding card')
            }
        }

        function getCards(boardIndex, listIndex) {
            var list = getList(boardIndex, listIndex);
            return list.cards;
        }

        function getList(boardIndex, listIndex) {
            var board = getBoard(boardIndex);
            return board.lists[listIndex];
        }

        function getBoard(boardIndex) {
            return $scope.boards[boardIndex];
        }

        function getLists(boardIndex) {
            var board = getBoard(boardIndex);
            return board.lists;
        }
    }
})();

