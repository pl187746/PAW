'use strict';

angular
    .module('trello')
    .controller('CardWindowController', CardWindowController);

CardWindowController.$inject = ['$scope', '$uibModalInstance', 'entity', 'Comment', 'Upload'];

function CardWindowController ($scope, $uibModalInstance, entity, Comment, Upload) {
    const VIEWS = {
        COMMENTS: 'COMMENTS',
        ATTACHMENTS: 'ATTACHMENTS'
    };

    // Window
    $scope.close = close;
    $scope.changeView = changeView;

    // Comments
    $scope.addComment = addComment;

    // Attachments
    $scope.upload = upload;

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

    function upload(file) {
        console.log('upload avatar');
        Upload.upload({
            url: '/cards/' + $scope.card.id + '/upload_attachment',
            data: {cardId: $scope.card.id, file: file}
        }).then(function (resp) {
            console.log('Success ' + resp.config.data.file.name + 'uploaded. Response: ' + resp.data);
            $scope.attachments.push(resp.data);
        }, function (resp) {
            console.log('Error status: ' + resp.status);
        }, function (evt) {
            var progressPercentage = parseInt(100.0 * evt.loaded / evt.total);
            console.log('progress: ' + progressPercentage + '% ' + evt.config.data.file.name);
        });
    }
}