# API Documentation - Staff Control System

## Сортировка пользователей

### GET `/api/staffcontrol/users`

**Описание:** Получение списка пользователей с поддержкой сортировки по полям Staff.

**Доступ:** Только для пользователей с ролью `SUPER_ADMIN`

**Параметры запроса:**
- `page` (int, optional) - номер страницы (по умолчанию 0)
- `size` (int, optional) - размер страницы (по умолчанию 10)
- `sort` (string, optional) - сортировка в формате `поле,направление`

**Поддерживаемые поля для сортировки:**
- `staff.firstName` - имя
- `staff.lastName` - фамилия  
- `staff.middleName` - отчество
- `staff.phone` - телефон
- `staff.pin` - ПИН

**Направления сортировки:**
- `asc` - по возрастанию
- `desc` - по убыванию

**Примеры запросов:**

```bash
# Сортировка по имени по возрастанию
GET /api/staffcontrol/users?sort=staff.firstName,asc

# Сортировка по фамилии по убыванию
GET /api/staffcontrol/users?sort=staff.lastName,desc

# Сортировка по телефону с пагинацией
GET /api/staffcontrol/users?sort=staff.phone,asc&page=0&size=20

# Сортировка по ПИН по убыванию
GET /api/staffcontrol/users?sort=staff.pin,desc
```

**Ответ:**
```json
{
  "content": [
    {
      "id": "uuid-пользователя",
      "username": "user1",
      "email": "user1@example.com",
      "isActive": true,
      "mustChangePassword": false,
      "createdAt": "2025-01-16T17:06:09",
      "updatedAt": "2025-01-16T17:06:09",
      "staff": {
        "id": 1,
        "pin": "12345678901234",
        "firstName": "Алексей",
        "lastName": "Иванов",
        "middleName": "Петрович",
        "phone": "+996555123456"
      }
    }
  ],
  "pageable": {
    "sort": {
      "sorted": true,
      "unsorted": false,
      "empty": false
    },
    "pageNumber": 0,
    "pageSize": 10
  },
  "totalElements": 100,
  "totalPages": 10,
  "last": false,
  "first": true,
  "numberOfElements": 10,
  "size": 10,
  "number": 0,
  "empty": false
}
```

## Новый роут: Сброс логина и пароля пользователя

### PUT `/api/staffcontrol/users/{id}/reset-credentials`

**Описание:** Сброс логина и пароля пользователя с установкой флага обязательной смены пароля.

**Доступ:** Только для пользователей с ролью `SUPER_ADMIN`

**Параметры пути:**
- `id` (UUID) - ID пользователя

**Тело запроса:**
```json
{
  "newUsername": "новый_логин",
  "newPassword": "новый_пароль123!"
}
```

**Валидация:**
- `newUsername`: обязательное поле, 3-100 символов
- `newPassword`: обязательное поле, минимум 8 символов, должен содержать строчные/заглавные буквы, цифру и спецсимвол

**Ответ:**
```json
{
  "id": "uuid-пользователя",
  "username": "новый_логин",
  "email": "email@example.com",
  "isActive": true,
  "mustChangePassword": true,
  "createdAt": "2025-01-16T17:06:09",
  "updatedAt": "2025-01-16T17:06:09",
  "staff": {
    "id": 1,
    "pin": "12345678901234",
    "firstName": "Имя",
    "lastName": "Фамилия",
    "middleName": "Отчество"
  }
}
```

**Особенности:**
1. **Автоматическая установка флага:** `mustChangePassword` автоматически устанавливается в `true`
2. **Проверка уникальности:** Система проверяет, что новый логин не занят другим пользователем
3. **Валидация пароля:** Пароль проверяется на соответствие требованиям безопасности
4. **Транзакционность:** Операция выполняется в рамках транзакции

**Примеры использования:**

```bash
# Сброс логина и пароля для пользователя с ID abc-123
curl -X PUT \
  http://localhost:8080/api/staffcontrol/users/abc-123/reset-credentials \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer YOUR_JWT_TOKEN" \
  -d '{
    "newUsername": "newuser",
    "newPassword": "NewPass123!"
  }'
```

**Ошибки:**

// Пользователь не найден
```json
{
  "timestamp": "2025-01-16T17:06:09",
  "status": 404,
  "error": "Not Found",
  "message": "User not found"
}
```
// Логин уже занят
```json
{
  "timestamp": "2025-01-16T17:06:09",
  "status": 400,
  "error": "Bad Request",
  "message": "Логин 'newuser' уже занят"
}
```
// Слабый пароль
```json
{
  "timestamp": "2025-01-16T17:06:09",
  "status": 400,
  "error": "Bad Request",
  "message": "Пароль должен содержать минимум 8 символов, строчные/ЗАГЛАВНЫЕ буквы, цифру и спецсимвол"
}
```
// Недостаточно прав
```json
{
  "timestamp": "2025-01-16T17:06:09",
  "status": 403,
  "error": "Forbidden",
  "message": "Access denied"
}
```

**Безопасность:**
- Доступ только для `SUPER_ADMIN`
- Пароль хешируется перед сохранением
- Флаг `mustChangePassword` принуждает пользователя сменить пароль при следующем входе
- Валидация силы пароля
- Проверка уникальности логина
