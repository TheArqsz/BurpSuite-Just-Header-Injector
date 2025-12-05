# Burp Suite Custom Header Extension

A Burp Suite extension that allows you to automatically inject custom HTTP headers into all outgoing requests.

---

*Have you ever wanted to configure custom headers in one place and have them automatically work for every request leaving Burp?*

This extension solves that problem. Whether you need to add an `Authorization` token, a custom `X-Bug-Bounty` ID or specific environment headers, you can define them once and let the extension handle the injection.

## Features

- **Global Injection** - automatically adds defined headers to **all** outgoing requests.
- **Scope Control** - a "*Include only in-scope requests*" toggle allows you to prevent leaking headers to out-of-scope targets.
- **Persistent Configuration** - saves your headers and settings automatically.
- **Simple UI** - add, edit or remove headers on the fly without complex session handling rules.

## Installation

1. **Download (Recommended)**

> The easiest way to get started is to use the latest pre-built release.

- Go to the Releases page.
- Download the latest `custom-header-injector.jar`.

2. **Load into Burp Suite**

- Open Burp Suite and navigate to the Extensions tab.
- Click *Add*.
- Select Java as the extension type.
- Select the `custom-header-injector.jar` file you downloaded.

## Build from source

If you prefer to build the extension yourself, you can use Docker or Maven.

### Docker

You can build the extension using Docker, which handles the Maven dependencies for you.

```bash
docker build --output ./build-output .
```

The directory `./build-output` will contain the generated JAR file.

### Maven

1. Ensure you have JDK 17+ and Maven installed.
2. Run the package command:

```bash
mvn clean package -DskipTests
```

3. The extension JAR will be located in the `target/` directory.

## Usage

1. Go to the `Custom Header Injector` tab in Burp Suite.
2. Click `Add Header` to define a new key-value pair.
3. Check `Enable globally` to start injecting headers immediately.
4. (Optional) Check `Include only in-scope requests` if you want headers injected only for targets in your defined Scope.