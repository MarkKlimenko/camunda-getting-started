Feature: Insurance

  Scenario: As a user, I create insurance
  All insurance parameters are valid and process should be finished with SUCCESS status
    Given Start process "insuranceProcess"
      | id   | 123126    |
      | name | User Name |
      | age  | 30        |
    Then Receive task "insurance.risk.determineRisks" with parameters
      | riskLevel | GREEN |
    Then Receive task "insurance.issue.sendInitiatePayment"
    Then Send task "insurance.issue.paymentReceived" with correlation key "123126"
      | null | null |
    Then Receive task "insurance.issue.issuePolicy" with parameters
      | status | SUCCESS |
    Then Receive task "insurance.issue.sendPolicy"