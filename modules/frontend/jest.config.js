module.exports = {
  preset: 'ts-jest',
  testEnvironment: "jest-environment-jsdom-fourteen",
  setupFilesAfterEnv: ["<rootDir>/test/test-lib/setupTests.ts"]
};
