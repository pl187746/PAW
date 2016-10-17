(function() {
    'use strict';

    angular
        .module('trello')
        .factory('stateHandler', stateHandler);

    stateHandler.$inject = ['$rootScope', '$window'];

    function stateHandler($rootScope, $window) {
        return {
            initialize: initialize
        };

        function initialize() {
            var stateChangeStart = $rootScope.$on('$stateChangeStart', function (event, toState, toStateParams, fromState) {
                $rootScope.toState = toState;
                $rootScope.toStateParams = toStateParams;
                $rootScope.fromState = fromState;

                if (toState.external) {
                    event.preventDefault();
                    $window.open(toState.url, '_self');
                }
            });

            var stateChangeSuccess = $rootScope.$on('$stateChangeSuccess',  function(event, toState, toParams, fromState, fromParams) {
                var titleKey = 'basic' ;

                if (toState.data != undefined && toState.data.pageTitle) {
                    titleKey = toState.data.pageTitle;
                }

                $window.document.title = titleKey;
            });

            $rootScope.$on('$destroy', function () {
                if(angular.isDefined(stateChangeStart) && stateChangeStart !== null){
                    stateChangeStart();
                }
                if(angular.isDefined(stateChangeSuccess) && stateChangeSuccess !== null){
                    stateChangeSuccess();
                }
            });
        }
    }
})();