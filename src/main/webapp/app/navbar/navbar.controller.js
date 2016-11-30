(function() {
    'use strict';

    angular
        .module('trello')
        .controller('NavbarController', NavbarController);

    NavbarController.$inject = ['$rootScope', '$scope', '$state','$translate', 'Board', 'LoginService', 'Subject', 'User'];

    function NavbarController ($rootScope, $scope, $state, $translate, Board, LoginService, Subject, User) {
        $scope.logout = logout;
        $scope.isAuthenticated = isAuthenticated;
        $scope.changeLanguage = changeLanguage;

        $scope.$state = $state;
        $rootScope.boards = null;

        $scope.user = null;
        $scope.isAuthenticated = null;

        $scope.fmtRecord = fmtRecord;
        $scope.fmtDate = fmtDate;
        $scope.updateNotificationsViewTime = updateNotificationsViewTime;
		$scope.isNewRecord = isNewRecord;

        $scope.$on('authenticationSuccess', function() {
            getAccount();
        });

        getAccount();
        loadBoards();

        function getAccount() {
            Subject.identity().then(function(account) {
                if (account !== null) {
                    console.log('Loaded user: ' + account.login);
                }
                $scope.user = account;
                $scope.isAuthenticated = Subject.isAuthenticated;
            });
        }

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

        function logout() {
            LoginService.logout();
            Subject.authenticate(null);
        }

        function isAuthenticated() {
            return LoginService.isAuthenticated();
        }

        function changeLanguage(langKey) {
            $translate.use(langKey);
        };

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

        function updateNotificationsViewTime() {
            if($scope.user == null) {
                return;
            }

            var date = new Date();
			var user = { id: $scope.user.id, notificationsLastViewTime: date.toISOString() };

            User.update(user, onSuccess, onError);

            function onSuccess() {
                console.log("Updated User");
            }

            function onError() {
                console.log("Error when updating User");
            }
        }

        function isNewRecord(rec) {
            if($scope.user == null || $scope.user.notificationsLastViewTime == null) {
                return true;
            }
            var viewTime = Date.parse($scope.user.notificationsLastViewTime);
            var recTime = Date.parse(rec.timestamp);
            return recTime > viewTime;
        }
    }

})();