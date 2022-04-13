let language_url = "http://cdn.datatables.net/plug-ins/9dcbecd42ad/i18n/Russian.json";
//"http://cdn.datatables.net/plug-ins/9dcbecd42ad/i18n/English.json"

const role_enum = Object.freeze({
    ADMIN: 'Администратор',
    DEPARTMENT_HEAD: 'Глава отдела',
    PROJECT_MANAGER: 'Проектный менеджер',
    EMPLOYEE: 'Сотрудник'
});

const task_status = Object.freeze({
    OPEN: 'Открыта',
    IN_PROGRESS: 'Выполняется',
    DONE: 'Выполнена',
    CLOSED: 'Завершена'
});

function show_preloader() {
    $("#preloader_malc").show();
}

function hide_preloader() {
    $("#preloader_malc").hide();
}