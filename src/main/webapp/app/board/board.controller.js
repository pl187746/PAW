var app = angular.module('app', []);
app.controller('BoardController', function ($scope) {

    $scope.tab = 1;

    $scope.setTab = function (newTab) {
        $scope.tab = newTab;
    };

    $scope.isSet = function (tabNum) {
        return $scope.tab === tabNum;
    };

    $scope.addList = function () {

    }
	
	$scope.boards = [];
	
	$.getJSON("/boards", function(brds) {
		$scope.boards = brds;
		$scope.$apply();
	});
	
});


