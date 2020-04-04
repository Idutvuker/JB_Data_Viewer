# CSV Data Viewer
Тестовое задание для проекта "Powerful Data Viewer for Python" в JetBrains.

Реализована загрузка больших таблиц по частям. 

## Структура
`Application` отвечает за главное окно приложения.
* Обрабатываются случаи отсутствия CSV файла или интерпретатора.

`DataProvider` общается с запущенным скриптом и получает от него данные.

`StringTable` предоставляет табличные данные для JTable, используя `DataProvider`.  
* Загружает необходимые данные в кэш с некоторым запасом (`LOAD_BUFFER_SIZE`). 
* Когда JTable запрашивает данные в последних (`LOAD_THRESHOLD`) загруженных в кэше строках, 
`StringTable` догружает данные с помощью `DataProvider`.

## Использование
Открыть файл: `File -> Open CSV`.

Изменить интерпретатор: `File -> Settings`. 

## Примеры
В папке examples лежат примеры CSV файлов.

## Тесты
Написаны тесты для классов `DataProvider` и `StringTable`.

## Зависимости
* Swing
* JUnit 5