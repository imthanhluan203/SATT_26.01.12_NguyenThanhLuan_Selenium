# AGTT_26.01.12_NguyenThanhLuan_Selenium

This repository is a **Selenium + TestNG automation test project** for the **Safe Railway** demo website:

- `http://saferailway.somee.com/Page/HomePage.cshtml`

Highlights:

- Uses **Page Object Model (POM)**.
- **Modular Data-Driven Locators:** Implemented a strategy that decouples XPath locators into dedicated JSON files mapped 1:1 with each Page Object, utilizing static readers to optimize memory and eliminate code duplication.
- Supports **email confirmation / password reset** flows via **GuerrillaMail**.

---

## 1. Goals & scope

Automate the main business flows of the Safe Railway application:

- Login / Logout
- Register account and **activate account via email**
- Forgot password / Reset password
- Book tickets (multiple scenarios)
- View ticket prices from Timetable
- Cancel booked tickets

---

## 2. Tech stack

- **Java**: the `.classpath` indicates **JavaSE-1.8** (Java 8).
- **Selenium WebDriver**: `selenium-java 4.40.0`
- **TestNG**: test suite execution via `testng.xml`
- **Maven**: `pom.xml`
- **Jackson (JSON)**: used to read locators from `DataProjects/DataObjects/locators.json`

---

## 3. Folder structure

This project is organized using multiple Eclipse “source folders” (per `.classpath`):

```
NguyenThanhLuan/
NguyenThanhLuan/
├─ Common/
│  ├─ Common/
│  │  ├─ Utilities.java
│  │  └─ JsonReader.java
│  ├─ Constant/
│  │  └─ Constant.java
│  └─ Enum/
│     ├─ PageTitle.java
│     ├─ Tab.java
│     ├─ City.java
│     ├─ SeatType.java
│     ├─ TableHeader.java
│     └─ BookTicketFormField.java
│
├─ DataProjects/
│  └─ DataObjects/
│     ├─ UserInfo.java
│     └─ Ticket.java
│
├─ PageObjects/
│  ├─ Railway/
│  │  ├─ GeneralPage.java
│  │  ├─ HomePage.java
│  │  ├─ LoginPage.java
│  │  ├─ RegisterPage.java
│  │  ├─ ResetAccountPage.java
│  │  ├─ BookTicketPage.java
│  │  ├─ TimeTablePage.java
│  │  ├─ TicketPricePage.java
│  │  ├─ TicketPage.java
│  │  └─ FAQPage.java
│  └─ Guerrillamail/
│     └─ GuerrillaMail.java
│
├─ Resource/
│  └─ Locators/
│     ├─ GuerrillaMail/
│     │  └─ GuerrillaMail.json
│     └─ Railway/
│        ├─ BookTicketPage.json
│        ├─ GeneralPage.json
│        ├─ HomePage.json
│        ├─ LoginPage.json
│        ├─ RegisterPage.json
│        ├─ ResetAccountPage.json
│        ├─ TicketPage.json
│        ├─ TicketPricePage.json
│        └─ TimeTablePage.json
│
├─ TestCases/
│  └─ TestPackage/
│     ├─ BaseTest.java
│     ├─ LoginTest.java
│     ├─ LogoutTest.java
│     ├─ CreateAccountTest.java
│     ├─ ResetPasswordTest.java
│     ├─ BookTicketTest.java
│     └─ CancelBookingTest.java
│
├─ testng.xml
├─ pom.xml
├─ target/         (build output)
---

## 4. Architecture & execution flow

### 4.1 Page Object Model (POM)

- Each page (Home/Login/Register/BookTicket/…) is mapped to a class under `PageObjects/Railway/*`.
- Test classes only call Page Object methods, minimizing raw Selenium calls inside tests.

### 4.2 Data-driven locators (JSON)

- 1:1 Mapping Strategy: Locators are strictly decoupled from Java code. Each Page Object automatically maps to its dedicated JSON file located at Resource/Locators/{Domain}/{PageClass}.json.
- Dynamic Loading: Common/JsonReader.java utilizes Jackson ObjectMapper to parse only the required JSON file into memory when a Page Object is instantiated.

Initialization in Page Class::

```java
// Automatically targets "Resource/Locators/Railway/LoginPage.json"
private static JsonReader loginReader = new JsonReader(ObjectType.Railway, LoginPage.class);
```

Static locator example:

```java
// Retrieves straightforward XPath mapped to "txtUsername"
Utilities.enter(loginReader.getLocator("txtUsername"), myUser.getName());
```

Dynamic locator example:

```java
// Under the hood, JsonReader applies String.format() with the provided values.
// Example JSON value: "//div[@id='menu']//a[contains(text(), '%s')]"
By tab = loginReader.getLocator("dynamicTabXpath", tabName.getValue());
```

**JSON Structure**:

Locators are decoupled into dedicated files per Page Object, the JSON structure is simplified into a flat key-value map:

- **Key** = Locator name (e.g., `"txtUsername"`, `"btnLogin"`, `"dynamicTabXpath"`, ...).
- **Value** = XPath string (may contain `%s` placeholders for dynamic formatting via `String.format()`).

> **Important:** The JSON file name must exactly match the Page Object class name (e.g., `LoginPage.java` naturally maps to `LoginPage.json` under the hood via `Class<T>.getSimpleName()`). If you add a new Page Object, simply create a corresponding JSON file in the respective `Resource/Locators/{Domain}/` directory.

### 4.3 WebDriver lifecycle

- `BaseTest.beforeMethod()` initializes WebDriver via TestNG parameter `browser`:
  - `chrome` → `new ChromeDriver()`
  - `firefox` → `new FirefoxDriver()` (default)
- `BaseTest.afterMethod()` always calls `quit()`.

### 4.4 Notable utilities

In `Common/Utilities.java`:

- Explicit wait: `waitForElementLocated(By)` with `WebDriverWait` + `visibilityOfElementLocated`
- JS click: `click(By)` uses `JavascriptExecutor` to reduce click issues
- JS text: `getTextElementByJS(By)` reads `innerText`
- Close tabs: `closeAllTabsExceptMain(PageTitle page)` closes all tabs except the one matching title
- Date helpers: `parseDateToJulian()` and `parseJulianToDate()` (used for selecting dates in Book Ticket)

---

## 5. Modules / components

### 5.1 Common

#### `Constant/Constant.java`
Central constants:

- `RAILWAY_URL`, `EMAIL_URL`
- `USERNAME`, `PASSWORD` (sample account)
- `TIMEOUT_WAIT_SECOND`
- `TODAY` (format `M/d/yyyy`)

#### `Common/JsonReader.java`
Loads `DataProjects/DataObjects/locators.json`.

Main API:

- `By getLocator(PageTitle pageName, String typeOfLocator, Object... values)`

#### `Common/Utilities.java`
Reusable Selenium actions: type/click/get text/wait/scroll, plus helpers for ticket date selection.

---

### 5.2 DataObjects

#### `UserInfo.java`
User model: `name` (email), `password`, `pid`.

- `getUserName()` returns the part before `@` (used to set GuerrillaMail inbox).

#### `Ticket.java`
Ticket model for booking:

- Uses `Enum.City` and `Enum.SeatType` to map to UI display text.
- Includes `duration` (days to add from today or from provided date).

---

### 5.3 PageObjects (Railway)

#### `GeneralPage.java`
Base page for all Railway pages:

- `gotoPage(...)` clicks a tab via dynamic XPath and returns the corresponding Page Object (reflection-based).
- `checkTabPageExist(Tab)` checks whether a tab exists.

#### `HomePage.java`
- `open()` navigates to `Constant.RAILWAY_URL`
- `getWelcomeMessage()` returns “Welcome …”
- `getCreateAccountPage()` clicks `create an account`

#### `LoginPage.java`
- `login(UserInfo)`:
  - type username/password
  - click login
  - if title becomes Home → returns `HomePage`, otherwise returns `LoginPage`
- `clickForgotPassword()` + `sendRequestReset()` support reset flow

#### `RegisterPage.java`
- `register(UserInfo)` fills the register form and submits
- Getters for messages / validation errors

#### `ResetAccountPage.java`
- `resetPassWord(newPass, confirmPass)` and getters for message/token

#### `BookTicketPage.java`
- `bookTicket(Ticket, String... yourday)`:
  - selects date (computed using `duration`)
  - selects depart/arrive/seat/amount if provided
  - returns chosen `departDate` for verification
- `submit()` clicks “Book ticket”

#### `TimeTablePage.java`
- `timeTableAction(City from, City to, TableHeader header)`:
  - iterates table rows to find a route matching `Depart Station` and `Arrive Station`
  - clicks link based on header: `Check Price` or `Book ticket`
  - returns `TicketPricePage` or `BookTicketPage`

#### `TicketPricePage.java`
- `getTableName()` gets price table title
- `getPrice(SeatType)` gets seat price

#### `TicketPage.java`
- `getCellValue(noOfTicket, TableHeader)` reads a cell value by header
- `clickCancelButton(noOfTicket)` + `clickOke()` cancels a ticket

---

### 5.4 PageObjects (GuerrillaMail)

#### `GuerrillaMail.java`
- `setAnEmail()` sets mailbox based on `UserInfo.getUserName()`
- `waitAndClickConfirmEmail()` opens the email and clicks the link in the body to confirm/reset
- `cleanAllOldMail()` selects and deletes old emails

> Note: the `_mailconfirmLink` locator is hard-coded with sender `thanhletraining03@gmail.com`. If the sender changes, update the locator accordingly.

---

## 6. Test cases

The suite is executed via `testng.xml`.

### LoginTest
- **TC1**: Login successfully with valid username/password
- **TC2**: Empty Username → validation error
- **TC3**: Wrong Password → error message
- **TC4**: Multiple failed attempts → “attempts” message appears
- **TC5**: Unactivated account → cannot login

### LogoutTest
- **TC6**: Logout → back to Home and Logout tab disappears

### CreateAccountTest
- **TC7**: Register with existing email → “already in use”
- **TC8**: Empty password & PID → validation errors
- **TC9**: Register + activate account via GuerrillaMail

### ResetPasswordTest
- **TC10**: New password equals old password → error
- **TC11**: Confirm password mismatch → error

### BookTicketTest
- **TC12**: Book 1 ticket
- **TC13**: Book multiple tickets
- **TC14**: Timetable → check price and verify
- **TC15**: Timetable → book ticket and verify auto-filled form

### CancelBookingTest
- **TC16**: Book 1 ticket → My ticket → Cancel → verify ticket removed

---

## 7. How to run

### 7.1 Prerequisites

- Java 8+ (project is configured for JavaSE-1.8)
- Browser:
  - Firefox (default)
  - Chrome
- Stable Internet connection (Safe Railway website + GuerrillaMail)

> Driver note: the code uses `new ChromeDriver()` / `new FirefoxDriver()`. With Selenium 4, Selenium Manager often downloads drivers automatically. If not, install `chromedriver/geckodriver` and add them to PATH, or set system properties accordingly.

### 7.2 Run with TestNG in an IDE (recommended)

1. Import the project into your IDE.
2. Install TestNG plugin (if using Eclipse).
3. Run the suite:
   - Right-click `testng.xml` → **Run As → TestNG Suite**

#### Switching browser

In `testng.xml`:

```xml
<parameter name="browser" value="firefox" />
```

Change to:

```xml
<parameter name="browser" value="chrome" />
```

## 8. How to extend

### 8.1 Add a new locator

1. Ensure the page title exists in `Enum.PageTitle`.
2. Add a new locator key under the correct page title in `locators.json`.
3. Use `JsonReader.getLocator(PageTitle.X, "yourKey")` in Page Objects.

### 8.2 Add a new Page Object

1. Create a class under `PageObjects/Railway` extending `GeneralPage`.
2. Implement user-behavior methods (enter/click/select/verify).

### 8.3 Add a new test case

1. Create a new class under `TestCases/TestPackage` extending `BaseTest`.
2. Add the class to `testng.xml` so it runs in the suite.

---

## 9. Author

- Nguyen Thanh Luan
