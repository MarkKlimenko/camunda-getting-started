Feature: Insurance

  Scenario: As a user, I create SUCCESSFUL insurance
  All insurance parameters are valid and process should be finished with SUCCESS status
    Given Start process "insuranceProcess"
      | id        | {uuid()}  |
      | userLogin | dr.killer |
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
      | <- | id        | {id}        |
      | <- | userLogin | {userLogin} |
    Then Send task "insurance.issue.paymentReceived" with key "{id}"
      | - | - |
    Then Receive task "insurance.issue.issuePolicy"
      | <- | id     | {id}    |
      | -> | status | SUCCESS |
    Then Receive task "insurance.issue.sendPolicy"
      | <- | id        | {id}        |
      | <- | userLogin | {userLogin} |

  Scenario: As a user, I create REJECTED insurance
  Determined risk level is RED and process should be finished with REJECTED status
    Given Start process "insuranceProcess"
      | id        | {uuid()}  |
      | userName  | User Name |
      | userAge   | 30        |
      | autoBrand | Audi      |
    Then Receive task "insurance.risk.determineRisks"
      | <- | id        | {id}        |
      | <- | userName  | {userName}  |
      | <- | userAge   | {userAge}   |
      | <- | autoBrand | {autoBrand} |
      | -> | riskLevel | RED         |
    Then Receive task "insurance.reject.returnFunds"
      | <- | id | {id} |
    Then Receive task "insurance.reject.rejectPolicy"
      | <- | id | {id} |
    Then Receive task "insurance.reject.sendRejection"
      | <- | id | {id} |

  Scenario: As a user, I create REJECTED insurance (fail for returnFunds / manual return)
  Fallback for return funds works successfully. Operator returns funds manually
    Given Start process "insuranceProcess"
      | id        | {uuid()}  |
      | userName  | User Name |
      | userAge   | 30        |
      | autoBrand | Audi      |
    Then Receive task "insurance.risk.determineRisks"
      | <- | id        | {id}        |
      | <- | userName  | {userName}  |
      | <- | userAge   | {userAge}   |
      | <- | autoBrand | {autoBrand} |
      | -> | riskLevel | RED         |
    Then Receive task "insurance.reject.returnFunds" and fail with error "returnFundsError"
      | <- | id | {id} |
    Then Receive task "insurance.reject.operatorAlertReturnFunds"
      | <- | id | {id} |
    Then Send task "insurance.reject.operatorReturnFunds" with key "{id}"
      | - | - |
    Then Receive task "insurance.reject.rejectPolicy"
      | <- | id | {id} |
    Then Receive task "insurance.reject.sendRejection"
      | <- | id | {id} |