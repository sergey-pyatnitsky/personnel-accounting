const role_enum = Object.freeze({
    ADMIN: 'Администратор',
    DEPARTMENT_HEAD: 'Глава отдела',
    PROJECT_MANAGER: 'Проектный менеджер',
    EMPLOYEE: 'Сотрудник'
});

function show_preloader() {
    $("#preloader_malc").show();
}

function hide_preloader() {
    $("#preloader_malc").hide();
}