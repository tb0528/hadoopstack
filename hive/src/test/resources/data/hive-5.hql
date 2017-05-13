create table t5_employee(
    id int comment "员工ID",
    name string comment "员工姓名",
    subordinate array<int> comment "下属员工ID",
    salary float comment "员工薪资",
    tax map<string, float> comment "税收",
    np struct<province:string,city:string,zip:int>
);
