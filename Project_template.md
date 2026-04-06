# Задание 1. Анализ и планирование

### 1. Описание функциональности монолитного приложения

**Управление отоплением:**

* Пользователи могут
    * Включать прибор отопления
    * Выключать прибор отопления

* Система поддерживает
    * Опрос приборов для получения их статусов
    * Отображение подключенных приборов и статусов
    * Включение и выключение отопительных приборов
    
**Мониторинг температуры:**

* Пользователи могут
    * Включать датчик
    * Выключать датчик
    * Получать показания датчика

* Система поддерживает
    * Опрос датчиков для получения их показателей
    * Отображение подключенных датчиков их показателей и статусов
    * Включение и выключение датчиков

### 2. Анализ архитектуры монолитного приложения

* **Язык программирования:** Go
* **База данных:** PostgreSQL
* **Архитектура:** Монолитная, все компоненты системы (обработка запросов, бизнес-логика, работа с данными) находятся в рамках одного приложения.
* **Взаимодействие:** Синхронное, запросы обрабатываются последовательно.
* **Масштабируемость:** Ограничена, так как монолит сложно масштабировать по частям.
* **Развертывание:** Требует остановки всего приложения.

### 3. Определение доменов и границы контекстов

* Домен: Управление устройствами
    * Контекст: мониторинг датчиков
    * Контекст: управление отопительными приборами

* Домен: Обслуживание клиентов
    * Поддомен: Управление подключением новых пользователей
        * Контекст: планирование расписания подключений
        * Контекст: распределение задач выездным специалистам

### **4. Проблемы монолитного решения**

- Текущее приложение целиком заточено на управление отоплением, добавление новых видов датчиков и устройств будет сложным
- Структура приложения недостаточно разделена на слои
- Высокая связность компонентов приложения
- Решение существует только для домена управления устройствами
- Приложение трудно масштабировать горизонтально, каждый инстанс будет опрашивать все датчики

### 5. Визуализация контекста системы — диаграмма С4

Диаграмма Контекста **текущей** системы (в формате puml лежит также в директории schemas)

[СurrentContextDiagram](schemas/СurrentContext.puml)

![Диаграмма контекста текущей системы](https://img.plantuml.biz/plantuml/png/XPDDIyD048RlWVo7CHTR44NeKQGQ1Jq8nI8UozACoK9sDkvii4NyxyucQRfjGNC8cTcNVTxvo9ozShurTPgSQLhNRO5may-kyjagjwQnXEHlqoHDlFOrmhjovicsZ91RyhZnyA1LwPG9aWKwjZJ9M_RMeBk0B98FoKooTx0U8h0VuAlusY_jAs0Zdg0InH0ZiNKCYWgeK7dXN6RJ4VXCKaHT7n77R8SaGDFKTejoPMMXgTkIGHE8qfqpu6Uh6oErWhUH6zwoHzEnqcJP1QjxsrAXt7QolevvsHI-qmJawNMJyQIF3xprgM2i_mAZI9MQIf2WGQTyw_2ax70-6WsU8jfGhOWiyKWyoFv1RPnT8tCOH-XCbvFcxCRqJt-2bP-eGuyRFox1axm4k0iXn1miT1y1k8TqizwJEYTOY943vd9afiahrj6wdNRyZGKsxNNHbXoPMmpBi5EUt7EaVx6alGpc0bv20qyGXqE8j_EEMPV4lVavKj7zUBy0)

# Задание 2. Проектирование микросервисной архитектуры

Диаграммы toBe системы (в формате puml лежат также в директории schemas)

**Диаграмма Контекста (Context)**

![Диаграмма контекста ToBe системы](https://img.plantuml.biz/plantuml/png/PLB1QeGm4BqB_iFKamkbfR37KhQrXRqKIgNqk0ITQi0aDZEXAwN_tcJZYguUeZFpPjvx4tV4mh9JNPfSILrrhaQuBxQtnVPG6DKRZPeVqYHDM7A7y2cisXj728NHZ4U6PoaQArI0lA4be_F24Hk5zXgo6NnCPZxx2DKOWVa2RkTZVoItGCfhWjOZnXWr6KiWT0qj2lOyDzacK9O3CQh3at6w5dR8gxivQxQ1tpG1_qHSFbLYl50OKq5V_08bj6YaRW2_dUoLNqJW_9lHlXnvShltX-yzXn3YC2oyAVWyUwReQHeAVUUQ1ckGcWsiz9nilcCtM-lQnGSX1UGITo6jH2Pm_T9Ew5ZDksXy3OjO4yJgMao9kYPWx50Xsy5JrlxAA3hP-UBfR_i7)

[С1](schemas/С1.puml)

**Диаграмма контейнеров (Containers)**

![Диаграмма контейнеров ToBe системы](https://img.plantuml.biz/plantuml/png/fLTHRzem47v7uZ_SU1cLqAPDVPocjb0LTQ9ZXArxgvnmGDR4pcpJ3atzxpl7JkA4f0Aj3rNitFVTT-UxSxXGcaczIvD-xqtCesItGlWqFdy_FdyO2wvPp55-xlVwFHth1E6UoNGgTWgXV0kJc6qaIurHi5SQquShiUCh9lV3wCC81YLcS0P_-psWlpbA9VXmYT4ss6MPa9hip0hSSa3hwpHBn1uHra92j5DQf0G3RSoKCpkpVAMOOO1S2QakivWOt08kioo98wPZmGtnBNjYmFWAWap6V0Ep7sEpFMESRSnQMTW1-IGiw2UXnpwvCkYvK7eZKPddG0lf4S6AQNOeTO9FSOHEgbkSAdLbOMrI7MCkjM3lbkg8EgJEK2csmIif7b4Q_tODTiFmV67hHpQ21Gl3MC--ZU0spVclEGiJuaqT8imHncE8-XcHKv9aUmp3cGZZ12aZncF-t4pFJvOo8ZEFbqaYdXLGcKdG0bAJ8InFmAImArV3Umoj4tfeEu0Wad565RhSPrWyBv15-Z_zUkLl_ViTO8JaXxNNMy57CFd5-4OC2YlN081_eYszmBOAyFZpAl3MhrI2tryTvN0dTBnsIL84hQsFB67gdfK2xYEDenSpHjnu6BkUzsS859jvxikn068DUklFf-TOKnXK7XgsPE7Mth5iaMdoyyvuRPbS7uc_y0LJ-bzDhp8H3zT_T34Ige5Wb5b70u_ZhOAWOwxaDAv5gsOrO5iw3Pf9LKyldlHSIwfkAAfdjZToPW6ubKrutcVvD3Ml3bBVxoqmyP9UjiK2V-zGQGMr2NtMjFVRzKY8dpiotcM79bLUEargSoBlFzNQWFHwkbpEWqE6CjHZuPQWb5-xTHQuYPKfhNn_PMwi15FKSa_ggQ4irSrYFYw4b37MheJJU6gASe8gkIwgfjiMOtknqIJmSb3CW2QaUIbOaVY4N9FJZlBo8woEjaDWYx4li7bdDI7j0Zk4DOnh9-57LZXgsBUFH19NJ2ZFKrMkjRffrDv1kNguche2rrNc7iwRgC7MN5lpxy7qC2CDrC4zrTdT5bfqqqdH5Q2JGYj0NbnVUK8pjJscmlpKW6hVgIrW9P8dKfb9GTywQUckQTdzkGjEm178gW7ytR87N9jwzu1_vdaTc-5_rvQUHWtNBvCIsfhP1hISfnRssfaqaFuij-3s4kttBkXj_hlb7m00)

[С2](schemas/С2.puml)

**Диаграмма компонентов (Components)**

![Диаграмма компонентов ToBe системы](https://img.plantuml.biz/plantuml/png/xLbDR-Cs4BqRy7zWVUaki43waMDHR6mdcwJDrh2IRcyBMg9j8P8ea5GIOz7_tXbIb4XAikDajxqq1qCQaMyUXyD73lDUAYfKbMVZqNTf4MTLmiWlizEJsUdd6SzBNh12_JeUZKSgLHaZdwZ8Btab6PdnGj6qO8BCKxeHDCT6JPStDtoLPkoiBD-HYNacy9AbCLKfBoPW_OlcLAJwyIpB-ACa02k8uYId1TqmahDynOJSfe0XMI6va8GM2KdOGnepENdh-ll4LiOPF8IUhka3ZMAHbkeTkTsLp3ul6OtLL_9_mwI4JbF1xvd0CPjtOWpev3UwlgSdIxfQfUhaEfTeCr_JOWFEi3qy-R3JYmjiHkDxikQ2h0L47Jj-XAPF-A2RIYP9GXN5xpcXxUXzalFLcncDjmowBLcIIXBZsyI-QZYqI29O9UaAvioWWcUC3O8XN3tndwUyAX8gTc_Y7w3h7KRlB8x1gBmPU4k-Z4S4_jfXHUcck2iHIV0iqm534xahIMj4Ff4EpisViy1UprZ17fjPamXGbKYow-ogw3gxAevpnfvIgR25vn4okECHGt5LhBddKyS7hQ_qMcUdsh9Ka18Ib1SqqnFL9NBJfB17fJL_JIxjUkaIgVDYVlR7xtIdETX7xRRKVg_dqnc-tcZKUZtQWVtjwGkaCiRJmgx2_9jF4MZ1fTe8fZCxKbmqr2reVpR_YCth1NyeaNMpMvQnd2cnywBSM8DWVbWkPYHY0bLauhW2ZKdgKAgcQ_-y1cvhCC-dwy24TyfeuOQtRJlVuyIqpi7Who3zaRHVmqmgXr8fZbcfP9qwkcVCStXBf94lgpJxoRH3xZ8P7dg_qDVY_em8P2dyzezUVsgcjts5fad_Q551zdi-Vu1dkH0ygM8TTMCWrkAEDDnUcaOj2MPwnnJ6dv3LhjdLcdGR8lAHgtHjznI7ZsT_AQt20ta1EtcSh5ZSG5bg7G1biQWhPt4E2qe8rwyfFo4tSt0uf2ccsUiqnRewGbC2jzz6KuoJ0Kqn7_yzJGd9j4FkCZbMKvn3ue48M4tf6VsBD6NGiwif43zPvQwcMClUnMiRwRKWEBZ1esUjAphIWufcXlUV24aT4MS5W7ORqaCyZvGKws5GKYpgobcRmv8ImlLBoi-Tk1qG5g-nam6k_JLvuCU_JWSna8rGfuLyMYN08iuVsZEKW3ocQakYwDnPbOy0vDH3P4D138Ks1soT6YTODhdyG1LxfBjs-uSGr3PtLHfIIoPLPwr6Dv6Xvtk5vMfqOojujTdgPZLmbyYYabi_P9OFVWdZrH2uI0LRKQZOuu1JYRqwV79Jh4FGcaIETbAntCcyKwmKAwauviab_BhPPXFr_AdPdzjz5BqRYPWu6Dz3W4BTS67g5TEk7Uni5aa0Cs_5pwMkkJJpY7ISG6jsEarr5k0dBoe8vk3OUCuoNYLkVBKXH5lSHPTTkF5eoRA1Grvuat3CsQr57H3QVl2ZJc3eOw-O7-VZW3YZjm1mO1-Rmpp4oXxlRC0WewtUKbSTUv671D92TnQBl-O21xMBzYBCNEPS5Og9DKL6MX6aJYtCcylRsqLagTaBhMTtCjrqStlBqzpcI90GoMJGRE0MHk8jIaYdwT1paoA9kSRhTkYz2F7A-nI-zlSTkhkeUpkt3VrSzrMlGIdGZx3th1hi_4CpyN-8lqs86uIznuWbYrdw012TyqJFV7NtUN_7QFRnZffZxy7DETmk3CxXRK7Q-FONWaksIK42wcQ9gvzL2UT64o-i6_nzeLjuU7jqVraHGZNxyRvIzNY6GwLHk_q_Yw41wzmuXlU5UDY6rF2l3V707HpplV0vTO5tIs2mcik2OPXEUU5M1ZLCKo3qmun7xo4C-bys_m00)

[С3](schemas/С3.puml)

**Диаграмма кода (Code)**

![Диаграмма кода ToBe системы](https://img.plantuml.biz/plantuml/png/tLJDRjim3BuRy3i8EQNRMcmx3aNHR5sM04bRD7NlQiKwsYp9aEG0mR1t7oNAVrdI1rWE8VbHfCXFfA-TvzOtgigpF0FTABRnt0FxdMSCVtVpUL9kISvdorNny3tF_lG1e9snZ_kwYte4LOFblh50mA9HKaY_9sjjTg10Ux9kZNJfs3VpcPIDChzWUCSDxEGB3EzO09PfspYDsQNHKfU4_41dyd65fP2-akNMfxGl5NUEBOpgChulYkKDao9P6syn7TCOdv2P41OmY9DCw4ewVqaSN34NvSX5fLyn4UNHg1ByhAh8SjEpje1e1oTfN_TBCGqrdXrBJpd8wdEG7Poa3NCCQ2WSs34DpHiy90GKbzKOghT6jwTsHWhcPAcBkYkdmvPwZ26buIccezO2qZOMzBwj-6pe2F94IsjGps3TLjPTOpqK1ydZtp-UCFsDOowAbiIm43jUIL5e7reUN9CUwSG8zPink8mMBDmmeWgtHTyTq1AnrzSnQA54Ti0UeWhNgiEUZQc0QoJsP-FyFRVuV26cqpwm9rurq9vVy_hoq1UvVm0ky6aZzuE-qs0TxtkmhVzFtt579fycxFoSJJvUNBoRnA_75xRbE2Xn8iTUUltJVc8f-KVPKVGJcGxasJLe4J_KVm40)

[С4](schemas/С4.puml)

# Задание 3. Разработка ER-диаграммы

Диаграмма в формате puml лежит также в директории schemas.

![ER-Диаграмма](https://img.plantuml.biz/plantuml/png/hLVBRfmm5DqZyGzNB97c1qPHb2fHrQZgAfarSi4LLh51s2GTPURVQx3n0ymZZyq8uyEvvpwEOUwuGAteQ1L7_2zX3MeHXOemBCuDXheLPHr7SOIP8E8CoNt7HKrncm3YaEi5lCKHG0gaE0Bhw6_Szkl3eVzbYE8Z2FnFzAiASP7vjvgoPcPvTMFzmZqLBqapHADckHOlDQeokmqtDzz_tjwgVSkJzJdkWCaIYOw7WI-e8iL9Agva3eHYYQNDOYBmB2DEiXbKg9HaN3-dCIk07hHH9NWQT4xfbVhD00eM3F53bdDWBUM5BJuSuNHwVF1x3K66RqNHOgwa8NMjUO8zxECCvUxvdMBdainE3XSjniAkoxhZpc0XESk2IWL1HK-OyLgDCHykD_Aol2aioKirrUfSQSZoJ0Lash5n0dnyP3IfCowUgC-CYgQ-hob5h72vStLhhUsRrTCSjXVIVTrgZe-i83aINgSpCjxS8IRKvasTxi04suahJB5eprvFnvkRzK-dUOHLJCepZCHuGYqVLW_uXUHgs8hXyakCeQXMZQ4XEusXP5fZE04-RWnDwXZ34_KPOsXgrnYQUuSnDglt3cDe8RunT81TnXYnWJPlwbmqnjXEpnWZtNvZJFFu2cEOhvpBvN2etuOXFq9I8fvCDYyNvxrh8CvtomoKgL5C28VwpnPsdAuUJPX43wzZZTDlLHFLx4d5whYGMtdD129ikg-gvk_hbw9cTquyHsqGCrFfe8mMso4ZM1yscioP92TW43CBwA1CSe60xglK5sVSbF2kQUGty3hSfPu_O3rrzPuoKosVUcs9mBO8NX3_bF13BCCNDCryVlMU2cgAeplC2laFuJy0)

[API](schemas/ER.puml)

# Задание 4. Создание и документирование API

### 1. Тип API

Для взаимодействия сервисов планирую использовать три типа api: 
* синхронные
  * RESTful для простых взаимодействий
  * GRPC для обмена с устройствами, в частности, быстрого получения большого количества телеметрии от устройств
* асинхронные
  * AsyncApi для брокера сообщений (Kafka/Jms/RabbitMQ), чтобы использовать event-driven подход, чтобы обеспечить быстрый, удобный и масштабируемый способ обмениваться ивентами и нотификациями

### 2. Документация API

Схема OpenApi/Swagger находится в папке schemas, файл API.yml

[API](schemas/API.yml)

# Задание 5. Работа с docker и docker-compose

Реализовал temperature-api + docker-compose для сервисов

# **Задание 6. Разработка MVP**

Необходимо создать новые микросервисы и обеспечить их интеграции с существующим монолитом для плавного перехода к микросервисной архитектуре.

### **Что нужно сделать**

1. Создайте новые микросервисы для управления телеметрией и устройствами (с простейшей логикой), которые будут интегрированы с существующим монолитным приложением. Каждый микросервис на своем ООП языке.
2. Обеспечьте взаимодействие между микросервисами и монолитом (при желании с помощью брокера сообщений), чтобы постепенно перенести функциональность из монолита в микросервисы.

В результате у вас должны быть созданы Dockerfiles и docker-compose для запуска микросервисов. 