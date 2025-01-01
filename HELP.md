Attendance:
employeeName (meno zamestnanca, povinné)
date (dátum dochádzky, povinné)
workedHours (počet odpracovaných hodín, default 0)
isPresent (boolean, označuje, či bol zamestnanec v tento deň prítomný)
zobraziť všetko
môcť pridať dochádzku cez form
môcť zvýšiť počet odpracovaných hodín konkrétnemu záznamu
môcť upraviť konkrétny záznam
môcť vymazať záznam
vedieť nájsť záznam podľa mena zamestnanca
vedieť nájsť záznam podľa dátumu
môcť zobraziť len záznamy kde isPresent je true


Attendance:
employee - vztah manytoone/onetomany
date (dátum dochádzky, povinné)
workedHours (počet odpracovaných hodín, default 0)
isPresent (boolean, označuje, či bol zamestnanec v tento deň prítomný)
Employee:
firstName
lastName
position
sort podľa
priezviska
datumu
odpracovanych hodín
isPresent
pagination
pridať security pre entitu Employee
bonus spraviť security s novou entitou Role so vztahom manytomany s Employee
vyskúšať si aj onetoone - napr. každý zamestnanec nech má
Employee Card:
serialNumber
activationDate
expirationDate