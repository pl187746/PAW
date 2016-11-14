'use strict';

angular
    .module('trello')
    .controller('CardWindowController', CardWindowController);

CardWindowController.$inject = ['$scope', '$uibModalInstance', 'entity'];

function CardWindowController ($scope, $uibModalInstance, entity) {
    $scope.card = entity;

    $scope.close = close;

    function close() {
        $uibModalInstance.dismiss('cancel');
    }
}