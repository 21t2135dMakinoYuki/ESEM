### Dataset Columns (Data Dictionary)
Each row in the Data/<REPOSITORY_NAME>/<REPOSITORY_NAME>.xlsx file

- `test_name`: Name of the UI test.
- `effort`: Calculated physical interaction effort (p-effort).
- `sequence_number`: Sequential ID of the commit in the repository history.
- `dloc`: Delta Lines of Code (total changes in LOC).
- `lines_added`: Number of lines added in the commit.
- `lines_deleted`: Number of lines deleted in the commit.
- `effort_change_direction`: Indicates whether the effort increased or decreased compared to the previous commit.
- `absolute_change_effort`: The absolute difference in effort score between the current and previous commit.
- `before_change`: Effort score before the commit.
- `after_change`: Effort score after the commit.
- `target_ui_element`: The UI element targeted by the test. (Note: Suffixes like `(1)` or `(2)` indicate the sequence of interactions with the same element).
- `is_in_scope`: Flag (`1`/`0`) indicating if the case was included in the final analysis.
- `specific_change(xxx)_y`: Classification of changes based on our taxonomy. `xxx` refers to the element, and `y` represents the y-th category assigned to that element (unordered).
- `code-changed_ui_element`: The UI element associated with the code change that influenced the target UI element's effort.

## Note on Empty Cells:
`specific_change(xxx)_y` columns indicate that **no specific changes were identified** for that element, or no direct relationship was found between the code change and the UI element. These cases are analyzed separately in our research results (e.g., cases where logic changes impacted effort without structural UI modifications).
`code-changed_ui_element` column: An empty cell indicates that no specific UI element could be identified as the source of the impact on the target element's effort. This occurs when the change was driven by underlying logic or the results of other UI tests rather than a visible modification of UI components.

### Out-of-Scope Data
The file `Data/<REPOSITORY_NAME>/out_of_scope.xlsx` contains data points excluded from the final analysis. Each row represents a test-commit combination that was deemed unsuitable for p-effort quantification.

- `test_name`: Name of the UI test.
- `sequence_number`: Sequential ID of the commit in the repository history.
- `UI_test_change`: Flag (`1`/`0`) indicating the test failed due to significant changes in the UI test script itself (e.g., changes in the test's logic or assertions).
- `carry-over_effects`: Flag (`1`/`0`) indicating the test result was affected by previous test executions (e.g., state not being properly reset), making the effort measurement unreliable.
- `tool's_failure`: Flag (1/0) indicating cases where no evidence of UI modification could be found despite a measured change in effort. This represents a limitation of the analysis (e.g., the cause was undetectable through the current tool's tracking of UI components).

### Accuracy Data
The file `Data/accuracy.xlsx` contains scenarios, their corresponding test names, and the results regarding accuracy.