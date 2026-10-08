# Dự Án Kiểm Thử Tự Động Hóa Trang Đăng Nhập - UTC (Văn Phòng Điện Tử)

Dự án Java Maven sử dụng **Selenium WebDriver 4.x**, **JUnit 5**, **AssertJ**, và **WebDriverManager** theo mô hình **Page Object Model (POM)** để kiểm thử tự động hóa chức năng Đăng nhập tại trang web [Văn phòng điện tử UTC](https://vanphongdientu.utc.edu.vn/Login).

---

## 1. Yêu Cầu Hệ Thống & Môi Trường

- **Java Development Kit (JDK)**: JDK 17 trở lên.
- **Build Tool**: Apache Maven 3.8+ (hoặc dùng script có sẵn `run_test.sh` / `run_test.bat`).
- **Trình duyệt**: Google Chrome (mặc định), Mozilla Firefox hoặc Microsoft Edge.
- **Kết nối Internet**: Để WebDriverManager tự động tải driver tương thích với phiên bản trình duyệt.

---

## 2. Cấu Trúc Thư Mục Dự Án (Chuẩn POM & Buổi 8)

```
d:\KiemThuPM\BaiTap
├── pom.xml                                  # Cấu hình Maven dependencies (Selenium 4, JUnit 5, AssertJ)
├── README.md                                # Tài liệu hướng dẫn dự án & danh sách 20 Test Cases
├── run_test.sh                              # Script chạy test nhanh cho Git Bash / Linux
├── run_test.bat                             # Script chạy test nhanh cho Windows CMD / PowerShell
└── src
    ├── main
    │   └── java
    │       └── com
    │           └── example
    │               ├── base
    │               │   └── DriverFactory.java  # Khởi tạo WebDriver (Chrome/Firefox/Edge, Headless, User Profile)
    │               ├── pages
    │               │   ├── BasePage.java       # Tầng cơ sở: Quản lý driver, wait, click(), type(), getText()
    │               │   ├── LoginPage.java      # Page Object: Locators private & nghiệp vụ trang Đăng nhập
    │               │   └── HomePage.java       # Page Object: Kiểm tra trạng thái sau đăng nhập thành công
    │               └── utils
    │                   └── ConfigReader.java   # Đọc file cấu hình config.properties (hỗ trợ UTF-8)
    └── test
        ├── java
        │   └── com
        │       └── example
        │           └── tests
        │               ├── BaseTest.java   # Setup/TearDown WebDriver, chụp screenshot khi test xong/FAIL
        │               └── LoginTest.java  # Chứa toàn bộ 20 Test Cases kiểm thử tự động
        └── resources
            └── config.properties           # Tham số cấu hình (browser, baseUrl, credentials, error msgs)
```

---

## 3. Quản Lý Cấu Hình & Hướng Dẫn Chạy Test

### 3.1 Cấu Hình Trong `src/test/resources/config.properties`

```properties
# Chọn trình duyệt: chrome, firefox, edge
browser=chrome

# URL trang đăng nhập target
baseUrl=https://vanphongdientu.utc.edu.vn/Login

# Chế độ headless (true: chạy ngầm không mở cửa sổ, false: mở giao diện trình duyệt)
headless=false

# Thời gian chờ ngầm định & tường minh (giây)
implicitWait=10
explicitWait=10

# Tài khoản kiểm thử hợp lệ (giả định)
validUsername=huongnt
validPassword=123456@utc

# Thông báo lỗi kỳ vọng
expectedErrNoUser=Bạn chưa nhập tên đăng nhập
expectedErrNoPass=Bạn chưa nhập mật khẩu
expectedErrInvalid=Tài khoản không đúng

# Ngưỡng thời gian phản hồi cho Time-based SQLi (TC19) (giây)
sqliTimeThresholdSeconds=4
```

### 3.2 Lệnh Chạy Kiểm Thử

#### Cách 1: Chạy bằng lệnh Shell Script (Khuyên dùng cho Git Bash)
- **Chạy toàn bộ 20 Test Cases**:
  ```bash
  ./run_test.sh
  ```
- **Chạy riêng 1 Test Case (Ví dụ TC03)**:
  ```bash
  ./run_test.sh "-Dtest=LoginTest#TC03_ValidUser_InvalidPass"
  ```

#### Cách 2: Chạy bằng Batch Script (Cho Windows CMD / PowerShell)
- **Chạy toàn bộ 20 Test Cases**:
  ```cmd
  run_test.bat
  ```
- **Chạy riêng 1 Test Case (Ví dụ TC01)**:
  ```cmd
  run_test.bat "-Dtest=LoginTest#TC01_EmptyUsername"
  ```

#### Cách 3: Chạy trực tiếp qua lệnh Maven tiêu chuẩn
- **Chạy toàn bộ test**:
  ```bash
  mvn clean test
  ```
- **Chạy riêng 1 Test Case**:
  ```bash
  mvn test -Dtest=LoginTest#TC03_ValidUser_InvalidPass
  ```
- **Chạy theo tên phương thức rút gọn**:
  ```bash
  mvn test -Dtest=LoginTest#TC03*
  ```

---

## 4. Danh Sách 20 Test Cases & Đọc Kết Quả Kiểm Thử

| ID | Tên Phương Thức Test | Username | Password | Kỳ Vọng Kết Quả |
|:---:|:---|:---|:---|:---|
| **TC01** | `TC01_EmptyUsername` | *(trống)* | `1256` | Báo lỗi chưa nhập tên đăng nhập |
| **TC02** | `TC02_EmptyPassword` | `huongnt` | *(trống)* | Báo lỗi chưa nhập mật khẩu |
| **TC03** | `TC03_ValidUser_InvalidPass` | `huongnt` | `utc@235` | Báo lỗi "Tài khoản không đúng" |
| **TC04** | `TC04_InvalidUser_ValidPass` | `huongthunguyen` | `123456@utc` | Báo lỗi "Tài khoản không đúng" |
| **TC05** | `TC05_RememberMe_Checked_ReopenBrowser` | `huongnt` | `123456@utc` | Tích "Giữ tôi luôn đăng nhập", đóng/mở lại trình duyệt giữ session |
| **TC06** | `TC06_RememberMe_Unchecked_ReopenBrowser` | `huongnt` | `123456@utc` | KHÔNG tích checkbox, đóng/mở lại chuyển về trang đăng nhập |
| **TC07** | `TC07_EmptyBothFields` | *(trống)* | *(trống)* | Báo lỗi yêu cầu nhập thông tin |
| **TC08** | `TC08_UsernameAllSpaces` | `"   "` | `123456@utc` | Báo lỗi chưa nhập tên đăng nhập (hoặc tự trim) |
| **TC09** | `TC09_PasswordAllSpaces` | `huongnt` | `"   "` | Báo lỗi chưa nhập mật khẩu |
| **TC10** | `TC10_BothInvalid` | `sai_user` | `sai_pass` | Báo lỗi "Tài khoản không đúng" |
| **TC11** | `TC11_UsernameUppercase` | `HUONGNT` | `123456@utc` | Ghi nhận phản hồi thực tế (cho phép hoặc từ chối) |
| **TC12** | `TC12_PasswordMasked` | — | `123456@utc` | Ô mật khẩu có `type="password"`, không hiện plain text |
| **TC13** | `TC13_LoginWithEnterKey` | `huongnt` | `123456@utc` | Gửi form bằng phím Enter thành công |
| **TC14** | `TC14_SQLi_Basic` | `' or 1=1 --` | *bất kỳ* | Bị từ chối, không đăng nhập được (Chỉ chạy trên môi trường test) |
| **TC15** | `TC15_SQLi_SingleQuote` | `huongnt'` | `123456` | Không lỗi 500, không lộ lỗi SQL syntax |
| **TC16** | `TC16_SQLi_AlwaysTrueBothFields` | `' OR '1'='1` | `' OR '1'='1` | Bị từ chối đăng nhập |
| **TC17** | `TC17_SQLi_Comment` | `huongnt'--` | *bất kỳ* | Bị từ chối đăng nhập |
| **TC18** | `TC18_SQLi_UnionSelect` | `' UNION SELECT NULL--` | `test` | Báo lỗi thông thường, không lộ stack trace hay schema |
| **TC19** | `TC19_SQLi_TimeBased` | `huongnt'; WAITFOR DELAY '0:0:5'--` | `123456` | Đo thời gian phản hồi bằng `System.nanoTime()` (< ngưỡng 4s) |
| **TC20** | `TC20_SQLi_InPassword` | `huongnt` | `' OR 'a'='a` | Bị từ chối đăng nhập |

### Đọc Kết Quả Kiểm Thử & Screenshots

1. **Báo cáo Surefire**:
   - Báo cáo chạy test dạng HTML/XML được tự động tạo tại: `target/surefire-reports/index.html`.
2. **Ảnh Chụp Màn Hình (Screenshots)**:
   - Mỗi khi test kết thúc hoặc bị lỗi, ảnh chụp màn hình tự động lưu tại: `target/screenshots/<TestName>_<Timestamp>.png`.

---

## 5. Vai Trò Từng File & Hướng Dẫn Điều Chỉnh Locators

### 5.1 Vai Trò Các File Chính trong Dự Án:
- **`BasePage.java`**: Lớp cha của các Page Object, quản lý `driver` và `WebDriverWait`, định nghĩa các phương thức `click()`, `type()`, `getText()`.
- **`LoginPage.java`**: Đóng gói các phần tử DOM trang Đăng nhập và các thao tác nghiệp vụ (`open()`, `loginAs()`, `setRememberMe()`, `getErrorMessage()`).
- **`HomePage.java`**: Đóng gói kiểm tra trạng thái trang Chủ sau khi đăng nhập.
- **`DriverFactory.java`**: Quản lý khởi tạo trình duyệt Chrome, Firefox, Edge, hỗ trợ chế độ Headless và cấu hình User Profile.
- **`BaseTest.java`**: Quản lý vòng đời `@BeforeEach` khởi tạo driver và `@AfterEach` đóng driver, chụp ảnh màn hình khi fail.
- **`LoginTest.java`**: Chứa 20 phương thức kiểm thử đại diện cho 20 kịch bản test case.

### 5.2 Hướng Dẫn Cập Nhật Locator Khớp Với Giao Diện Thực Tế

Trang web target: `https://vanphongdientu.utc.edu.vn/Login`

Toàn bộ Locators được tập trung khai báo tại đầu file [`LoginPage.java`](file:///d:/KiemThuPM/BaiTap/src/main/java/com/example/pages/LoginPage.java):

```java
public static final String URL = "https://vanphongdientu.utc.edu.vn/Login";

private final By usernameField = By.name("username");
private final By passwordField = By.name("userpwd");
private final By loginButton = By.cssSelector("input.submit_login");
private final By rememberMeCheckboxLocator = By.id("persistent");
private final By rememberMeLabelLocator = By.cssSelector("label.check, label[for='persistent']");
```

#### Các bước lấy lại Locator nếu trang web thay đổi giao diện:
1. Mở trang https://vanphongdientu.utc.edu.vn/Login trên trình duyệt Chrome.
2. Nhấn `F12` (hoặc `Ctrl + Shift + I`) để mở **Chrome DevTools**.
3. Bấm biểu tượng con trỏ chỉ định (`Ctrl + Shift + C`) và click vào phần tử cần lấy locator (ô username, password, nút đăng nhập).
4. Xem thuộc tính HTML trong tab **Elements** (ưu tiên đọc theo `name` -> `class` riêng -> `CSS Selector` -> `XPath`).
5. Cập nhật lại chuỗi Selector tương ứng trong file `LoginPage.java` hoặc cập nhật thông báo lỗi trong `config.properties`.
