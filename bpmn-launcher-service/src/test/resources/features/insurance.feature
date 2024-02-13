Feature: Insurance

  Scenario: As a user, I create insurance
  All insurance parameters are valid and process should be finished with SUCCESS status
    Given Start process "insuranceProcess"
      | id        | uuid()    |
      | userName  | User Name |
      | userAge   | 30        |
      | autoBrand | Audi      |
    Then Receive task "insurance.risk.determineRisks"
      | <- | id        | {id}        |
      | <- | userName  | {userName}  |
      | <- | userAge   | {userAge}   |
      | <- | autoBrand | {autoBrand} |
      | -> | riskLevel | GREEN       |
    Then Receive task "insurance.issue.sendInitiatePayment"
      | <- | id | {id} |
    Then Send task "insurance.issue.paymentReceived" with key "{id}"
      | - | - |
    Then Receive task "insurance.issue.issuePolicy"
      | <- | id     | {id}    |
      | -> | status | SUCCESS |
    Then Receive task "insurance.issue.sendPolicy"
      | <- | id | {id} |