'use strict';

angular
    .module('trello')
    .controller('CardWindowController', CardWindowController);

CardWindowController.$inject = ['$scope', '$uibModalInstance', 'entity'];

function CardWindowController ($scope, $uibModalInstance, entity) {
    $scope.close = close;
    $scope.changeView = changeView;

    const VIEWS = {
        COMMENTS: 'COMMENTS',
        ATTACHMENTS: 'ATTACHMENTS'
    };
    $scope.VIEWS = VIEWS;

    $scope.card = entity;

    changeView(VIEWS.COMMENTS);

    function close() {
        $uibModalInstance.dismiss('cancel');
    }

    function changeView(view) {
        console.log('View changed from ' + $scope.view + ' to ' + view);
        $scope.view = view;
    }
}