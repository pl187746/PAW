'use strict';

angular
    .module('trello')
    .controller('CardWindowController', CardWindowController);

CardWindowController.$inject = ['$scope', '$uibModalInstance','Subject', 'entity', 'Comment'];

function CardWindowController ($scope, $uibModalInstance, Subject, entity, Comment) {
    const VIEWS = {
        COMMENTS: 'COMMENTS',
        ATTACHMENTS: 'ATTACHMENTS'
    };

    $scope.close = close;
    $scope.changeView = changeView;
    $scope.addComment = addComment;
    $scope.updateComment = updateComment;
    $scope.removeComment = removeComment;

    $scope.VIEWS = VIEWS;
    $scope.card = entity;
    $scope.commentContent = '';
    $scope.user = null;


    getAccount();

    function getAccount() {
        Subject.identity().then(function(account) {
            if (account !== null) {
            }
            $scope.user = account;
        });
    }

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

    function updateComment(comment) {
        console.log('Update comment request for card with id: ' + $scope.card.id + ' and content: ' + comment);

        Comment.update(comment,onSuccess,onError);

        function onSuccess(response) {
            console.log('Updated comment in card with id ' + $scope.card.id);
        }

        function onError() {
            console.log('Error while updating comment');
        }
    }

    function removeComment(comment, card) {
        console.log('Delete comment request for card with id: ' + $scope.card.id + ' and content: ' + comment);
        var commentIndex = $scope.card.comments.indexOf(comment);
        $scope.card.comments.splice(commentIndex,1);

        Comment.delete({id: comment.id},onSuccess,onError);
        function onSuccess(response) {
            console.log('Deleted comment in card with id ' + $scope.card.id);
        }

        function onError() {
            console.log('Error while deleting comment');
        }
    }

}