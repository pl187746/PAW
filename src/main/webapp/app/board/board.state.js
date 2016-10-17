(function() {
    'use strict';

    angular
        .module('trello')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider.state('board', {
            parent: 'app',
            url: '/board/{id}',
            data: {
                pageTitle: 'Board'
            },
            views: {
                'content@': {
                    templateUrl: '/app/board/board.html',
                    controller: 'BoardController'
                }
            }
        })
    }
})();