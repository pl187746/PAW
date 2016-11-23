'use strict';

angular
    .module('trello')
    .controller('CardWindowController', CardWindowController);

CardWindowController.$inject = ['$scope', '$http', '$uibModalInstance', 'entity', 'Comment', 'Upload', 'Subject', 'uibDateParser'];

function CardWindowController ($scope, $http, $uibModalInstance, entity, Comment, Upload, Subject, uibDateParser) {
    const VIEWS = {
        COMMENTS: 'COMMENTS',
        ATTACHMENTS: 'ATTACHMENTS',
        COMPLETION_DATE: 'COMPLETION_DATE'
    };

    const DATE_FORMAT = 'dd MMM yyyy HH:mm';

    // Window
    $scope.close = close;
    $scope.changeView = changeView;

    // Comments
    $scope.addComment = addComment;
    $scope.updateComment = updateComment;
    $scope.removeComment = removeComment;

    // Attachments
    $scope.submit = submit;
    $scope.removeAttachment = removeAttachment;
    
    // Completion Date
    $scope.openCalendar = openCalendar;
    $scope.addCompletionDate = addCompletionDate;
    $scope.updateCompletionDate = updateCompletionDate;
    $scope.deleteCompletionDate = deleteCompletionDate;
    $scope.isDefined = isDefined;
    $scope.parsedDate = parsedDate;

    $scope.VIEWS = VIEWS;
    $scope.DATE_FORMAT = DATE_FORMAT;
    $scope.card = entity;
    $scope.commentContent = '';
    $scope.user = null;
    $scope.completionDate = null;
    $scope.dateTime = null;
    $scope.datePickerOpenStatus = {date: false, dateTime: false};

    changeView(VIEWS.ATTACHMENTS);

    initializeFileButtonBehaviour();
    initializeToolTips();

    getAccount();

    initializeCompletionDate();

    function close() {
        $uibModalInstance.dismiss('cancel');
    }

    function changeView(view) {
        console.log('View changed from ' + $scope.view + ' to ' + view);
        $scope.view = view;
    }

    function addComment(comment) {
        console.log('Add comment request for card with id: ' + $scope.card.id + ' and content: ' + comment);
        $scope.date = new Date();
        if (comment != null && comment.length > 0) {
            Comment.save({cardId: $scope.card.id, content: comment, date: $scope.date}, onSuccess, onError);
        }

        function onSuccess(response) {
            console.log('Added new comment to card with id ' + $scope.card.id);
            $scope.card.comments.push(response);
            $scope.commentContent = '';
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

    function submit() {
        if ($scope.file) {
            upload($scope.file);
        }
    }

    function upload(file) {
        Upload.upload({
            url: '/cards/attachments',
            data: {cardId: $scope.card.id, file: file}
        }).then(function (resp) {
            console.log('Success ' + resp.config.data.file.name + ' uploaded');
            $scope.card.attachments.push(resp.data);
        }, function (resp) {
            console.log('Error status: ' + resp.status);
        }, function (evt) {
            var progressPercentage = parseInt(100.0 * evt.loaded / evt.total);
            console.log('progress: ' + progressPercentage + '% ' + evt.config.data.file.name);
        });
    }

    function removeAttachment(attachmentId) {
        $http.delete('/cards/attachments/' + attachmentId)
            .then(function (response) {
                removeAttachmentFromList(attachmentId);
            })
            .catch(function (response) {
                console.log('Error while removing attachment');
            })
    }

    function removeAttachmentFromList(attachmentId) {
        for (var i = 0; i < $scope.card.attachments.length; i++) {
            if ($scope.card.attachments[i].id === attachmentId) {
                $scope.card.attachments.splice(i, 1);
            }
        }
    }

    function getAccount() {
        Subject.identity().then(function(account) {
            if (account !== null) {
            }
            $scope.user = account;
        });
    }

    function openCalendar() {
        $scope.datePickerOpenStatus.date = true;
    }

    function addCompletionDate() {
        $http.post('/cards/' + $scope.card.id + '/completion_date', {date: $scope.dateTime})
            .then(onSuccess)
            .catch(onError);

        function onSuccess(response) {
            console.log('Completion date has been added to card with id ' + $scope.card.id);
            $scope.completionDate.id = response.data.id;
        }

        function onError() {
            console.log('Error while adding completion date to card with id ' + $scope.card.id);
        }
    }

    function updateCompletionDate() {
        var data = {
            date: $scope.dateTime,
            completed: $scope.completionDate.completed,
            id: $scope.completionDate.id
        };

        $http.put('/cards/' + $scope.card.id + '/completion_date', data)
            .then(onSuccess)
            .catch(onError);

        function onSuccess(response) {
            $scope.completionDate.date = response.data.date;
            console.log('Completion date has been updated in card with id ' + $scope.card.id);
        }

        function onError() {
            console.log('Error while updating completion date in card with id ' + $scope.card.id);
        }
    }

    function deleteCompletionDate() {
        $http.delete('/cards/' + $scope.card.id + '/completion_date')
            .then(onSuccess)
            .catch(onError);

        function onSuccess() {
            console.log('Completion date has been deleted from card with id ' + $scope.card.id);
            $scope.completionDate.id = null;
        }

        function onError() {
            console.log('Error while deleting completion date from card with id ' + $scope.card.id);
        }
    }

    function initializeCompletionDate() {
        var cDate = $scope.card.completionDate;

        if (isDefined(cDate)) {
            $scope.completionDate = {
                id: cDate.id,
                cardId : cDate.cardId,
                completed: cDate.completed
            };
            $scope.dateTime = new Date(cDate.date);
        } else {
            $scope.completionDate = {
                cardId: $scope.card.id,
                completed: false
            };
            $scope.dateTime = new Date();
        }
    }

    function isDefined(value) {
        return value !== undefined && value !== null;
    }

    function initializeFileButtonBehaviour() {
        $(function() {
            $(document).on('change', ':file', function() {
                var input = $(this),
                    numFiles = input.get(0).files ? input.get(0).files.length : 1,
                    label = input.val().replace(/\\/g, '/').replace(/.*\//, '');
                input.trigger('fileselect', [numFiles, label]);
            });

            $(document).ready( function() {
                $(':file').on('fileselect', function(event, numFiles, label) {

                    var input = $(this).parents('.input-group').find(':text'),
                        log = numFiles > 1 ? numFiles + ' files selected' : label;

                    if(input.length) {
                        input.val(log);
                    } else {
                        if( log ) alert(log);
                    }

                });
            });
        });
    }

    function initializeToolTips() {
        $("[data-toggle=tooltip]").tooltip();
    }

    function parsedDate() {
        return moment($scope.completionDate.date).format('DD-MMM-YYYY  HH:mm');
    }
}