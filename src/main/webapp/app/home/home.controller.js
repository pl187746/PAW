(function() {
    'use strict';

    angular
        .module('trello')
        .controller('HomeController', HomeController);

    HomeController.$inject = ['$rootScope', '$scope', '$state', 'Board', 'FavBoard', 'LoginService', 'Team'];

    function HomeController ($rootScope, $scope, $state, Board, FavBoard, LoginService, Team) {
        $scope.$state = $state;
        $scope.boards = null;
		$scope.teams = null;

        $scope.addBoard = addBoard;
        $scope.updateBoard = updateBoard;
        $scope.removeBoard = removeBoard;
		$scope.isAuthenticated = isAuthenticated;
		$scope.isUserLikingBoard = isUserLikingBoard;
		$scope.likeBoard = likeBoard;
		$scope.unlikeBoard = unlikeBoard;

        loadBoards();

		loadTeams();

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
                $scope.boards.push(response);
                console.log('Added new board');
            }

            function onError() {
                console.log('Error while adding board')
            }

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

		function loadTeams() {
            Team.query(onSuccess, onError);

            function onSuccess(data) {
                $scope.teams = data;
                console.log('Loaded teams of size: ' + $scope.teams.length);
            }

            function onError() {
                console.log('Error while loading teams');
            }
        }

		function addTeam() {
            Team.save( {name : 'Empty'}, onSuccess, onError);

            function onSuccess(response) {
                $scope.teams.push(response);
                console.log('Added new team');
            }

            function onError() {
                console.log('Error while adding team')
            }

        }

        function removeTeam(team) {
            console.log('Remove team request for team with id' + team.id);

			var teamIndex = $scope.teams.indexOf(team);
            var teamId = team.id;
            Team.delete({id : team.id}, onSuccess, onError);

            function onSuccess() {
                console.log('Removed team with id' + teamId);
                $scope.teams.splice(teamIndex, 1);
            }

            function onError() {
                console.log('Error while removing team with id' + teamId);
            }
        }

        function updateTeam(team) {
            console.log('Update team request for team with id ' + team.id);

            Team.update(team, onSuccess, onError);

            function onSuccess() {
                console.log('Updated team with index ' + team.id);
            }

            function onError() {
                console.log('Error while updating team with id ' + team.id);
            }
        }

		function getUser() {
			return $rootScope.user;
		}

		function isAuthenticated() {
            return getUser() != null;
        }

		function isUserLikingBoard(boardId) {
			if(!isAuthenticated())
				return false;
			for(var fb in getUser().favoriteBoards) {
				if(getUser().favoriteBoards[fb].boardId == boardId)
					return true;
			}
			return false;
		}

		function likeBoard(boardId) {
			var user = getUser();
			FavBoard.save({ 'userId': user.id, 'boardId': boardId}, onSuccess, onError);
			
			function onSuccess(data) {
                console.log('User id=' + user.id + ' liked board id=' + boardId);
				user.favoriteBoards.push(data);
				LoginService.saveUser();
            }

            function onError() {
                console.log('Error while liking user.id=' + user.id + ' board.id=' + boardId);
            }
		}

		function unlikeBoard(boardId) {
			var user = getUser();
			FavBoard.delete({ 'userId': user.id, 'boardId': boardId}, onSuccess, onError);
			
			function onSuccess() {
                console.log('User id=' + user.id + ' unliked board id=' + boardId);
				for(var i in user.favoriteBoards) {
					if(user.favoriteBoards[i].boardId == boardId) {
						user.favoriteBoards.splice(i, 1);
						break;
					}
				}
            }

            function onError() {
                console.log('Error while unliking user.id=' + user.id + ' board.id=' + boardId);
            }
		}
    }
})();