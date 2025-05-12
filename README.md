# SMTP Server Backend

Микросервис для регистрации и авторизации пользователей на личном SMTP-сервере, реализованный на Kotlin и Spring. Поддерживает JWT-аутентификацию и интеграцию с iRedMail.

## Требования

- **ОС**: Ubuntu 20.04 или выше
- **Docker** и **Docker Compose**
- **PostgreSQL**: 13 или выше (настраивается через iRedMail)
- Доступ к VPS с публичным IP (например, 64.227.178.46)
- Настроенный домен (например, `mail.messenger-mail`)

## Установка и развертывание

### 1. Развертывание iRedMail

iRedMail используется как почтовый сервер для SMTP и IMAP. Установите его перед запуском микросервиса:

1. **Обновите пакеты**:
   ```bash
   apt-get update
   ```

2. **Настройте rDNS** у хостера (например, Timeweb Cloud):
   - Установите: `64.227.178.46` → `mail.messenger-mail`.

3. **Настройте файл hosts**:
   ```bash
   nano /etc/hosts
   ```
   Добавьте:
   ```
   127.0.0.1 localhost localhost.localdomain
   127.0.1.1 mail.messenger-mail mail
   64.227.178.46 mail.messenger-mail
   ```

4. **Установите hostname**:
   ```bash
   hostnamectl set-hostname mail.messenger-mail
   ```

5. **Установите iRedMail**:
   ```bash
   wget https://github.com/iredmail/iRedMail/archive/refs/tags/1.6.2.tar.gz
   tar -xzvf 1.6.2.tar.gz
   cd iRedMail-1.6.2
   chmod +x iRedMail.sh
   ./iRedMail.sh
   ```
   - Укажите домен: `mail.messenger-mail`.
   - Настройте пароль администратора и выберите компоненты.

6. **Перезагрузите сервер**:
   ```bash
   shutdown -r now
   ```

7. **Проверьте доступность**:
   - Админ-панель: `https://<your-ip>/iredadmin`
   - Веб-почта: `https://<your-ip>/mail`

### 2. Развертывание микросервиса с Docker Compose

Микросервис развертывается вместе с другими компонентами (бэкенд, фронтенд) через Docker Compose.

1. **Клонируйте все репозитории**:
   ```bash
   mkdir mail-client && cd mail-client
   git clone https://github.com/BogdanPronin/SMTP_server_back.git smtpauth
   git clone https://github.com/BogdanPronin/Diploma_prj_backend.git backend
   git clone https://github.com/BogdanPronin/Diploma_Prj_Front.git front
   ```

2. **Создайте docker-compose.yml**:
   В корне директории `mail-client` создайте файл `docker-compose.yml`:
   ```yaml
   version: '3.8'

   services:
     smtpauth:
       build: ./smtpauth
       container_name: smtpauth
       image: smtpauth:latest
       network_mode: "host"
       environment:
         - BACK_DB_USER=vmailadmin
         - BACK_DB_HOST=localhost:5432
         - BACK_DB_PASSWORD=M9VxYYYjcUKQAAOFsLhVJOC7JDmCuGKH
       ports:
         - "8081:8081"
       dns:
         - 8.8.8.8
         - 8.8.4.4

     backend:
       build: ./backend
       container_name: backend
       image: backend:latest
       network_mode: "host"
       environment:
         - ALLOWED_ORIGINS=https://mail.messenger-mail.ru,http://localhost:3001,http://localhost:3000
       ports:
         - "8080:8080"
       dns:
         - 8.8.8.8
         - 8.8.4.4

     frontend:
       build: ./front
       container_name: front
       image: frontend:latest
       ports:
         - "3001:3000"
       dns:
         - 8.8.8.8
       depends_on:
         - smtpauth
         - backend
       networks:
         - app-network

   networks:
     app-network:
       driver: bridge
   ```

3. **Запустите сервисы**:
   ```bash
   docker-compose up -d
   ```

4. **Проверьте доступность**:
   - Микросервис: `http://<your-ip>:8081`
   - Пример эндпоинта: `POST /api/auth/login`

## Тестирование

- Используйте Postman для проверки:
  - `POST /api/auth/register` — регистрация.
  - `POST /api/auth/login` — авторизация (возвращает JWT).

## Примечания

- Убедитесь, что PostgreSQL (часть iRedMail) доступен на `localhost:5432`.
- Откройте порты 8081, 587 (SMTP), 143 (IMAP).
- Настройте SSL для `https://mail.messenger-mail`.
