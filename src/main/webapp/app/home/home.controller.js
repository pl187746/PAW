(function() {
    'use strict';

    angular
        .module('trello')
        .controller('HomeController', HomeController);

    HomeController.$inject = ['$rootScope', '$scope', '$state', 'Board', 'FavBoard'];

    function HomeController ($rootScope, $scope, $state, Board, FavBoard) {
        $scope.$state = $state;
        $scope.boards = null;

        $scope.addBoard = addBoard;
        $scope.updateBoard = updateBoard;
        $scope.removeBoard = removeBoard;
		$scope.isAuthenticated = isAuthenticated;
		$scope.isUserLikingBoard = isUserLikingBoard;
		$scope.likeBoard = likeBoard;
		$scope.unlikeBoard = unlikeBoard;
		
		$scope.user = null;
		
		$rootScope.$on('loginEvent', onLoginEvent);

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

        function addBoard() {
            Board.save( {name : 'Empty'}, onSuccess, onError);

            function onSuccess(response) {
                console.log('Added new board');
                $scope.boards.push(response);
            }

            function onError() {
                console.log('Error while adding board')
            }

            loadBoards();
        }

        function removeBoard(board, boardIndex) {
            console.log('Remove board request for board with id' + board.id);

            var boardId = board.id;
            Board.delete({id : board.id}, onSuccess, onError);

            function onSuccess() {
                console.log('Removed board with id' + boardId);
                $scope.boards.splice(boardIndex, 1);
            }

            function onError() {
                console.log('Error while removing board with id' + boardId);
            }
        }

        function updateBoard(board) {
            console.log('Update board request for board with id ' + board.id);

            Board.update(board, onSuccess, onError);

            function onSuccess() {
                console.log('Updated board with index ' + board.id);
            }

            function onError() {
                console.log('Error while updating board with id ' + board.id);
            }
        }
		
		function onLoginEvent(event, user) {
			$scope.user = user;
		}
		
		function isAuthenticated() {
            return $scope.user != null;
        }
		
		function isUserLikingBoard(boardId) {
			if(!isAuthenticated())
				return false;
			for(var fb in $scope.user.favoriteBoards) {
				if($scope.user.favoriteBoards[fb].boardId == boardId)
					return true;
			}
			return false;
		}
		
		function likeBoard(boardId) {
			FavBoard.save({ 'userId': $scope.user.id, 'boardId': boardId}, onSuccess, onError);
			
			function onSuccess() {
                console.log('User id=' + $scope.user.id + ' liked board id=' + boardId);
            }

            function onError() {
                console.log('Error while liking user.id=' + $scope.user.id + ' board.id=' + boardId);
            }
		}
		
		function unlikeBoard(boardId) {
		FavBoard.delete({ 'userId': $scope.user.id, 'boardId': boardId}, onSuccess, onError);
			
			function onSuccess() {
                console.log('User id=' + $scope.user.id + ' unliked board id=' + boardId);
            }

            function onError() {
                console.log('Error while unliking user.id=' + $scope.user.id + ' board.id=' + boardId);
            }
		}
		
    }
})();