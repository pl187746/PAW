(function () {
    'use strict';

    angular
        .module('trello')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider', '$translateProvider'];

    function stateConfig($stateProvider, $translateProvider) {
        $stateProvider.state('app', {
            abstract: true,
            views: {
                'navbar@': {
                    templateUrl: '/app/navbar/navbar.html',
                    controller: 'NavbarController',
                }
            }
        })
        $translateProvider.translations('en', {
            BOARD_SEARCH: 'Boards',
            LOGGED_AS: 'Logged as',
            ACCOUNT_DROPDOWN: 'Account',
            SIGN_IN: 'Sign in',
            SIGN_OUT: 'Sing out',
            REGISTER: 'Register',
            MEMBERS_DROPDOWN: 'Members',
            ARCHIVE_DROPDOWN: 'Archive',
            ARCHIVE_LISTS: 'Lists',
            ARCHIVE_CARDS: 'Cards',
            LABELS: 'Labels',
            BOARDS_HEAD: 'Boards',
            TEAMS: 'Teams',
            ATTACHMENTS: 'Attachments',
            CARD_COMMENTS: 'Comments',
            ADD_COMMENT: 'Add comment',
            UPLOAD: 'Upload',
            BROWSE: 'Browse...',
            DIARY: 'Diary',
            BUTTON_TEXT_EN: 'english',
            BUTTON_TEXT_PL: 'polish',
            LOG_IN: 'Log in',
            REMEMBER_ME: 'Remember me',
            LOGGED_IN_SUCCESS: 'Logged in successfully',
            LOGGED_IN_FAILED: 'Failed to sign in!',
            CHECK_CREDENTIALS: 'Please check your credentials and try again.',
            PASSWORD: 'Password',
            DOESNT_HAVE_ACCOUNT: "Don't have account yet?",
            NEW_TEAM: 'New team',
            ADD_MEMBER: 'Add member',
            LIKE: 'Like',
            UNLIKE: 'Unlike',
            COMPLETION_DATE: 'Completion Date',
            ADD: 'Add',
            UPDATE: 'Update',
            DELETE: 'Delete',
            FINISHED: 'Finished',
            COMPLETION_DATE_ADD_SUCCESS: 'Completion date has been added',
            COMPLETION_DATE_ADD_ERROR: 'Completion date couldn\'t be added',
            COMPLETION_DATE_UPDATE_SUCCESS: 'Completion date has been updated',
            COMPLETION_DATE_UPDATE_ERROR: 'Completion date couldn\'t be updated',
            COMPLETION_DATE_DELETE_SUCCESS: 'Completion date has been removed',
            COMPLETION_DATE_DELETE_ERROR: 'Completion date couldn\'t be removed'
        });
        $translateProvider.translations('pl', {
            BOARD_SEARCH: 'Tablice',
            LOGGED_AS: 'Zalogowany jako',
            ACCOUNT_DROPDOWN: 'Konto',
            SIGN_IN: 'Zaloguj się',
            SIGN_OUT: 'Wyloguj się',
            REGISTER: 'Rejestracja',
            MEMBERS_DROPDOWN: 'Członkowie',
            ARCHIVE_DROPDOWN: 'Archiwum',
            ARCHIVE_LISTS: 'Listy',
            ARCHIVE_CARDS: 'Karty',
            LABELS: 'Etykietki',
            BOARDS_HEAD: 'Tablice',
            TEAMS: 'Zespoły',
            ATTACHMENTS: 'Zalączniki',
            CARD_COMMENTS: 'Komentarze',
            ADD_COMMENT: 'Dodaj komentarz',
            UPLOAD: 'Zalącz',
            BROWSE: 'Przeglądaj...',
            DIARY: 'Dziennik',
            BUTTON_TEXT_EN: 'angielski',
            BUTTON_TEXT_PL: 'polski',
            LOG_IN: 'Logowanie',
            REMEMBER_ME: 'Zapamietaj mnie',
            LOGGED_IN_SUCCESS: 'Zalogowano się pomyślnie!',
            LOGGED_IN_FAILED: 'Błąd podczas logowania!',
            CHECK_CREDENTIALS: 'Sprawdź swoje dane i spróbuj ponownie.',
            PASSWORD: 'Hasło',
            DOESNT_HAVE_ACCOUNT: "Nie masz jeszcze konta?",
            NEW_TEAM: 'Nowa drużyna',
            ADD_MEMBER: 'Dodaj członka',
            LIKE: 'Lubię',
            UNLIKE: 'Nie lubię',
            COMPLETION_DATE: 'Terminarz',
            ADD: 'Dodaj',
            UPDATE: 'Aktualizuj',
            DELETE: 'Usuń',
            FINISHED: 'Zakończone',
            COMPLETION_DATE_ADD_SUCCESS: 'Nowa data ukończenia zadania została dodana',
            COMPLETION_DATE_ADD_ERROR: 'Nowa data ukończenia zadania nie mogła zostać dodana',
            COMPLETION_DATE_UPDATE_SUCCESS: 'Data ukończenia zadania została zaaktualizowana',
            COMPLETION_DATE_UPDATE_ERROR: 'Data ukończenia zadania nie mogła zostać zaaktualizowana',
            COMPLETION_DATE_DELETE_SUCCESS: 'Data ukończenia zadania została usunięta',
            COMPLETION_DATE_DELETE_ERROR: 'Data ukończenia zadania nie mogła zostać usunięta',
        });
        $translateProvider.preferredLanguage('en');

    }
})();