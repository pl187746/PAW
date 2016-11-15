'use strict';

angular
    .module('trello')
    .config(stateConfig);

stateConfig.$inject = ['$stateProvider'];

function stateConfig($stateProvider) {
    $stateProvider.state('entity', {
        abstract: true,
        parent: 'app'
    });
}