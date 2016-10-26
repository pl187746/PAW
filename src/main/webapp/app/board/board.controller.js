(function() {
    'use strict';

    angular
        .module('trello')
        .controller('BoardController', BoardController);

    BoardController.$inject = ['$rootScope', '$scope', '$stateParams', 'Board', 'Card', 'List', 'User'];

    function BoardController ($rootScope, $scope, $stateParams, Board, Card, List, User) {
        // Cards
        $scope.updateCard = updateCard;
        $scope.removeCard = removeCard;
        $scope.addCard = addCard;

        // Cards List
        $scope.removeList = removeList;
        $scope.updateList = updateList;
        $scope.addList = addList;
        $scope.archiveList = archiveList;
        $scope.returnArchiveList = returnArchiveList;

        $scope.board = null;
        $scope.archList = [];

        loadBoard($stateParams.id);

        function loadBoard(id) {
            Board.get({"id" : id}, onSuccess, onError);

            function onSuccess(data) {
                $scope.board = data;
                console.log('Loaded board of of name: ' + $scope.board.name)
            }

            function onError(response) {
                console.log('Error while loading board');

                var boardId = $stateParams.id;
                if (response.status === 404 || response.status === 400) {
                    $scope.error = 'Invalid board id : ' + boardId;
                    console.log($scope.error);
                }
            }
        }

        function removeList(list) {
            var lists = getLists();
            var index = lists.indexOf(list);
            console.log('Delete list request for listIndex: ' + index);


            List.delete({id: list.id}, onSuccess, onError);

            function onSuccess() {
                console.log('Deleted list with index ' + list.id);
                lists.splice(index, 1);

            }

            function onError() {
                console.log('Error while removing list');
            }
        }

        function updateList(list) {
            console.log('Update list request for listIndex: ' + getLists().indexOf(list));
            List.update(list, onSuccess, onError);

            function onSuccess() {
                console.log('Updated list with index ' + list.id);
            }

            function onError() {
                console.log('Error while updating list with index ' + list.id);
            }
        }

        function addList() {
            var lists = getLists();

            List.save( {boardId : $scope.board.id, name : ''}, onSuccess, onError);

            function onSuccess(response) {
                console.log('Added new list to board with id ' + $scope.board.id);
                lists.push(response);
            }

            function onError() {
                console.log('Error while adding list')
            }
        }

        function archiveList(list){
            list.archive = true;
            updateList(list);
        }

        function returnArchiveList(list){
            list.archive = false;
            updateList(list);
        }

        function updateCard(card,list) {
            var listIndex = getLists().indexOf(list);
            var cards = getCards(listIndex);
            var cardIndex = cards.indexOf(card);
            console.log('Update card request for listIndex: ' + listIndex + ", cardIndex: " + cardIndex);
            Card.update(card, onSuccess, onError);

            function onSuccess() {
                console.log('Updated card with index ' + card.id);
            }

            function onError() {
                console.log('Error while updating card with index ' + card.id);
            }
        }

        function removeCard(card,list) {
            var listIndex = getLists().indexOf(list);
            var cards = getCards(listIndex);
            var cardIndex = cards.indexOf(card);
            console.log("Remove card request for listIndex: " + listIndex + ", cardIndex: " + cardIndex);
            cards.splice(cardIndex, 1);

            Card.delete({id: card.id}, onSuccess, onError);

            function onSuccess() {
                console.log('Deleted card with index ' + card.id);
            }

            function onError() {
                console.log('Error while removing card');
            }
        }

        function addCard(list) {
            var listIndex = getLists().indexOf(list);
            var cards = getCards(listIndex);
            var listId = getList(listIndex).id;

            Card.save( {listId : listId, name : ''}, onSuccess, onError);

            function onSuccess(response) {
                console.log('Added new card to list with index ' + listIndex);
                cards.push(response);
            }

            function onError() {
                console.log('Error while adding card')
            }
        }

        function getCards(listIndex) {
            var list = getList(listIndex);
            return list.cards;
        }

        function getList(listIndex) {
            return $scope.board.lists[listIndex];
        }

        function getLists() {
            return $scope.board.lists;
        }
    }
})();

