(function() {
    'use strict';

    angular
        .module('trello')
        .controller('BoardController', BoardController);

    BoardController.$inject = ['$rootScope', '$scope', '$stateParams', 'Board', 'Card', 'List', 'User', 'Label'];

    function BoardController ($rootScope, $scope, $stateParams, Board, Card, List, User, Label) {
        // Cards
        $scope.updateCard = updateCard;
        $scope.removeCard = removeCard;
        $scope.addCard = addCard;
		$scope.moveCard = moveCard;
		$scope.isCardFirst = isCardFirst;
		$scope.isCardLast = isCardLast;
		$scope.transferCardToNextList = transferCardToNextList;
		$scope.hasCardLabel = hasCardLabel;
		$scope.toggleLabel = toggleLabel;
		
		// Labels
		$scope.updateLabel = updateLabel;
		$scope.removeLabel = removeLabel;
		$scope.createLabel = createLabel;

        // Cards List
        $scope.removeList = removeList;
        $scope.updateList = updateList;
        $scope.addList = addList;
        $scope.archiveList = archiveList;
        $scope.returnArchiveList = returnArchiveList;
		$scope.moveList = moveList;
		$scope.isListFirst = isListFirst;
		$scope.isListLast = isListLast;
		$scope.archiveCard = archiveCard;
		$scope.returnArchiveCard = returnArchiveCard;

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
				for(var li in data.lists) {
					for(var ci in data.lists[li].cards) {
						var lbs = data.lists[li].cards[ci].labels;
						if(lbs == null) {
							data.lists[li].cards[ci] = [];
						} else {
							for(var i in lbs) {
								for(var j in data.availableLabels) {
									if(lbs[i].id == data.availableLabels[j].id) {
										lbs[i] = data.availableLabels[j];
									}
								}
							}
						}
					}
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
		
		function firstIdx(arr) {
			for(var i = 0; i < arr.length; ++i) {
				if(!arr[i].archive)
					return i;
			}
			return null;
		}
		
		function lastIdx(arr) {
			for(var i = arr.length - 1; i >= 0; --i) {
				if(!arr[i].archive)
					return i;
			}
			return null;
		}
		
		
		function isListFirst(list) {
			var lists = getLists();
			return lists.indexOf(list) == firstIdx(lists);
		}
		
		function isListLast(list) {
			var lists = getLists();
			return lists.indexOf(list) == lastIdx(lists);
		}
		
		function isCardFirst(list, card) {
			return list.cards.indexOf(card) == firstIdx(list.cards);
		}
		
		function isCardLast(list, card) {
			return list.cards.indexOf(card) == lastIdx(list.cards);
		}
		
		function moveObj(arr, obj, dir, updFun) {
			if(!dir)
				return;
			dir = ((dir < -1) ? -1 : ((dir > 1) ? 1 : dir));
			var index = arr.indexOf(obj);
			var newIdx = index;
			for(;;) {
				newIdx += dir;
				if(newIdx < 0 || newIdx >= arr.length)
					break;
				if(!arr[newIdx].archive)
					break;
			}
			newIdx = ((newIdx < 0) ? 0 : ((newIdx >= arr.length) ? (arr.length - 1) : newIdx));
			if(newIdx != index) {
				arr.splice(newIdx, 0, arr.splice(index, 1)[0]);
				updFun();
			}
		}
		
		function moveList(list, dir) {
			return moveObj(getLists(), list, dir, updateListOrds);
		}
		
		function moveCard(list, card, dir) {
			return moveObj(list.cards, card, dir, function() { updateCardOrds(list); });
		}
		
		function updateOrds(arr, updFun) {
			for(var i in arr) {
				var up = (arr[i].ord != i);
				arr[i].ord = i;
				if(up) {
					updFun(arr[i]);
				}
			}
		}
		
		function updateListOrds() {
			return updateOrds(getLists(), updateList);
		}
		
		function updateCardOrds(list) {
			return updateOrds(list.cards, updateCard);
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

        function updateCard(card) {
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

		function archiveCard(card,list){
			card.archive = true;
			updateCard(card,list);
		}

		function returnArchiveCard(card,list){
			card.archive = false;
			updateCard(card,list);
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
		
		function hasCardLabel(card, label) {
			if(card.labels == null)
				return false;
			for(var i in card.labels) {
				if(card.labels[i].id == label.id)
					return true;
			}
			return false;
		}
		
		function toggleLabel(card, label) {
			if(card.labels != null) {
				function remLab() {
					for(var i in card.labels) {
						if(card.labels[i].id == label.id) {
							card.labels.splice(i, 1);
							return true;
						}
					}
					return false;
				}
				if(!remLab()) {
					card.labels.push(label);
				}
			} else {
				card.labels = [ label ];
			}
			updateCard(card);
		}
		
		function updateLabel(label) {
			console.log('Update label request for label.id: ' + label.id);
            Label.update(label, onSuccess, onError);

            function onSuccess() {
                console.log('Updated label with id ' + label.id);
            }

            function onError() {
                console.log('Error while updating label with id ' + label.id);
            }
		}
		
		function removeLabel(label) {
			function remFrom(arr) {
				if(arr == null)
					return false;
				var up = false;
				for(var i = 0; i < arr.length; ++i) {
					if(arr[i].id == label.id) {
						arr.splice(i, 1);
						--i;
						up = true;
					}
				}
				return up;
			}
			remFrom($scope.board.availableLabels);
			for(var li in $scope.board.lists) {
				var list = $scope.board.lists[li];
				for(var ci in list.cards) {
					if(remFrom(list.cards[ci].labels)) {
						updateCard(list.cards[ci]);
					}
				}
			}
			
			Label.delete({id: label.id}, onSuccess, onError);

            function onSuccess() {
                console.log('Deleted label with id ' + label.id);
            }

            function onError() {
                console.log('Error while removing label with id ' + label.id);
            }
		}
		
		function createLabel() {
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
				case "CARD_ADD_LABEL": msg += "Dodano do karty " + rec.params.cardName + " etykietę " + rec.params.labelName; break;
				case "CARD_REMOVE_LABEL": msg += "Usunięto z karty " + rec.params.cardName + " etykietę " + rec.params.labelName; break;
				case "LABEL_CREATE": msg += "Utworzono nową etykietę " + rec.params.labelName; break;
				case "LABEL_DELETE": msg += "Usunięto etykietę " + rec.params.labelName; break;
				case "LABEL_RENAME": msg += "Zmieniono nazwę etykiety " + rec.params.oldLabelName + " na " + rec.params.newLabelName; break;
				case "LABEL_CHANGE_COLOR": msg += "Zmieniono kolor etykiety " + rec.params.labelName; break;
			}
			return msg;
		}
		
    }
})();

