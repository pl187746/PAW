'use strict';

angular
    .module('trello')
    .controller('CardWindowController', CardWindowController);

CardWindowController.$inject = ['$scope', '$uibModalInstance', 'entity', 'Comment'];

function CardWindowController ($scope, $uibModalInstance, entity, Comment) {
    const VIEWS = {
        COMMENTS: 'COMMENTS',
        ATTACHMENTS: 'ATTACHMENTS'
    };

    $scope.close = close;
    $scope.changeView = changeView;
    $scope.addComment = addComment;

    $scope.VIEWS = VIEWS;
    $scope.card = entity;
    $scope.commentContent = '';

    changeView(VIEWS.COMMENTS);

    function close() {
        $uibModalInstance.dismiss('cancel');
    }

    function changeView(view) {
        console.log('View changed from ' + $scope.view + ' to ' + view);
        $scope.view = view;
    }

    function addComment(comment) {
        console.log('Add comment request for card with id: ' + $scope.card.id + ' and content: ' + comment);

        if (comment != null && comment.length > 0) {
            Comment.save({cardId: $scope.card.id, content: comment}, onSuccess, onError);
        }

        function onSuccess(response) {
            console.log('Added new comment to card with id ' + $scope.card.id);
            $scope.card.comments.push(response);
        }

        function onError() {
            console.log('Error while adding comment')
        }
    }

}