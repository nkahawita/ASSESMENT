-- db
create
database assessment;

-- table holds the data on global covid 19 pandemic

create table `global_covid_info`
(
    `COUNTRY` varchar(32),
    `REGION`  varchar(32),
    `CASES`   int,
    `DEATHS`  int,
    PRIMARY KEY (`COUNTRY`, `REGION`),
    UNIQUE (`COUNTRY`)
) ENGINE=InnoDB;

