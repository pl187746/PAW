(function() {
    'use strict';

    angular
        .module('trello')
        .controller('HomeController', HomeController);

    HomeController.$inject = ['$scope', '$state', 'Board', 'FavBoard', 'LoginService', 'Team', 'User'];

    function HomeController ($scope, $state, Board, FavBoard, LoginService, Team, User) {
        $scope.$state = $state;
        $scope.boards = null;
		$scope.teams = null;
		$scope.users = null;

        $scope.addBoard = addBoard;
        $scope.updateBoard = updateBoard;
        $scope.removeBoard = removeBoard;
		$scope.addTeam = addTeam;
        $scope.updateTeam = updateTeam;
        $scope.removeTeam = removeTeam;
		$scope.addTeamMember = addTeamMember;
		$scope.removeTeamMember = removeTeamMember;
		$scope.isUserATeamMember = isUserATeamMember;
		$scope.isUserNotATeamMember = isUserNotATeamMember;
		$scope.usersThatArentMembersOf = usersThatArentMembersOf;
		$scope.isBoardTeamed = isBoardTeamed;
		$scope.isBoardNotTeamed = isBoardNotTeamed;
		$scope.isCurrentUserMemberOfTeam = isCurrentUserMemberOfTeam;
		$scope.isCurrentUserMemberOfBoard = isCurrentUserMemberOfBoard;
		$scope.isBoardNotTeamedAndIsCurrentUserMemberOfBoard = isBoardNotTeamedAndIsCurrentUserMemberOfBoard;
		$scope.isAuthenticated = isAuthenticated;
		$scope.isUserLikingBoard = isUserLikingBoard;
		$scope.likeBoard = likeBoard;
		$scope.unlikeBoard = unlikeBoard;

        loadBoards();

		loadTeams();

		loadUsers();

        function loadBoards() {
            Board.query(onSuccess, onError);

            function onSuccess(data) {
                $scope.boards = data;
                console.log('Loaded boards of size: ' + $scope.boards.length);
				dedupBoards();
            }

            function onError() {
                console.log('Error while loading boards');
            }
        }

        function addBoard(team) {
			var board = { name: 'Empty' };
			if(team !== null) {
				board.teamId = team.id;
			}
            Board.save(board, onSuccess, onError);

            function onSuccess(response) {
                $scope.boards.push(response);
				if(team !== null) {
					if(team.boards != null) {
						team.boards.push(response);
					} else {
						team.boards = [ response ];
					}
				}
                console.log('Added new board');
            }

            function onError() {
                console.log('Error while adding board')
            }

        }

        function removeBoard(board) {
            console.log('Remove board request for board with id' + board.id);

			var team = getBoardTeam(board);
			var boardIndex = $scope.boards.indexOf(board);
            var boardId = board.id;
            Board.delete({id : board.id}, onSuccess, onError);

            function onSuccess() {
                console.log('Removed board with id' + boardId);
                $scope.boards.splice(boardIndex, 1);
				if(team != null && team.boards != null) {
					var tbidx = team.boards.indexOf(board);
					team.boards.splice(tbidx, 1);
				}
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
				dedupBoards();
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

		function getBoardTeam(board) {
			if(board.teamId != null) {
				for(var ti in $scope.teams) {
					if($scope.teams[ti].id === board.teamId) {
						return $scope.teams[ti];
					}
				}
			}
			return null;
		}

		function dedupBoards() {
			if($scope.boards == null || $scope.teams == null) {
				return;
			}
			for(var i in $scope.boards) {
				var board = $scope.boards[i];
				var team = getBoardTeam(board);
				if(team != null && team.boards != null) {
					for(var j in team.boards) {
						if(team.boards[j].id === board.id) {
							team.boards[j] = board;
						}
					}
				}
			}
		}

		function addTeamMember(team, user) {
			if(team.users != null) {
				team.users.push(user);
			} else {
				team.users = [ user ];
			}
			updateTeam(team);
		}

		function removeTeamMember(team, user) {
			if(team.users != null) {
				var up = false;
				for(var i = 0; i < team.users.length; ++i) {
					if(team.users[i].id == user.id) {
						up = true;
						team.users.splice(i, 1);
						--i;
					}
				}
				if(up) {
					updateTeam(team);
				}
			}
		}

		function isUserATeamMember(team, user) {
			if(team.users != null) {
				for(var i in team.users) {
					if(team.users[i].id == user.id) {
						return true;
					}
				}
			}
			return false;
		}

		function isUserNotATeamMember(team, user) {
			return ! isUserATeamMember(team, user);
		}

		function usersThatArentMembersOf(team) {
			return $scope.users != null ? $scope.users.filter(function(u) { return isUserNotATeamMember(team, u) }) : [];
		}

		function isBoardTeamed(board) {
			return board.teamId != null;
		}

		function isBoardNotTeamed(board) {
			return ! isBoardTeamed(board);
		}

		function isCurrentUserMemberOfTeam(team) {
			if(!isAuthenticated())
				return false;
			return isUserATeamMember(team, getUser());
		}

		function isUserABoardMember(board, user) {
			if(board.members != null) {
				for(var i in board.members) {
					if(board.members[i].id == user.id) {
						return true;
					}
				}
			}
			return false;
		}

		function isCurrentUserMemberOfBoard(board) {
			if(!isAuthenticated())
				return false;
			return isUserABoardMember(board, getUser());
		}

		function isBoardNotTeamedAndIsCurrentUserMemberOfBoard(board) {
			return isBoardNotTeamed(board) && isCurrentUserMemberOfBoard(board);
		}

		function getUser() {
			return LoginService.identity(false).$$state.value;
		}

		function isAuthenticated() {
            return LoginService.isAuthenticated();
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

		function loadUsers() {
			User.query(onSuccess, onError);

			function onSuccess(data) {
				$scope.users = data;
			}

			function onError() {
			}
		}

    }
})();