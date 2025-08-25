# RequestController Removal Plan

## Issue
RequestController duplicates GradeClaimController functionality without proper security.

## Files to Remove
1. `/controller/RequestController.java` - Completely redundant
2. `/service/RequestService.java` - Generic interface not needed
3. `/service/AbstractRequestService.java` - Only used by GradeClaim, can be simplified

## Keep
- GradeClaimController (has proper security & validation)
- GradeClaimService (specific implementation)
- BaseRequest model (used by GradeClaim entity)

## Action Required
Delete RequestController.java as it provides no additional value.