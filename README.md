# 個人書籤管理器 (Personal Bookmark Manager) - v2

## 1. 專案概述 (Project Overview)

本專案旨在開發一個個人化的書籤管理工具，讓使用者可以更有效率地儲存、組織、搜尋及管理自己的網路書籤。相較於瀏覽器內建的書籤功能，此工具提供更豐富的描述欄位、標籤系統與更強大的搜尋篩選功能，解決書籤一多就難以尋找和整理的痛點。

## 2. 功能特色 (Features Implemented)

*   **新增書籤 (Create)**: 輸入 URL (必填)、標題、描述、標籤 (選填)。URL 格式會進行驗證。
*   **查看書籤列表 (Read)**:
    *   **卡片模式 (Card View)**: 每張卡片顯示書籤的標題、描述、連結與所有標籤。
    *   **表格模式 (Table View)**: 以表格呈現書籤，包含標題、連結、標籤欄位。
    *   提供視圖切換功能。
*   **編輯書籤 (Update)**: 修改現有書籤的所有資訊。
*   **刪除書籤 (Delete)**: 刪除書籤前有確認提示。
*   **搜尋與篩選 (Search & Filter)**:
    *   **關鍵字搜尋**: 搜尋書籤的「標題」和「簡短描述」。
    *   **標籤篩選**: 點擊書籤上的標籤以篩選包含該標籤的書籤。
    *   提供清除搜尋/篩選的選項。
*   **使用者介面 (UI)**: 簡潔、直觀，易於操作。
*   **響應式設計 (RWD)**: 網頁能在桌面和行動裝置上正常顯示與操作。

## 3. 技術棧 (Technology Stack)

*   **後端 (Backend)**:
    *   Java 11
    *   Spring Boot (v2.7.5)
    *   Spring Data JPA
    *   Maven (for dependency management and build)
    *   H2 Database (in-memory, for development and testing)
*   **前端 (Frontend)**:
    *   Vue 3 (Composition API with `<script setup>`)
    *   Vite (build tool)
    *   Pinia (state management)
    *   Vue Router
    *   Axios (for API calls)
    *   Node.js and npm (for frontend development environment)
*   **API 介面**:
    *   後端以 RESTful API 形式提供對書籤資源的 CRUD 操作。

## 4. 專案結構 (Project Structure)

```
.
├── frontend/       # Vue 3 Frontend Application
│   ├── public/
│   ├── src/
│   │   ├── assets/
│   │   ├── components/ # Reusable Vue components
│   │   ├── router/     # Vue Router configuration
│   │   ├── store/      # Pinia store (bookmarks.js)
│   │   ├── views/      # Page-level components (HomeView.vue)
│   │   ├── App.vue
│   │   └── main.js
│   ├── package.json
│   └── vite.config.js
├── src/            # Spring Boot Backend Application (Maven structure)
│   ├── main/
│   │   ├── java/com/example/bookmarkmanager/
│   │   │   ├── BookmarkManagerApplication.java
│   │   │   ├── controller/
│   │   │   ├── model/
│   │   │   ├── repository/
│   │   │   └── service/
│   │   └── resources/
│   │       └── application.properties
│   └── test/         # Backend tests
│       └── java/com/example/bookmarkmanager/service/
│           └── BookmarkServiceImplTest.java
├── pom.xml         # Backend Maven configuration
└── README.md
```

## 5. 安裝與執行 (Setup and Run)

### 5.1 環境需求 (Prerequisites)

*   Java JDK 11 或更高版本 (for Backend)
*   Apache Maven 3.6.x 或更高版本 (for Backend)
*   Node.js 16.x 或更高版本 (includes npm, for Frontend)

### 5.2 後端 (Backend - Spring Boot)

1.  **進入專案根目錄 (Navigate to project root directory)**:
    ```bash
    cd path/to/bookmark-manager
    ```

2.  **編譯並執行後端應用程式 (Compile and run the backend application)**:
    使用 Maven wrapper (如果專案內有 `mvnw` 的話，建議使用):
    ```bash
    ./mvnw spring-boot:run
    ```
    或者，如果系統已安裝 Maven:
    ```bash
    mvn spring-boot:run
    ```
    後端服務預設會在 `http://localhost:8080` 啟動。
    您可以透過 `http://localhost:8080/h2-console` 存取 H2 資料庫控制台 (JDBC URL: `jdbc:h2:mem:bookmarkdb`, User Name: `sa`, Password: (空白))。

### 5.3 前端 (Frontend - Vue 3)

1.  **進入前端專案目錄 (Navigate to the frontend directory)**:
    ```bash
    cd path/to/bookmark-manager/frontend
    ```

2.  **安裝依賴套件 (Install dependencies)**:
    ```bash
    npm install
    ```

3.  **啟動開發伺服器 (Start the development server)**:
    ```bash
    npm run dev
    ```
    前端應用程式預設會在 `http://localhost:5173` (或其他 Vite 指定的埠號) 啟動。
    Vite 設定檔 (`vite.config.js`) 中已包含代理設定，會將 `/api` 開頭的請求轉發至後端服務 (`http://localhost:8080/api`)。

4.  **開啟瀏覽器 (Open your browser)**:
    造訪 `http://localhost:5173` 即可開始使用書籤管理器。

### 5.4 執行後端測試 (Running Backend Tests)

在專案根目錄下執行:
```bash
./mvnw test
# 或者
# mvn test
```

## 6. API 端點 (API Endpoints - Backend)

後端提供以下主要的 RESTful API 端點 (基本路徑: `/api/bookmarks`):

*   `POST /` : 新增書籤
*   `GET /` : 獲取所有書籤
*   `GET /{id}` : 根據 ID 獲取特定書籤
*   `PUT /{id}` : 根據 ID 更新特定書籤
*   `DELETE /{id}` : 根據 ID 刪除特定書籤
*   `GET /search?keyword={keyword}` : 根據關鍵字搜尋書籤 (標題或描述)
*   `GET /filter?tag={tag}` : 根據標籤篩選書籤

## 7. 未來可能擴充的功能 (Future Scope - Original)

*   **匯入／匯出**：支援從瀏覽器匯出書籤檔 (HTML) 或匯出為 JSON/CSV 格式。
*   **瀏覽器擴充功能**：製作瀏覽器擴充，讓使用者在瀏覽網頁時能一鍵快速新增書籤。
*   **自動抓取標題**：在新增書籤時，若使用者未填寫標題，可根據輸入的 URL 自動抓取網頁標題。
*   **使用者帳號系統**：支援雲端同步，讓使用者在不同裝置間同步書籤。
