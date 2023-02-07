# Changelog
All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.0.0/),
and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

## [2.0.0] - 2023-01-09
### Changed
- Rename instances of `Credential` to `Passkey`
- Update example app to authenticate with Beyond Identity by using Invocation Type `manual`
- Nest tenantId, realmId, and identityId under appropriate objects in the `Credential`
- Update support links in the example app

### Fixed
- In the example app, if there is no internet connection, an error will be displayed
- Scheme without a path is now recognized as a valid URL when binding a credential

## [1.0.2] - 2022-09-20
### Added
- New attributes to the `Credential` model. `Tenant` now has a `displayName` and `Identity` contains a `primaryEmailAddress`.
### Changed
- Replaced existing `authenticate` function with one that now takes two arguments, `url`and `credentialID`. This is now inline with how consumers of the SDK have been using this function.
