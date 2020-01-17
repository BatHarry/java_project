# java_project
Електронния домоуправител е приложение за следене и за събиране на такси от клиенти на частна домоуправителна фирма. Програмата предоставя следните функционалности:
  1. Добавяне/Редактиране/Триене на клиенти
  2. Добавяне на работници.
  3. Разпределяне на работници по обекти.
  4. Въвеждане на фиксирана такса за събиране за всеки обект.
  5. Съхраняване на информация за всеки апартамент.
  6. Следене на платени и неплатени такси.
След като стартирате приложението, командите, с които разполагате, за да ползвате различните функционалности на приложението ще се изпишат на прозореца. Командите генерално следват следната структура
command -option_1 -option_2 -option_3. Комбинация от същата команда, но различни допълнителни параметри извършват различни процеси. Приложението ползва база от данни за съхранение на всичката информация. Необходимo e да инсталирате, ако все още нямате, MySql сървър в комбинация с официалния драйвер (Connector/J) за Java в противен случай приложението няма да работи. След като сте сигурни, че разполагате с MySql, можете да оптворите проекта. Програмата може да бъде отворена от клас Main от main метода.

След като програмата е успешно заредена в командния интерфейс, въведете командата ви след представената ви подсказка "Enter command:". Следва подробно описание на всяка една от командите (В следващите описания под клиент се разбира цяла сграда съставена от n на брой апартаменти):

  1. clients 
     С тази команда програмата се свързва с базата данни и извежда всеки клиент на екрана.
     Командата изписва "id" и адрес на сградата. Ако искате по-подробна информация за даден клиент е 
     необходимо да запомните неговото "id" и да ползвате следващата по ред опция от менюто.
  
  2. client -ID
     Както споменахме по-горе тази команда ви предоставя повече информация за клиента, но без да знаете
     неговия идентификационен номер ("id") това не би било възможно. Заменете ID с номера на желания от 
     вас клиент. Пример:
         
	client -4
	
   Примерът ще изведе клиент с идентификационен номер 4 на екрана.
 
  3. client -ID -edit
     Да кажем, че е допусната грешка при създаването на клиент или пък е нужна промяна. В този случай
     ще ви се наложи да редактирате вече въведен клиент. С тази команда вие бихте могли да извършите 
     именно тази операция. Подобно на последната команда е необходимо да предоставите идентификационен
     номер и добавите опция "-edit". Това ще започне процедура на попълване на нови стойности за
     параметрите на клиента. Ако не желаете да променяте дадена стойност просто натиснете бутона "Enter"
     на клавиатурата и старата стойност ще бъде запазена. Пример

	client -4 -edit

  4. client -ID -tax -AMOUNT 
     При създаване на нов клиент е нужно да оточните каква такса ще се изисква за всеки апартамент.
     Освен идентификационен номер се изисква и стойност на таксата за последния параметър "AMOUNT".
     Пример:

	client -4 -tax -25 

  5. client -ID -delete
     Командата трие клиента с подадения идентификационен номер от вашите списъци.	

  6. employees
     Аналогично на команда 1, тази извежда всички регистрирани работници във вашата фирма

  7. employee -ID
     Аналогично на команда 2, тази извежда по-подробна информация за даден работник

  8. employee -ID delete
     С тази команда вие ще изтриете работник от списъците ви

  9. assigned -CLIENT_ID
     Ако искате да проверите кой от вашите работници е отговорен за даден обект използвайте тази команда.
     Необходимо е да подадете идентификационен номер на клиент.
     
  10. assign -CLIENT_ID -EMPLOYEE_ID
      Ако току-що сте създали нов клиент, то ще трябва той да бъде разпределен на някой отговорен, който да събира таксите.         Командата приема две променливи, за които последевателността е критична! Първо подайте номер на клиента, а след това и       номер на ваш работник. Пример:

	assign -2 -6
  
  11. create -TYPE
      Тази команда ще ви преведе през процедура за създаване на нов клиент или работник. Възможните опции са - "client" за 
      нов клиент и "employee" за нов работник. (Командите за добавяне на апртамент са различни) Пример:
      
        create -client
  
  12. apartments -CLIENT_ID
      Ползвайте, за да изведете всички апартаменти (подклиенти) за определен клиент (сграда). Въведете клиентски номер
      като параметър. На екрана ще са изписани клиентски номер, име на фамилията и колко пари дължат от такси.
      
  13. apartment -ID
      Изведете подробна информация за апартамент. Нужно е да знаете неговия идентификационен номер.
      
  14. apartment -CLIENT_ID -create 
      Процедура за създаване на нов апартамент. Като втори параметър въведете индетификационен номер на КЛИЕНТ!
      
  15. apartment -ID -pay 
      Фунцкия за плащане на клиентска такса. Като втори параметър въведете идентификационен номер на апартамента.
      Тази команда ще запише плащане за най-старите задължение. Ако апартаментът дължи пари за януари, февруар и март,
      процедурата ще отметне януари. 
      
  16. apartment -ID -edit
      Процедура за редактиране на апартамент
      
  17. total -owed/collected (optional) yyyy-mm
      Командата връща информация за общо събрани и несъбрани пари от всички клиенти. Пример:
      
	total -owed
	total -collected
	total -collected 2019-08
	
   Незадължителния параметър yyyy-mm представлява дата. Може да се използва само с "collected"
      
  18. menu
      Тази команда извежда всичко опции от менюто.
      
  19. exit
      Използвайте, за да затворите програмата
      
