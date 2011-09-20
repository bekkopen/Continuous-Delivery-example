Feature: For the love of cheese
In order to find information about cheese
As a cheese lover
I want the ability to search Google for anything related to cheese

Scenario: The search for cheese
Given I have accessed Google's home page
When I enter the keyword of "Cheese"
And click the Submit button
Then the page title returned should contain "Cheese"