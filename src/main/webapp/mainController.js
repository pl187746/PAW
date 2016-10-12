/**
 * Created by Krystian on 2016-10-11.
 */
var app = angular.module('app', []);
app.controller('mainController', function ($scope) {

    $scope.tab = 1;

    $scope.setTab = function (newTab) {
        $scope.tab = newTab;
    };

    $scope.isSet = function (tabNum) {
        return $scope.tab === tabNum;
    };

    $scope.addList = function () {

    }
});
