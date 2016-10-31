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
		$scope.moveCard = moveCard;
		$scope.isCardFirst = isCardFirst;
		$scope.isCardLast = isCardLast;
		$scope.transferCardToNextList = transferCardToNextList;

        // Cards List
        $scope.removeList = removeList;
        $scope.updateList = updateList;
        $scope.addList = addList;
        $scope.archiveList = archiveList;
        $scope.returnArchiveList = returnArchiveList;
		$scope.moveList = moveList;
		$scope.isListFirst = isListFirst;
		$scope.isListLast = isListLast;
		
		$scope.fmtRecord = fmtRecord;

        $scope.board = null;
        $scope.archList = [];

        loadBoard($stateParams.id);
		
		function sortByOrd(arr) {
			arr.sort(function(a, b) {
				return (a.ord == null) ? ((b.ord == null) ? (a.id - b.id) : 1) : ((b.ord == null) ? -1 : ((a.ord != b.ord) ? (a.ord - b.ord) : (a.id - b.id)));
			});
		}

        function loadBoard(id) {
            Board.get({"id" : id}, onSuccess, onError);

            function onSuccess(data) {
				sortByOrd(data.lists);
				for(var li in data.lists) {
					sortByOrd(data.lists[li].cards);
				}
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
		
		function firstListIdx() {
			var lists = getLists();
			for(var li = 0; li < lists.length; ++li) {
				if(!lists[li].archive)
					return li;
			}
			return null;
		}
		
		function lastListIdx() {
			var lists = getLists();
			for(var li = lists.length - 1; li >= 0; --li) {
				if(!lists[li].archive)
					return li;
			}
			return null;
		}
		
		function isListFirst(list) {
			return getLists().indexOf(list) == firstListIdx();
		}
		
		function isListLast(list) {
			return getLists().indexOf(list) == lastListIdx();
		}
		
		function isCardFirst(list, card) {
			return list.cards.indexOf(card) == 0;
		}
		
		function isCardLast(list, card) {
			return list.cards.indexOf(card) == (list.cards.length - 1);
		}
		
		function moveList(list, dir) {
			if(!dir)
				return;
			dir = ((dir < -1) ? -1 : ((dir > 1) ? 1 : dir));
			var lists = getLists();
			var index = lists.indexOf(list);
			var newIdx = index;
			for(;;) {
				newIdx += dir;
				if(newIdx < 0 || newIdx >= lists.length)
					break;
				if(!lists[newIdx].archive)
					break;
			}
			newIdx = ((newIdx < 0) ? 0 : ((newIdx >= lists.length) ? (lists.length - 1) : newIdx));
			if(newIdx != index) {
				lists.splice(newIdx, 0, lists.splice(index, 1)[0]);
				updateListOrds();
			}
		}
		
		function moveCard(list, card, dir) {
			if(!dir)
				return;
			dir = ((dir < -1) ? -1 : ((dir > 1) ? 1 : dir));
			var cards = list.cards;
			var index = cards.indexOf(card);
			var newIdx = index + dir;
			if((newIdx < 0) || (newIdx >= cards.length))
				return;
			cards.splice(index + dir, 0, cards.splice(index, 1)[0]);
			updateCardOrds(list);
		}
		
		function updateListOrds() {
			var lists = getLists();
			for(var li in lists) {
				var up = (lists[li].ord != li);
				lists[li].ord = li;
				if(up) {
					updateList(lists[li]);
				}
			}
		}
		
		function updateCardOrds(list) {
			var cards = list.cards;
			for(var ci in cards) {
				var up = (cards[ci].ord != ci);
				cards[ci].ord = ci;
				if(up) {
					updateCard(cards[ci]);
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
				updateListOrds();
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

            List.save( {boardId : $scope.board.id, name : '', ord : lists.length }, onSuccess, onError);

            function onSuccess(response) {
                console.log('Added new list to board with id ' + $scope.board.id);
                lists.push(response);
				updateListOrds();
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
            console.log('Update card request for card,id: ' + card.id);
            Card.update(card, onSuccess, onError);

            function onSuccess() {
                console.log('Updated card with id ' + card.id);
            }

            function onError() {
                console.log('Error while updating card with id ' + card.id);
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
		
		function transferCardToNextList(card, list, dir) {
			if(!dir)
				return;
			dir = ((dir < -1) ? -1 : ((dir > 1) ? 1 : dir));
			var lists = getLists();
			var idxList = lists.indexOf(list);
			var newIdxList = idxList;
			for(;;) {
				newIdxList += dir;
				if(newIdxList < 0 || newIdxList >= lists.length)
					break;
				if(!lists[newIdxList].archive)
					break;
			}
			newIdxList = ((newIdxList < 0) ? 0 : ((newIdxList >= lists.length) ? (lists.length - 1) : newIdxList));
			if(newIdxList != idxList) {
				transferCardBetweenLists(card, list, lists[newIdxList]);
			}
		}
		
		function transferCardBetweenLists(card, fromList, toList) {
			var oldIdx = fromList.cards.indexOf(card);
			fromList.cards.splice(oldIdx, 1);
			updateCardOrds(fromList);
			card.listId = toList.id;
			card.ord = toList.cards.length;
			toList.cards.push(card);
			updateCard(card);
		}
		
		function fmtRecord(rec) {
			var date = new Date(rec.timestamp);
			var msg = date.getHours() + ":" + date.getMinutes() + ":" + date.getSeconds() + " " + date.getDate() + "." + (date.getMonth() + 1) + "." + date.getFullYear() + " ";
			switch(rec.type) {
				case "BOARD_CREATE": msg += "Utworzono tablicę pod nazwą " + rec.params.boardName; break;
				case "BOARD_RENAME": msg += "Zmieniono nazwę tablicy z " + rec.params.oldBoardName + " na " + rec.params.newBoardName; break;
				case "BOARD_LIKE": msg += "Poluniono tablicę"; break;
				case "BOARD_UNLIKE": msg += "Odlubiono tablicę"; break;
				case "LIST_CREATE": msg += "Utworzono listę " + rec.params.listName; break;
				case "LIST_DELETE": msg += "Usunięto listę " + rec.params.listName; break;
				case "LIST_RENAME": msg += "Zmieniono nazwę listy " + rec.params.oldListName + " na " + rec.params.newListName; break;
				case "LIST_ARCHIVE": msg += "Zarchiwizowano listę " + rec.params.listName; break;
				case "LIST_UNARCHIVE": msg += "Odarchiwizowano listę " + rec.params.listName; break;
				case "CARD_CREATE": msg += "Utworzono kartę " + rec.params.cardName + " w liście " + rec.params.listName; break;
				case "CARD_DELETE": msg += "Usunięto kartę " + rec.params.cardName; break;
				case "CARD_RENAME": msg += "Zmieniono nazwę karty " + rec.params.oldCardName + " na " + rec.params.newCardName; break;
				case "CARD_CHANGE_LIST": msg += "Przeniesiono kartę " + rec.params.cardName + " z listy " + rec.params.oldListName + " do " + rec.params.newListName; break;
				case "CARD_ARCHIVE": msg += "Zarchiwizowano kartę " + rec.params.cardName; break;
				case "CARD_UNARCHIVE": msg += "Odarchiwizowano kartę " + rec.params.cardName; break;
			}
			return msg;
		}
		
    }
})();

