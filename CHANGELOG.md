# Changelog

All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.0.0/),
and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

## [Unreleased]

### Changed
- **Dependency updates**
    - Bumped `com.diffplug.spotless` from 8.1.0 to 8.2.0
    - Bumped `com.gradleup.shadow` from 9.0.0-beta15 to 9.3.1
    - Bumped `de.exlll:configlib-paper` from 4.7.0 to 4.8.0
    - Bumped `net.kyori:adventure-platform-bukkit` from 4.3.2 to 4.4.1
    - Bumped `net.kyori:adventure-text-serializer-ansi` from 4.17.0 to 4.26.1

### Fixed
- Fixed Spotless branch detection in CI/CD environments
- Resolved "No such reference 'origin/main'" error in Gradle builds

### Technical
- Refactored build.gradle for improved Git handling and branch autodetection
- Enhanced Spotless configuration to automatically detect main branch (main/master)
- Added `gitRefExists()` helper method to verify Git references before usage
- Improved error handling in Git-related Gradle tasks
- Added informative logging for Spotless formatting operations
- Updated build.gradle comments and documentation

## [1.0.1] - 2026-01-04

### Changed
- **Complete rewrite of Logger system**
    - Now uses ANSI color serialization for console output
    - Added configurable log levels (TRACE, DEBUG, INFO, SUCCESS, WARN, ERROR, FATAL)
    - Enhanced with timestamps and formatted output
    - Removed dependency on Guice injection for Logger
    - Added exception stack trace support with `error()` and `fatal()` methods
- **Configuration system updates**
    - Migrated from Spongepowered/Configurate to Exlll/ConfigLib
    - Replaced `isDebug` boolean with `logLevel` enum for fine-grained control
- **Dependency updates**
    - Bumped `org.projectlombok:lombok` from 1.18.32 to 1.18.42
    - Bumped `com.nisovin.shopkeepers:ShopkeepersAPI` from 2.24.0 to 2.25.0
    - Bumped `net.kyori:adventure-text-minimessage` from 4.16.0 to 4.26.1
    - Bumped `net.kyori:adventure-text-serializer-plain` from 4.16.0 to 4.26.1

### Added
- Integrated Lamp command framework for command handling
- Added run-paper plugin (`xyz.jpenilla.run-paper`) for development testing
- Implemented Spotless for code formatting and style enforcement
- New Logger utility methods: `separator()`, `header()`, `isDebugEnabled()`, `isTraceEnabled()`
- SUCCESS log level for positive feedback messages
- Dependabot configuration for automated dependency updates

### Technical
- Migrated shadowJar plugin from `com.github.johnrengelman.shadow` to `com.gradleup.shadow` (v9.0.0-beta15)
- Implemented dependency relocation with shadowJar to prevent conflicts
- Optimized JAR size with selective minimize excluding reflection-heavy dependencies
- Added automatic git branch detection for Spotless ratchetFrom
- Configured Google Java Format (AOSP style) with Spotless

## [1.0.0] - 2025-01-03

### Added
- Initial release of ShopkeepersEI plugin
- Base integration with Shopkeepers API
- Configuration system with Spongepowered/Configurate
- Basic logging system with Adventure components
- Dependency injection with Google Guice
