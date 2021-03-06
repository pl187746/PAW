(function() {
    'use strict';

    angular
        .module('trello')
        .controller('BoardController', BoardController);

    BoardController.$inject = ['$rootScope', '$http', '$scope', '$stateParams', 'Board', 'Card', 'List', 'User', 'Label', 'Record', 'Comment', 'Subject'];

    function BoardController ($rootScope, $http, $scope, $stateParams, Board, Card, List, User, Label, Record, Comment, Subject) {
        // Cards
        $scope.updateCard = updateCard;
        $scope.removeCard = removeCard;
        $scope.addCard = addCard;
        $scope.hasCardLabel = hasCardLabel;
        $scope.toggleLabel = toggleLabel;
        $scope.cardMoved = cardMoved;
        $scope.isCardSubscribed = isCardSubscribed;

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
        $scope.archiveCard = archiveCard;
        $scope.returnArchiveCard = returnArchiveCard;
        $scope.listMoved = listMoved;

        //Members
        $scope.addMember = addMember;
        $scope.removeMember = removeMember;
        $scope.isUserAMember = isUserAMember;
        $scope.isUserNotAMember = isUserNotAMember;

        // Comments
        $scope.getComments = getComments;
        $scope.addComment = addComment;
        $scope.getCard = getCard;

        $scope.fmtRecord = fmtRecord;
        $scope.fmtDate = fmtDate;

        $scope.user = null;
        $scope.users = null;
        $scope.board = null;
        $scope.archList = [];
        $scope.movingCard = null;
        $scope.movingList = null;

        // Date
        $scope.isDefined = isDefined;
        $scope.parsedDate = parsedDate;

        $scope.createShareUrl = createShareUrl;
        $scope.urlToShare = null;

        // Subscription
        $scope.subscribed = false;
        $scope.subscribeBoard = subscribeBoard;
        $scope.unsubscribeBoard = unsubscribeBoard;

        if (isDefined($stateParams.share)) {
            loadBoardBySharedId($stateParams.share);
        } else {
        	loadBoardById($stateParams.id)
        }

        loadUsers();
        initializeToolTips();

        if ($stateParams.refresh.hasChanged) {
            console.log('Card wit id ' + $stateParams.refresh.cardId + ' has to be refreshed');
            refreshCard($stateParams.refresh.cardId);
        }

        function loadAccount() {
            Subject.identity().then(function(account) {
                if (account !== null) {
                    $scope.user = account;
                    for (var i = 0; i < $scope.board.subscribers.length; i++) {
                        if ($scope.board.subscribers[i] === account.login) {
                            $scope.subscribed = true;
                        }
                    }
                }
            });
        }

        function sortByOrd(arr) {
            arr.sort(function(a, b) {
                return (a.ord == null) ? ((b.ord == null) ? (a.id - b.id) : 1) : ((b.ord == null) ? -1 : ((a.ord != b.ord) ? (a.ord - b.ord) : (a.id - b.id)));
            });
        }

        function loadBoardById(id) {
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
                sortDiary($scope.board.diary);
                console.log('Loaded board of of name: ' + $scope.board.name)
                createShareUrl();
                loadAccount();
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

        function loadBoardBySharedId(sharedId) {
            $http.get('boards?share=' + sharedId).then(onSuccess).catch(onError);

            function onSuccess(response) {
            	var data = response.data;

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
                sortDiary($scope.board.diary);
                console.log('Loaded board of of name: ' + $scope.board.name)
                createShareUrl();
                loadAccount();
            }

            function onError(response) {
                console.log('Error while loading board');

                var boardId = $stateParams.share;
                if (response.status === 404 || response.status === 400) {
                    $scope.error = 'Invalid board shared id : ' + boardId;
                    console.log($scope.error);
                }
            }
        }

        function loadUsers() {
            User.query(onSuccess, onError);

            function onSuccess(data) {
                $scope.users = data;
            }

            function onError() {
            }
        }

        function firstIdx(arr) {
            for(var i = 0; i < arr.length; ++i) {
                if(!arr[i].archive)
                    return i;
            }
            return null;
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
                refreshDiary();
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
                refreshDiary();
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
                refreshDiary();
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
                refreshDiary();
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
                refreshDiary();
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
                refreshDiary();
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
            function remLab() {
                for(var i in card.labels) {
                    if(card.labels[i].id == label.id) {
                        card.labels.splice(i, 1);
                        return true;
                    }
                }
                return false;
            }

            if (card.labels != null) {
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
                refreshDiary();
            }

            function onError() {
                console.log('Error while updating label with id ' + label.id);
            }
        }

        function removeLabel(label) {
            function remFrom(arr) {
                if(arr == null)
                    return;
                for(var i = 0; i < arr.length; ++i) {
                    if(arr[i].id == label.id) {
                        arr.splice(i, 1);
                        --i;
                    }
                }
            }

            remFrom($scope.board.availableLabels);
            for(var li in $scope.board.lists) {
                var list = $scope.board.lists[li];
                for(var ci in list.cards) {
                    remFrom(list.cards[ci].labels);
                }
            }

            Label.delete({id: label.id}, onSuccess, onError);

            function onSuccess() {
                console.log('Deleted label with id ' + label.id);
                refreshDiary();
            }

            function onError() {
                console.log('Error while removing label with id ' + label.id);
            }
        }

        function createLabel() {
            Label.save( {boardId: $scope.board.id, name: '', color: '#FFFFFF'}, onSuccess, onError);

            function onSuccess(response) {
                console.log('Created new label id=' + response.id);
                $scope.board.availableLabels.push(response);
                refreshDiary();
            }

            function onError() {
                console.log('Error while creating card')
            }
        }

        function fmtDate(date) {
            if(!(date instanceof Date)) {
                date = new Date(date);
            }
            function s0(n) {
                return (n < 10 ? "0" : "") + n;
            }

            return s0(date.getHours()) + ":" + s0(date.getMinutes()) + ":" + s0(date.getSeconds()) + " "
                + s0(date.getDate()) + "." + s0(date.getMonth() + 1) + "." + date.getFullYear();
        }

        function fmtRecord(rec) {
            var msg = "";
            switch(rec.type) {
                case "BOARD_CREATE":
                    msg += "Utworzono tablicę pod nazwą " + rec.params.boardName;
                    break;
                case "BOARD_RENAME":
                    msg += "Zmieniono nazwę tablicy z " + rec.params.oldBoardName + " na " + rec.params.newBoardName;
                    break;
                case "BOARD_LIKE":
                    msg += "Poluniono tablicę";
                    break;
                case "BOARD_UNLIKE":
                    msg += "Odlubiono tablicę";
                    break;
                case "LIST_CREATE":
                    msg += "Utworzono listę " + rec.params.listName;
                    break;
                case "LIST_DELETE":
                    msg += "Usunięto listę " + rec.params.listName;
                    break;
                case "LIST_RENAME":
                    msg += "Zmieniono nazwę listy " + rec.params.oldListName + " na " + rec.params.newListName;
                    break;
                case "LIST_ARCHIVE":
                    msg += "Zarchiwizowano listę " + rec.params.listName;
                    break;
                case "LIST_UNARCHIVE":
                    msg += "Odarchiwizowano listę " + rec.params.listName;
                    break;
                case "CARD_CREATE":
                    msg += "Utworzono kartę " + rec.params.cardName + " w liście " + rec.params.listName;
                    break;
                case "CARD_DELETE":
                    msg += "Usunięto kartę " + rec.params.cardName;
                    break;
                case "CARD_RENAME":
                    msg += "Zmieniono nazwę karty " + rec.params.oldCardName + " na " + rec.params.newCardName;
                    break;
                case "CARD_CHANGE_LIST":
                    msg += "Przeniesiono kartę " + rec.params.cardName + " z listy " + rec.params.oldListName + " do " + rec.params.newListName;
                    break;
                case "CARD_ARCHIVE":
                    msg += "Zarchiwizowano kartę " + rec.params.cardName;
                    break;
                case "CARD_UNARCHIVE":
                    msg += "Odarchiwizowano kartę " + rec.params.cardName;
                    break;
                case "CARD_ADD_LABEL":
                    msg += "Dodano do karty " + rec.params.cardName + " etykietę " + rec.params.labelName;
                    break;
                case "CARD_REMOVE_LABEL":
                    msg += "Usunięto z karty " + rec.params.cardName + " etykietę " + rec.params.labelName;
                    break;
                case "LABEL_CREATE":
                    msg += "Utworzono nową etykietę " + rec.params.labelName;
                    break;
                case "LABEL_DELETE":
                    msg += "Usunięto etykietę " + rec.params.labelName;
                    break;
                case "LABEL_RENAME":
                    msg += "Zmieniono nazwę etykiety " + rec.params.oldLabelName + " na " + rec.params.newLabelName;
                    break;
                case "LABEL_CHANGE_COLOR":
                    msg += "Zmieniono kolor etykiety " + rec.params.labelName;
                    break;
            }
            return msg;
        }

        function refreshDiary() {
            if($scope.board.diary == null || $scope.board.diary.length == 0) {
                Record.get({ boardId: $scope.board.id }, onSuccess, onError);
            } else {
                Record.get({ boardId: $scope.board.id, dateAfter: $scope.board.diary[0].timestamp }, onSuccess, onError);
            }

            function onSuccess(response) {
                console.log("Refreshed diary.");
                $scope.board.diary = sortDiary($scope.board.diary.concat(response));
            }

            function onError() {
                console.log('Error while refreshing diary.')
            }
        }

        function sortDiary(dr) {
            sortByIdDescendingAndRemoveDuplicates(dr);
            return dr;
        }

        function sortByIdDescendingAndRemoveDuplicates(arr) {
            sortByIdDescending(arr);
            removeDuplicatesFromSortedById(arr);
        }

        function sortByIdDescending(arr) {
            arr.sort(function (a, b) {
                return b.id - a.id;
            });
        }

        function removeDuplicatesFromSortedById(arr) {
            return removeDuplicatesFromSorted(arr, function (a, b) {
                return a.id == b.id;
            });
        }

        function removeDuplicatesFromSorted(arr, eq) {
            for(var i = 1; i < arr.length; ++i) {
                if(eq(arr[i - 1], arr[i])) {
                    arr.splice(i, 1);
                    --i;
                }
            }
        }

        function addMember(member) {
            $scope.board.members.push(member);

            Board.update($scope.board, onSuccess, onError);

            function onSuccess() {
                console.log('Updated board');
                refreshDiary();
            }

            function onError() {
                console.log('Error while updating board');
            }
        }

        function removeMember(member) {
            var idx = $scope.board.members.indexOf(member);
            $scope.board.members.splice(idx, 1);

            Board.update($scope.board, onSuccess, onError);

            function onSuccess() {
                console.log('Updated board');
                refreshDiary();
            }

            function onError() {
                console.log('Error while updating board');
            }
        }

        function isUserAMember(user) {
            for(var mi in $scope.board.members) {
                if($scope.board.members[mi].id === user.id) {
                    return true;
                }
            }
            return false;
        }

        function isUserNotAMember(user) {
            return ! isUserAMember(user);
        }

        function addComment(content, card, list) {
            var listIndex = getLists().indexOf(list);
            var cardIndex = getCards(listIndex).indexOf(card);
            var comments = getComments(listIndex, cardIndex);


            if (content != null && content !== "") {
                Comment.save({cardId: card.id, content: content}, onSuccess, onError);
            }


            function onSuccess(response) {
                console.log('Added new comment to card with id ' + card.id);
                comments.push(response);
            }

            function onError() {
                console.log('Error while adding comment')
            }
        }

        function getCard(listIndex, cardIndex) {
            return getList(listIndex).cards[cardIndex];
        }

        function getComments(listIndex, cardIndex) {
            return getCard(listIndex, cardIndex).comments;
        }

        function refreshCard(refreshedCardId) {
            Card.get({id: refreshedCardId}, onSuccess, onError);

            function onSuccess(response) {;
                var card = response;
                var lists = getLists();
                for (var listIndex = 0; listIndex < lists.length; listIndex++) {
                    if (lists[listIndex].id !== card.listId) {
                        continue;
                    }

                    var list = lists[listIndex];
                    for (var cardIndex = 0; cardIndex < list.length; cardIndex++) {
                        if (list[cardIndex].id !== card.id) {
                            continue;
                        }

                        list[cardIndex].id = card;
                        console.log('Card updated');
                    }
                }
            }

            function onError() {
                console.log('Error while refreshing card with id ' + refreshedCardId);
            }
        }

        function initializeToolTips() {
            $("[data-toggle=tooltip]").tooltip();
        }

        function parsedDate(date) {
            return moment(date).format('DD-MMM-YYYY  HH:mm');
        }

        function isDefined(value) {
            return value !== undefined && value !== null;
        }

        function createShareUrl() {
            var id = $scope.board.id + 1234567890;
            var number = (+id).toString(16);
            $scope.urlToShare = 'localhost:8080/#/board/?share=' + number;
            return $scope.urlToShare;
        }

        function subscribeBoard() {
            $http.post('/boards/' + $scope.board.id + '/subscribe')
                .then(function (response) {
                    console.log('Board has been subscribed');
                    $scope.subscribed = true;
                });
        }

        function unsubscribeBoard() {
            $http.post('/boards/' + $scope.board.id + '/unsubscribe')
                .then(function (response) {
                    console.log('Board has been unsubscribed');
                    $scope.subscribed = false;
                });
        }

        function cardMoved(list, card, $index) {
            list.cards.splice($index, 1);
            var newList = getListOfCard(card);
            card.listId = newList.id;

            for (var i = 0; i < newList.cards.length; i++) {
                if (newList.cards[i].id === card.id) {
                    newList.cards[i].listId = newList.id;
                    break;
                }
            }
            updateOrders(newList.cards, updateCard);
        }

        function listMoved(list, $index) {
            getLists().splice($index, 1);

            updateOrders(getLists(), updateList);
        }

        function getListOfCard(card) {
            var lists = getLists();
            for (var i = 0; i < lists.length; i++) {
                var cards = lists[i].cards;
                for (var j = 0; j < cards.length; j++) {
                    if (cards[j].id === card.id) {
                        return lists[i];
                    }
                }
            }
        }

        function updateOrders(items, updateFunction) {
            for (var i = 0; i < items.length; i++) {
                items[i].ord = i;
            }

            for (var i = 0; i < items.length; i++) {
                updateFunction(items[i]);
            }
        }

        function isCardSubscribed(card) {
            for (var i = 0; i < card.subscribers.length; i++) {
                if (card.subscribers[i] === $scope.user.login) {
                    return true;
                }
            }

            return false;
        }
    }
})();

