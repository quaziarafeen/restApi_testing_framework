Feature: Validate Purgomalum API service request and response

  Background: Purgomalum service is working

  @FunctionTests
  Scenario Outline: Validate <fileType> file response
    Given I have selected file type as "<fileType>"
    When I send request with data input string "<data>"
    Then I see "<fileType>" response with entered "<data>" text

    Examples:
      | fileType | data                     |
      | json     | this is some test input  |
      | plain    | this is plain test input |
      | xml      | this is xml test input   |

  @FunctionTests
  Scenario Outline: Validate text contains profanity <isTextProfanity>
    When I send "<data>" with profanity request
    Then I see response as per profanity "<isTextProfanity>"

    Examples:
      | data                    | isTextProfanity |
      | this is $hit test input | true            |
      | this is json test input | false           |

  @FunctionTests
  Scenario Outline: Add and replace profanity words by default with * in <fileType> input file
    Given I have selected file type as "<fileType>"
    When I send request with text "<data>"
    And I add "<text>" to profanity list
    Then I see "<fileType>" with "<modified_data>" response

    Examples:
      | fileType | data                     | text      | modified_data            |
      | json     | this is json test input  | this,json | **** is **** test input  |
      | plain    | this is plain test input | is,test   | th** ** plain **** input |
      | xml      | this is xml test input   | xml,input | this is *** test *****   |


  @FunctionTests
  Scenario Outline: Validate text replace exceeds limit of 20 Characters shows error message in <fileType> file
    Given I have selected file type as "<fileType>"
    When I send request with text "<data>"
    And I add "<add_text>" with exceed limit text "<exceeded_limit_text>" to profanity list
    Then I see "<fileType>" with error "<error_message>" response

    Examples:
      | fileType | data                     | add_text  | exceeded_limit_text                     | error_message                                         |
      | json     | this is json test input  | this,json | this is curiously long replacement text | User Replacement Text Exceeds Limit of 20 Characters. |
      | plain    | this is plain test input | is,test   | this is curiously long replacement text | User Replacement Text Exceeds Limit of 20 Characters. |
      | xml      | this is xml test input   | xml,input | this is curiously long replacement text | User Replacement Text Exceeds Limit of 20 Characters. |